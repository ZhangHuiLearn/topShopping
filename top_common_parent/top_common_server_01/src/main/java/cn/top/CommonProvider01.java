package cn.top;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient// eureka的客户端
public class CommonProvider01 {
    public static void main(String[] args) {
        SpringApplication.run(CommonProvider01.class);
    }
}
