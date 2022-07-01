//package com.fairy.kafka.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.core.ProducerFactory;
//import org.springframework.kafka.transaction.KafkaTransactionManager;
//
//
///**
// * @author hll
// * @version 1.0
// * @date 2022/6/30 15:49
// */
//@Configuration
//public class ProductConfig {
//
//
//    /**
//     * 以该方式配置事务管理器：就不能以普通方式发送消息，只能通过 kafkaTemplate.executeInTransaction
//     * 或在方法上加 @Transactional 注解来发送消息，否则报错
//     */
//    @Bean
//    public KafkaTransactionManager kafkaTransactionManager(ProducerFactory producerFactory) {
//        KafkaTransactionManager<String, String> kafkaTransactionManager = new KafkaTransactionManager<>(producerFactory);
//        return kafkaTransactionManager;
//    }
//}
