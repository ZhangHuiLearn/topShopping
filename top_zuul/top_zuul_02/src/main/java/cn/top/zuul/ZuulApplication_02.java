package cn.top.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulServer;

@SpringBootApplication
@EnableZuulServer
public class ZuulApplication_02 {
    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication_02.class);
    }
}
