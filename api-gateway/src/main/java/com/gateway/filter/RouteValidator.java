package com.gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/v1/auth/**",
            "/v1/auth/test/**",
            "/v1/product/**",
            "/v1/category/**",
            "/v1/product/test/**",
            "/v1/payment/**",
            "/v1/cart/**",
            "/v1/order/**",
            "/v1/rating/**",
            "/v1/notification/**",
            "/v1/inventory/**",
            "/eureka"

    );

    private final PathMatcher pathMatcher = new AntPathMatcher();

//    public Predicate<ServerHttpRequest> isSecured =
//            request -> openApiEndpoints
//                    .stream()
//                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> pathMatcher.match(uri, request.getURI().getPath()));

}