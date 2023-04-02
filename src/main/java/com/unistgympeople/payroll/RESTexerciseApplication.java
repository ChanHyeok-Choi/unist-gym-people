package com.unistgympeople.payroll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("EmployeeRepository")
public class RESTexerciseApplication extends SpringBootServletInitializer {

    public static void main(String... args) {
        SpringApplication.run(RESTexerciseApplication.class, args);
    }
}