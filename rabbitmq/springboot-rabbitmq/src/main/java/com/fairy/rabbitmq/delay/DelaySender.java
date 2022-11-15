package com.fairy.rabbitmq.delay;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DelaySender {

    @Autowired
    public RabbitTemplate rabbitTemplate;

    public void sendDelayMessage() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String exchange = "delay_exchange";
        String routingKey = "bind.delay.B";
        String msg = "我是第一条消息";
        // 延迟时间
        String delayTime = "6000";

        MessageProperties properties = new MessageProperties();
        properties.setExpiration(delayTime);
        Message message = MessageBuilder.withBody(msg.getBytes("utf-8"))
                .andProperties(properties)
//                .setExpiration(delayTime)
                .build();

        System.out.println("当前时间：" + simpleDateFormat.format(new Date()) + "开始发送消息：" + msg + "  延迟的时间为：" + delayTime);
//        rabbitTemplate.convertAndSend(exchange, routingKey, msg, new MyMessagePostProcessor(delayTime));
        rabbitTemplate.send(exchange,routingKey,message);
        Thread.sleep(30000L);
    }
}

