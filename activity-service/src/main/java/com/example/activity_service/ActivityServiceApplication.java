package com.example.activity_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ActivityServiceApplication {

	public static void main(String[] args) {
		System.out.println("환경변수 체크(USER): " + System.getenv("DB_USERNAME"));
		SpringApplication.run(ActivityServiceApplication.class, args);
	}

}
