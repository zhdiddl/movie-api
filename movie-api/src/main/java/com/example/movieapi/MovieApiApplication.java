package com.example.movieapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = "com.example.moviedomain.entity") // 엔티티 스캔
@EnableJpaRepositories(basePackages = "com.example.movieadapter.db") // 레포지토리 스캔
@ComponentScan(basePackages = {"com.example"}) // 모든 모듈의 빈 스캔
@SpringBootApplication(scanBasePackages = "com.example")
public class MovieApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieApiApplication.class, args);
	}

}
