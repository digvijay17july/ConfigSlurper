package com.slurper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan
public class ConfigSlurperApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigSlurperApplication.class, args);
	}

}
