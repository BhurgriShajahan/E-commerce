package com.customutility;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CustomUtilityApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomUtilityApplication.class, args);
		System.out.println("Custom utility started...");
	}

}
