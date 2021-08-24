package br.com.integra.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

//@EnableEurekaClient
@SpringBootApplication
public class ApplicationContext {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationContext.class, args);
	}
	
}
