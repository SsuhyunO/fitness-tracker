package com.example.analysis_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients // 다른 서비스와 통신(Feign)을 가능하게 합니다!
public class AnalysisServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnalysisServiceApplication.class, args);
	}

}
