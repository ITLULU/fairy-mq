package com.fairy.rabbitmq.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

/**
 * @author 鹿少年
 * @date 2022/11/10 23:00
 */
@Component
public class MyListenenr  implements MessageListener {
    @Override
    public void onMessage(Message message) {
        //打印消息
        System.out.println("监听者接收到新消息："+new String(message.getBody()));
    }
}
