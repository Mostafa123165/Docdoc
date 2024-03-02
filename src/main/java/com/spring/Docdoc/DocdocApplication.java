package com.spring.Docdoc;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DocdocApplication {
	public static void main(String[] args) {
		SpringApplication.run(DocdocApplication.class, args);
	}

}
