package com.fairy.rabbitmq;

import com.fairy.rabbitmq.send.RabbitMqSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author 鹿少年
 * @date 2022/11/10 23:01
 */
@SpringBootTest(classes = RabbitMqApp.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class MqSenderTest {

    @Autowired
    private RabbitMqSender rabbitMqSender;

    @Test
    public void sendHelloWorld(){
        rabbitMqSender.sendMessge(RabbitConstant.QUEUE_Simple,"hello world简单队列模式 发送消息");
    }
}
