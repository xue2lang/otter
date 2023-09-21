package com.alibaba.otter.client;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author quanlinglong
 * @date 2023/8/24 17:29
 */
@EnableAsync(proxyTargetClass = true)
@SpringBootApplication(scanBasePackages = "com.alibaba.otter.client")
public class OtterClientApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(OtterClientApplication.class);
        application.run(args);
    }
}
