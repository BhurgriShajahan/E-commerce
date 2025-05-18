package com.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth/test")
public class TestController {

    @ResponseBody
    @GetMapping("/msg")
    public String message(){
        return "Auth Test Controller Is Running Done...";
    }

}
