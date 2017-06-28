package com.mawsitsit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.sql.SQLException;

@SpringBootApplication
public class BookingresourceApplication extends SpringBootServletInitializer{

	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		jsonConverter.setObjectMapper(objectMapper);
		jsonConverter.setDefaultCharset(null);
		return jsonConverter;
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		startH2Server();
		return application.sources(BookingresourceApplication.class);
	}

	private static void startH2Server() {
		try {
			Server h2Server = Server.createTcpServer().start();
			if (h2Server.isRunning(true)) {
				System.out.println("H2 server was started and is running.");
			} else {
				throw new RuntimeException("Could not start H2 server.");
			}
		} catch (SQLException e) {
			throw new RuntimeException("Failed to start H2 server: ", e);
		}
	}

	public static void main(String[] args) {
		startH2Server();
		SpringApplication.run(BookingresourceApplication.class, args);
	}
}
