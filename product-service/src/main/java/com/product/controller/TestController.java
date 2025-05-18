package com.product.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/product/test")
public class TestController {

//    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseBody
    @GetMapping("/msg")
    public String message(){
        return "Product Test Controller Is Running Done...";
    }

}
