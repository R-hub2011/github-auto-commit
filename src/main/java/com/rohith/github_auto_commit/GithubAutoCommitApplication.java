package com.rohith.github_auto_commit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GithubAutoCommitApplication {

	public static void main(String[] args) {
		SpringApplication.run(GithubAutoCommitApplication.class, args);


	}

}
