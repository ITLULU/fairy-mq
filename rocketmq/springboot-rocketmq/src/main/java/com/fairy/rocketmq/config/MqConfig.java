package com.fairy.rocketmq.config;

import com.fairy.rocketmq.listener.MessageConcurrentListenerImpl;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.impl.producer.DefaultMQProducerImpl;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 鹿少年
 * @date 2022/7/13 10:03
 */
@Configuration
public class MqConfig {

    @Value("${rocketmq.name-server}")
    private String nameServer;
    @Value("${rocketmq.producer.retry-times-when-send-async-failed}")
    private Integer asyncRetry;
    @Value("${rocketmq.producer.retry-times-when-send-failed}")
    private Integer syncRetry;
    @Value("${rocketmq.producer.group}")
    private String producerGroup;
    @Value("${rocketmq.consumer.group}")
    private String consumerGroup;

    @Bean
    public DefaultMQProducer producer(){
        DefaultMQProducer producer = new DefaultMQProducer();
        producer.setNamesrvAddr(nameServer);
        //生产者组
        producer.setProducerGroup(producerGroup);
        producer.setRetryTimesWhenSendAsyncFailed(asyncRetry);
        producer.setRetryTimesWhenSendFailed(syncRetry);
        return producer;
    }

    @Bean
    public DefaultMQPushConsumer consumer(){
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();
        consumer.setNamesrvAddr(nameServer);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        consumer.setConsumeMessageBatchMaxSize(10);
        //默认15分钟
        consumer.setConsumeTimeout(15);
        consumer.setConsumerGroup(consumerGroup);
        consumer.setPullInterval(10);
        consumer.setMessageListener(new MessageConcurrentListenerImpl());

        return consumer;
    }
}
