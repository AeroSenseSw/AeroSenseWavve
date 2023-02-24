package com.aerosense;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.aerosense"})
public class ExampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestsdkApplication.class, args);
    }

}
