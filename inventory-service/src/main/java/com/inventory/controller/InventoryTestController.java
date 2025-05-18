package com.inventory.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/inventory")
public class InventoryTestController {

    @ResponseBody
    @GetMapping("/run")
    String run(){
        return "Inventory Test Controller Is Running Done...";
    }
}
