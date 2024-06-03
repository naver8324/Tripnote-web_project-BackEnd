package com.elice.tripnote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TripnoteApplication {

	public static void main(String[] args) {
		SpringApplication.run(TripnoteApplication.class, args);
	}

}
