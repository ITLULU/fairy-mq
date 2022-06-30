package com.fairy.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 鹿少年
 * @version 1.0
 * @date 2022/5/29 15:49
 */
@EnableTransactionManagement
@SpringBootApplication
public class KafkaApp {
    public static void main(String[] args) {
        SpringApplication.run(KafkaApp.class,args);
    }
}
