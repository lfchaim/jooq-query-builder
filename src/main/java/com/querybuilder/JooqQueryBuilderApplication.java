package com.querybuilder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

//@SpringBootApplication
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class JooqQueryBuilderApplication {

	public static void main(String[] args) {
		SpringApplication.run(JooqQueryBuilderApplication.class, args);
	}

}
