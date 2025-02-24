package com.Gen2Play.VideoService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableDiscoveryClient
public class VideoServiceApplication {

	public static void main(String[] args) {
		// Dotenv dotenv = Dotenv.load();
		// dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		SpringApplication.run(VideoServiceApplication.class, args);
	}

}
