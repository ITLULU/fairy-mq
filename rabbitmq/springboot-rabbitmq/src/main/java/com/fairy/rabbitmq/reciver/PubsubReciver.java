package com.fairy.rabbitmq.reciver;

import com.fairy.rabbitmq.RabbitConstant;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import java.io.IOException;
import java.util.Map;

/**
 * 路由 通配符 监听
 */
@Configuration
@RabbitListener(queues = RabbitConstant.QUEUE_Pubsu_Beijing)
public class PubsubReciver {

    @RabbitHandler
    public void processMessage1(Message message) {
        System.out.println(new String(message.getBody()));
    }



}
