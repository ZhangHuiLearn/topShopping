package cn.top;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class UserProvider01 {
    public static void main(String[] args) {
        SpringApplication.run(UserProvider01.class, args);
    }
}