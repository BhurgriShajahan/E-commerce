package com.order.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/order")
public class OrderTestController {

    @ResponseBody
    @GetMapping("/run")
    String run(){
        return "Order Test Controller Is Running Done...";
    }
}
