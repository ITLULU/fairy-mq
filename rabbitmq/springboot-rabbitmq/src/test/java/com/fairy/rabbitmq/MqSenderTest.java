package com.fairy.rabbitmq;

import com.fairy.rabbitmq.delay.DelaySender;
import com.fairy.rabbitmq.send.RabbitMqSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;

/**
 * @author 鹿少年
 * @date 2022/11/10 23:01
 */
@SpringBootTest(classes = RabbitMqApp.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class MqSenderTest {

    @Autowired
    private RabbitMqSender rabbitMqSender;

    @Autowired
    private DelaySender delaySender;

    @Test
    public void sendHelloWorldFirst() throws InterruptedException, UnsupportedEncodingException {

        rabbitMqSender.sendMessge(RabbitConstant.QUEUE_Simple, "hello world简单队列模式 发送消息 num" + 0);
    }

    @Test
    public void sendHelloWorld() throws InterruptedException, UnsupportedEncodingException {

        int num = 20;
        CountDownLatch countDownLatch = new CountDownLatch(num);
        for (int i = 0; i < num; i++) {
            rabbitMqSender.sendMessge(RabbitConstant.QUEUE_Simple, "hello world简单队列模式 发送消息 num" + i);
            countDownLatch.countDown();
        }
        countDownLatch.await();
    }

    @Test
    public void sendHelloWorldWithCorrelationData() throws InterruptedException, UnsupportedEncodingException {

        int num = 20;
        CountDownLatch countDownLatch = new CountDownLatch(num);
        for (int i = 0; i < num; i++) {
            rabbitMqSender.sendMessgeWithCorrelation(RabbitConstant.QUEUE_Simple, "hello world简单队列模式 发送消息 num" + i);
            countDownLatch.countDown();
        }
        countDownLatch.await();
    }

    @Test
    public void sendTopic() throws UnsupportedEncodingException, InterruptedException {
        int num = 20;
        CountDownLatch countDownLatch = new CountDownLatch(num);
        for (int i = 0; i < num; i++) {
            rabbitMqSender.sendMessge(RabbitConstant.EXCHANGE_Topic_Topic, "china.henan.zhengzhou.20201128", "rabbitmq 路由模式 发送消息 num:" + i);
            countDownLatch.countDown();
        }
        countDownLatch.await();
    }

    @Test
    public void sendWorkQueue() throws UnsupportedEncodingException, InterruptedException {
        int num = 20;
        CountDownLatch countDownLatch = new CountDownLatch(num);
        for (int i = 0; i < num; i++) {
            rabbitMqSender.sendMessge(RabbitConstant.QUEUE_WorkQueue, "rabbitmq 工作队列模式 发送消息 num:" + i);
            countDownLatch.countDown();
        }
        countDownLatch.await();
    }

    @Test
    public void sendDelayMsg() throws Exception {
        delaySender.sendDelayMessage();
    }
}
