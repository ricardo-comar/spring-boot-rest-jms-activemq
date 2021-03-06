package com.github.ricardocomar.activemq.sample;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class DemoJmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoJmsApplication.class, args);
	}
}