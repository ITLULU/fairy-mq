package com.fairy.rabbitmq.workqueue;

import com.fairy.rabbitmq.MqMessage;
import com.fairy.rabbitmq.RabbitConstant;
import com.fairy.rabbitmq.RabbitmqUtils;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 * 工做队列模式多个消费者消费 平均消费消息
 * @author 鹿少年
 * @date 2022/11/8 19:06
 */
public class WorkQueueProduct {
    public static void main(String[] args) throws IOException, TimeoutException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
        Connection connection = RabbitmqUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(RabbitConstant.QUEUE_WorkQueue, true, false, false, null);

        for(int i = 1 ; i <= 100 ; i++) {
            MqMessage mqMessage = new MqMessage(System.currentTimeMillis()+"","messageA"+i,format.format(new Date()));
            String message = new Gson().toJson(mqMessage);
            channel.basicPublish("" , RabbitConstant.QUEUE_WorkQueue , null , message.getBytes());
        }
        System.out.println("mq消息发送成功");
        channel.close();
        connection.close();
    }


}
