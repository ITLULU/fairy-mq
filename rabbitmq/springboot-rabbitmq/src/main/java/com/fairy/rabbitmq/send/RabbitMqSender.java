package com.fairy.rabbitmq.send;

/**
 * 消息发送者
 */

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * @author 鹿少年
 * @date 2022/11/9 20:20
 */
@Component
public class RabbitMqSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     *
     * @param queue
     * @param message
     */
    public void sendMessge(String queue, String message) {
        rabbitTemplate.convertAndSend(queue, message);
    }

    /**
     * 发送通配符模式消息
     * @param queue
     * @param exchange
     * @param routingKey
     * @param msg
     */
    public void sendMessge(String queue, String exchange, String routingKey, String msg) throws UnsupportedEncodingException {
        Message message =  MessageBuilder.withBody(msg.getBytes("utf-8")).build();
        CorrelationData correlationData = new CorrelationData(System.currentTimeMillis()+"");
        rabbitTemplate.sendAndReceive(exchange,routingKey,message,correlationData);
    }
}
