package com.fairy.rocketmq.service.impl;

import com.fairy.rocketmq.service.MqSender;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQLocalRequestCallback;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;

/**
 * @author 鹿少年
 * @version 1.0
 * @date 2022/7/11 21:48
 */
@Service
@Slf4j
public class MqSenderImpl implements MqSender {

    @Value("${rocketmq.consumer.topic}")
    private String TOPIC_NAME;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;


    @Override
    public void sendMessage(String topic, String msg) {
        SendResult sendResult = this.rocketMQTemplate.syncSend(topic, msg);
        SendResult sendResult1 = this.rocketMQTemplate.sendAndReceive(topic,
                msg,
                new Type() {
                    @Override
                    public String getTypeName() {
                        return "type";
                    }
                });
        log.info("事务消息发送结果:{}", sendResult);

    }

    @Override
    public void sendMessageInTransaction(String topic, String msg) {
        String[] tags = new String[]{"TagA", "TagB", "TagC", "TagD", "TagE"};
        for (int i = 0; i < 10; i++) {
            Message<String> message = MessageBuilder.withPayload(msg).build();
            String destination = topic + ":" + tags[i % tags.length];
            SendResult sendResult = rocketMQTemplate.sendMessageInTransaction(destination, message, destination);
            log.info("发送消息结果：{}", sendResult);
        }
    }

    @Override
    public void sendAsynMessag(String topic, String tag, String msg) {
        String destination = topic + ":" + tag;
        Message message = MessageBuilder.withPayload(msg).build();
        rocketMQTemplate.sendAndReceive(destination, message, new RocketMQLocalRequestCallback() {

            @Override
            public void onSuccess(Object o) {
                log.info("----消息：{}发送成功", o);
            }

            @Override
            public void onException(Throwable throwable) {
                log.info("----消息发送失败:{}", throwable);

            }
        });
    }

    @Override
    public void syncSendOrderly(String topic, String tag, String msg) {
        String destination = topic + ":" + tag;
        Message message = MessageBuilder.withPayload(msg).build();
        rocketMQTemplate.sendOneWayOrderly(destination, message, "key");
    }

    @Override
    public void send(String topic, String tag, String msg) {
        String destination = topic + ":" + tag;
        Message message = MessageBuilder.withPayload(msg).build();
        rocketMQTemplate.send(destination, message);
    }
}
