package com.fairy.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 鹿少年
 * @version 1.0
 * @date 2022/7/10 22:01
 */
@SpringBootApplication
@Slf4j
public class RabbitMqApp {

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqApp.class,args);
    }
}
