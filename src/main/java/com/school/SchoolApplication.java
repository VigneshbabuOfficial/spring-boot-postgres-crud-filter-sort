package com.school;

import java.time.Instant;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.annotation.RequestScope;

import com.school.utils.RequestId;
import com.school.utils.SchoolLogger;

@SpringBootApplication(scanBasePackages = "com.school")
public class SchoolApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolApplication.class, args);
	}
	
	@Bean
	public SchoolLogger logger() {
		return SchoolLogger.getLogger(" ##### com.school.api.v1.O ##### ");
	}
	
	@Bean
	@RequestScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
	public RequestId requestId() {

		String string = Long.toString( Instant.now().toEpochMilli() );
		UUID requestId = UUID.nameUUIDFromBytes( string.getBytes() );

		return new RequestId( requestId.toString() );
	}
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
