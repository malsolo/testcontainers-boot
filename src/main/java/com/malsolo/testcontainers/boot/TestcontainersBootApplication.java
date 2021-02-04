package com.malsolo.testcontainers.boot;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@ConfigurationPropertiesScan
public class TestcontainersBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestcontainersBootApplication.class, args);
	}

	@Bean
	public BlockingQueue<String> queue() {
		return new LinkedBlockingQueue<>();
	}

}
