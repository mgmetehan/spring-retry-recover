package com.mgmetehan.springretryrecover;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class SpringRetryRecoverApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRetryRecoverApplication.class, args);
	}

}
