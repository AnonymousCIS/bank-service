package org.anonymous;

import com.netflix.discovery.EurekaClient;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BankServiceApplication {

	@Autowired
	private EurekaClient eurekaClient;

	@PreDestroy
	public void unregister() {
		eurekaClient.shutdown();
	}
	public static void main(String[] args) {
		SpringApplication.run(BankServiceApplication.class, args);
	}

}
