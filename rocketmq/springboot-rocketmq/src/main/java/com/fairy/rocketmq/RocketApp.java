package com.fairy.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.common.message.MessageQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * @author 鹿少年
 * @version 1.0
 * @date 2022/7/10 11:07
 */
@SpringBootApplication
public class RocketApp {

    public static void main(String[] args) {
        SpringApplication.run(RocketApp.class, args);
    }

    @Value("${rocketmq.consumer.topic}")
    private String cosumerTopic;

    @Autowired
    private DefaultMQPushConsumer consumer;

    @PostConstruct
    public void pullMessage(){
//        Set<MessageQueue> consumer.fetchSubscribeMessageQueues(cosumerTopic);
//        consumer.subscribe(cosumerTopic,new MessageSelector());
        while (true){
        }
    }
}
