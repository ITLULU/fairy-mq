package com.fairy.rabbitmq.delay.plugin;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DelaySender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.delay.plugins.exchange}")
    private String exchange;
    @Value("${rabbitmq.delay.plugins.routingKey}")
    private String routingKey;

    public void sendDelayWithPlugin() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String msg = "delay plugins插件延迟 我是第一条消息 延迟6000";
        String delayTime = "6000";

        System.out.println("当前时间：" + simpleDateFormat.format(new Date()) + " 开始发送消息：" + msg + "  延迟的时间为：" + delayTime);

        MessageProperties properties = new MessageProperties();
        properties.setExpiration(delayTime);
        Message message = MessageBuilder.withBody(msg.getBytes("utf-8"))
                .andProperties(properties)
//                .setExpiration(delayTime)
                .build();
        rabbitTemplate.convertAndSend(exchange, routingKey, message);


        msg = "delay plugins插件延迟我是第二条消息 延迟3000";
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
