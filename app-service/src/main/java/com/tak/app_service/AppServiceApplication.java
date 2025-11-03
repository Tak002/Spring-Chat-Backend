package com.tak.app_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(
		scanBasePackages = {
				"com.tak.app_service",    // 이 모듈
				"com.tak.common"       // 공용 모듈 (엔티티/DTO 등)
		}
)
@EntityScan(basePackages = {
		"com.tak.common",         // AppUser 등 엔티티가 있는 패키지
		"com.tak.app_service"        // 이 모듈 내 엔티티 있으면 추가
})
public class AppServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppServiceApplication.class, args);
	}

}
