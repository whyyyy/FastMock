package com.mock;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
@MapperScan("com.mock.mapper")
//@EnableWebMvc
public class MockApplication {

	public static void main(String[] args) {
		SpringApplication.run(MockApplication.class, args);
	}

}
