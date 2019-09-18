package com.syhb.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SwitchingApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SwitchingApplication.class, args);

		RPCSwitching.main();
	}

}
