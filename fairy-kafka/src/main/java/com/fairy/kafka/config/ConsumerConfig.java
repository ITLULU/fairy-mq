package com.fairy.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

/**
 * @author hll
 * @version 1.0
 * @date 2022/6/30 15:45
 */
@Configuration
public class ConsumerConfig {

    @Value("${spring.kafka.listener.concurrency}")
    private Integer concurrency;
    @Value("${kafka.consumer.topic}")
    private String TOPIC_NAME;

/*    @Bean
    @ConditionalOnMissingBean(name = "kafkaListenerContainerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory(ConsumerFactory consumerFactory) {
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        //设置并发量，小于或等于Topic的分区数 设置消费者组中的线程数量
        factory.setConcurrency(concurrency);
        //必须 设置为批量监听
        factory.setBatchListener(true);
        //消费者监听器自启
        factory.setAutoStartup(true);
        //消费一次提交一次  MANUAL 表示批量提交一次
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }*/


    @Bean
    public NewTopic topic() {
        return new NewTopic(TOPIC_NAME, 2, (short) 3);
    }

}
