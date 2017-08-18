package io.github.rodrik.tracehandler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TraceHandlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TraceHandlerApplication.class, args);
	}
}
