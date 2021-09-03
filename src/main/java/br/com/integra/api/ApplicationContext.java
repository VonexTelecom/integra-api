package br.com.integra.api;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableEurekaClient
@SpringBootApplication
public class ApplicationContext {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationContext.class, args);
		 TimeZone.setDefault(TimeZone.getTimeZone("GMT-3"));
	}
	
}
