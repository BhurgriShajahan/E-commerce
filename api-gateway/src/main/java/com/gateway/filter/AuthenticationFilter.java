package com.gateway.filter;

import com.gateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private static final Logger logger = Logger.getLogger(AuthenticationFilter.class.getName());

    @Autowired
    private RouteValidator validator;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    private JwtUtil jwtUtil;

    private WebClient webClient;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Autowired
    public AuthenticationFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClient = webClientBuilder.build();
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                logger.info("Request is secured, checking for authorization header");

                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    logger.severe("Missing authorization header");
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    logger.severe("Invalid authorization header format");
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

                final String token = authHeader.substring(7);

                return this.webClient
                        .get()
                        .uri("http://AUTH-SERVICE/v1/auth/validate?token=" + token)
                        .retrieve()
                        .bodyToMono(Boolean.class)
                        .flatMap(response -> {
                            if (Boolean.TRUE.equals(response)) {
                                // Add the token back in headers and continue the chain
                                ServerWebExchange modifiedExchange = exchange.mutate()
                                        .request(builder -> builder.header(HttpHeaders.AUTHORIZATION, "Bearer " + authHeader))
                                        .build();

                                return chain.filter(modifiedExchange);
                            } else {
                                logger.severe("Unauthorized access to application");
                                return handleUnauthorizedResponse(exchange, "Unauthorized access to application");
                            }
                        })
                        .onErrorResume(throwable -> {
                            logger.severe("Error validating authorization token: " + throwable.getMessage());
                            return handleUnauthorizedResponse(exchange, "Unauthorized access to application");
                        });

            }
            return chain.filter(exchange);
        };
    }

    public static class Config {
        // Empty class as placeholder for any future configurations
    }
    private Mono<Void> handleUnauthorizedResponse(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        String errorMessage = "{\"error\":\"" + message + "\"}";

        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory()
                        .wrap(errorMessage.getBytes())))
                .then(exchange.getResponse().setComplete());
    }
}