package com.review.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/rating")
public class RatingTestController {

    @ResponseBody
    @GetMapping("/run")
    String run(){
        return "Rating Test Controller Is Running Done...";
    }
}
