package com.syhb.project;

import com.syhb.project.server.RPCServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ServerApplication.class, args);

		RPCServer.main();

	}

}
