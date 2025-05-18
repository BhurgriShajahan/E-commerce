package com.payment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/payment")
public class CategoryTestController {

    @ResponseBody
    @GetMapping("/run")
    String run(){
        return "Payment Test Controller Is Running Done...";
    }
}
