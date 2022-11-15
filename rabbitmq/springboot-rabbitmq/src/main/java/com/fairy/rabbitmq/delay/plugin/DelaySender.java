package com.fairy.rabbitmq.delay.plugin;

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
    private RabbitTemplate rabbitTemplate;

    public void sendDelayWithPlugin() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String exchange = "x_delay_exchange";
        String routingKey = "bind.delay.C";
        String msg = "delay延迟队列消息";
        String delayTime = "6000";

        System.out.println("当前时间：" + simpleDateFormat.format(new Date()) + " 开始发送消息：" + msg + "  延迟的时间为：" + delayTime);

        MessageProperties properties = new MessageProperties();
        properties.setExpiration(delayTime);
        Message message = MessageBuilder.withBody(msg.getBytes("utf-8"))
                .andProperties(properties)
//                .setExpiration(delayTime)
                .build();
        rabbitTemplate.convertAndSend(exchange, routingKey, message);


        msg = "我是第二条消息";
        delayTime = "3000";

        System.out.println("当前时间：" + simpleDateFormat.format(new Date()) + " 开始发送消息：" + msg + "  延迟的时间为：" + delayTime);
        message = MessageBuilder.withBody(msg.getBytes("utf-8"))
                .andProperties(properties)
//                .setExpiration(delayTime)
                .build();
        rabbitTemplate.convertAndSend(exchange, routingKey, message);

        Thread.sleep(30000L);
    }
}
