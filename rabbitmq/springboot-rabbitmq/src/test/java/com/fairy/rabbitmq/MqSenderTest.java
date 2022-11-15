package com.fairy.rabbitmq;

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
    public void producer_B() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String exchange = "delay_exchange";
        String routingKey = "bind.delay.B";
        String msg = "我是第一条消息";
        // 延迟时间
        String delayTime = "6000";

        System.out.println("当前时间：" + simpleDateFormat.format(new Date()) + "开始发送消息：" + msg + "  延迟的时间为：" + delayTime);
        rabbitTemplate.convertAndSend(exchange, routingKey, msg, new MyMessagePostProcessor(delayTime));

        msg = "我是第二条消息";
        // 修改延迟时间
        delayTime = "3000";

        System.out.println("当前时间：" + simpleDateFormat.format(new Date()) + "开始发送消息：" + msg + "  延迟的时间为：" + delayTime);
        rabbitTemplate.convertAndSend(exchange, routingKey, msg, new MyMessagePostProcessor(delayTime));

        Thread.sleep(30000L);
    }
}
