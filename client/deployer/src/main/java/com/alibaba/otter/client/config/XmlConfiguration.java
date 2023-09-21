package com.alibaba.otter.client.config;

import com.alibaba.otter.client.service.ConfigService;
import com.alibaba.otter.client.service.impl.ConfigServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author quanlinglong
 * @date 2023/8/29 09:38
 */
@Configuration
@ImportResource("classpath:applicationContext.xml")
public class XmlConfiguration {
    @Bean
    public ConfigService taskService() {
        return new ConfigServiceImpl();
    }
}
