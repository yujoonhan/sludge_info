package com.greenart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SludgeInfoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SludgeInfoServiceApplication.class, args);
	}

}
