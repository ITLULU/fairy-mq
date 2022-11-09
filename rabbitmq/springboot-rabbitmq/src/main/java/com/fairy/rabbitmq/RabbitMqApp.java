package com.fairy.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 鹿少年
 * @date 2022/11/9 20:17
 */
@SpringBootApplication
@Slf4j
public class RabbitMqApp {

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqApp.class,args);
    }
}
