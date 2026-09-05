package com.imatcoding.expensetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Applies multiple annotations, the main ones are:
// - @SpringBootConfiguration: Mark class as main config source since extends
//		@Configuration (= bean methods here)
// - @EnableAutoConfiguration: Trigger reading of AutoConfiguration.imports file in
//		.m2/.../spring-boot-autoconfigure jar for @Configuration classes, each
//		one guarded by conditional annotations => scan classpath to select only
//		the needed autoconfigured Beans
// - @ComponentScan: Trigger scan of current package & sub-packages for @Component,
// 		@Service, @Repository, @Controller, @RestController, etc. to register them as Beans
@SpringBootApplication
public class ExpenseTrackerApplication {

	// Running the class starts the application, SpringApplication.run:
	//	- create and return ConfigurableApplicationContext
	//	- run autoconfiguration
	//	- start embedded server
	//	- wire all Beans
	public static void main(String[] args) {
		SpringApplication.run(ExpenseTrackerApplication.class, args);
	}

}
