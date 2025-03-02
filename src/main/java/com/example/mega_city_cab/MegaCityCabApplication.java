package com.example.mega_city_cab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MegaCityCabApplication {

	public static void main(String[] args) {
		SpringApplication.run(MegaCityCabApplication.class, args);
	}

}
