package com.dairy.farm.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DairyFarmManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(DairyFarmManagementApplication.class, args);
	}

}
