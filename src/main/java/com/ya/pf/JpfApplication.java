package com.ya.pf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JpfApplication {

	public static void main(String[] args) {

		SpringApplication.run(JpfApplication.class, args);
	}

}
