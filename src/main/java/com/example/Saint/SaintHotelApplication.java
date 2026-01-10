package com.example.Saint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SaintHotelApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaintHotelApplication.class, args);
	}

}
