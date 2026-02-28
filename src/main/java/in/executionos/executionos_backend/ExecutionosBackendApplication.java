package in.executionos.executionos_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExecutionosBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExecutionosBackendApplication.class, args);
	}

}
