package project.dealdrop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DealdropApplication {

	public static void main(String[] args) {
		SpringApplication.run(DealdropApplication.class, args);
	}

}
