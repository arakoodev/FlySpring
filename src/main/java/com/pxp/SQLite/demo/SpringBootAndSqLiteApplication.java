package com.pxp.SQLite.demo;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class SpringBootAndSqLiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootAndSqLiteApplication.class, args);
	}

}
