package com.ting.R3S;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class R3SApplication {

	public static void main(String[] args) {
		SpringApplication.run(R3SApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/login")
						.allowedOrigins("http://localhost")
						.allowedMethods("GET", "POST", "PUT", "DELETE")
						.allowCredentials(true)
						.maxAge(3600);

				registry.addMapping("/get-session")
						.allowedOrigins("http://localhost")
						.allowedMethods("GET", "POST", "PUT", "DELETE")
						.allowCredentials(true)
						.maxAge(3600);

				registry.addMapping("/logout")
						.allowedOrigins("http://localhost")
						.allowedMethods("GET", "POST", "PUT", "DELETE")
						.allowCredentials(true)
						.maxAge(3600);

				registry.addMapping("/sales")
						.allowedOrigins("http://localhost")
						.allowedMethods("GET", "POST", "PUT", "DELETE")
						.allowCredentials(true)
						.maxAge(3600);

				registry.addMapping("/account")
						.allowedOrigins("http://localhost")
						.allowedMethods("GET", "POST", "PUT", "DELETE")
						.allowCredentials(true)
						.maxAge(3600);

				registry.addMapping("/restaurant")
						.allowedOrigins("http://localhost")
						.allowedMethods("GET", "POST", "PUT", "DELETE")
						.allowCredentials(true)
						.maxAge(3600);

				registry.addMapping("/employee")
						.allowedOrigins("http://localhost")
						.allowedMethods("GET", "POST", "PUT", "DELETE")
						.allowCredentials(true)
						.maxAge(3600);

				registry.addMapping("/projectedsales")
						.allowedOrigins("http://localhost")
						.allowedMethods("GET", "POST", "PUT", "DELETE")
						.allowCredentials(true)
						.maxAge(3600);

				registry.addMapping("/schedule")
						.allowedOrigins("http://localhost")
						.allowedMethods("GET", "POST", "PUT", "DELETE")
						.allowCredentials(true)
						.maxAge(3600);
			}
		};
	}
}
