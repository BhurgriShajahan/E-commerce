package com.notification.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/notification")
public class NotificationTestController {

    @ResponseBody
    @GetMapping("/run")
    String run(){
        return "Notification Test Controller Is Running Done...";
    }
}
