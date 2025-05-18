package com.cart.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/cart")
public class CartTestController {

    @ResponseBody
    @GetMapping("/run")
    String run(){
        return "Cart Test Controller Is Running Done...";
    }
}
