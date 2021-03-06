package cn.top.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class ZuulApplication_01 {
    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication_01.class);
    }
}
