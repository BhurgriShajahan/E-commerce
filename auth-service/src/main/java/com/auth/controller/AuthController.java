package com.auth.controller;

import com.auth.model.dto.request.LoginDto;
import com.auth.model.dto.request.UserDto;
import com.auth.repository.UserRepository;
import com.auth.service.AuthService;
import com.customutility.model.CustomResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @ResponseBody
    @GetMapping("/msg")
    public String msg() {
        return "done..";
    }

    @PostMapping("/register")
    public CustomResponseEntity registerNewUser(@RequestBody UserDto userDto) throws Exception {
        return authService.registerNewUser(userDto);
    }

    @PostMapping("/login")
    public CustomResponseEntity login(@RequestBody LoginDto loginDto) {
        try{
            return authService.login(loginDto);
        }
        catch (Exception exception){
            return CustomResponseEntity.error(exception.getMessage());
        }
    }
    @GetMapping("/validate")
    public Boolean validate(@RequestParam("token") String token) {
        long currentTime = System.currentTimeMillis();
        return authService.findBySessionToken(token, currentTime);
    }
}

