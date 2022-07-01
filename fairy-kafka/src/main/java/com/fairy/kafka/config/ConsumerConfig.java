//package com.fairy.kafka.config;
//
//import org.apache.kafka.clients.admin.NewTopic;
//import org.springframework.beans.factory.annotation.Configurable;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.config.KafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
//import org.springframework.kafka.listener.ContainerProperties;
//
///**
// * @author hll
// * @version 1.0
// * @date 2022/6/30 15:45
// */
//@Configuration
//public class ConsumerConfig {
//
//    @Value("${spring.kafka.listener.concurrency}")
//    private Integer concurrency;
//    @Value("${kafka.consumer.topic}")
//    private String TOPIC_NAME;
//
//
//
//    @Bean
//    public NewTopic topic() {
//        return new NewTopic(TOPIC_NAME, 2, (short) 3);
//    }
//
//}
