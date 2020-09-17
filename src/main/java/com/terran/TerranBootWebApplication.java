package com.terran;

import java.util.Collections;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
@SpringBootApplication
public class TerranBootWebApplication extends SpringBootServletInitializer {
	
	//this method purpose is to make spring can run in any webserver
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(TerranBootWebApplication.class);
	}
	//this method to run application as standalone java application
	public static void main(String[] args) throws Exception {
		SpringApplication app = new SpringApplication(TerranBootWebApplication.class);
        app.setDefaultProperties(Collections
          .singletonMap("server.port", "8084"));
        app.run(args);
	}
}
