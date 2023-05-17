package com.airlinesystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AirlineSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirlineSystemApplication.class, args);
		System.out.println("Airline-Reservation-System is working...  :)");
	}

}
