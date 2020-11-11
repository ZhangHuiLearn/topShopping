package cn.top;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages="cn.top.mapper")
@EnableFeignClients(basePackages="cn.top.client")
public class UserProvider01 {
    public static void main(String[] args) {
        SpringApplication.run(UserProvider01.class, args);
    }
}
