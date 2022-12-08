package com.mobilemessagesgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class MobileMessagesGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MobileMessagesGatewayApplication.class, args);
	}

}
