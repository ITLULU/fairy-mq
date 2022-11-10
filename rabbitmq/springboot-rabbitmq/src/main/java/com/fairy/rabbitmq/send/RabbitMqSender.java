package com.fairy.rabbitmq.send;

/**
 * 消息发送者
 */

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 鹿少年
 * @date 2022/11/9 20:20
 */
@Component
public class RabbitMqSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送
     */
    public void testHelloWorld(){
        //2.发送消息

    }

    /**
     * 发送消息
     * @param queue
     * @param message
     */
    public void sendMessge(String queue, String message) {
        rabbitTemplate.convertAndSend(queue,message);
    }
}
