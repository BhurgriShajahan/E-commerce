package com.review;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class ReviewAndRatingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewAndRatingServiceApplication.class, args);
		System.out.println("Review_&_Rating Service started...");
	}

}
