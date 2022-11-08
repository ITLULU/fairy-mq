package com.fairy.rabbitmq.pubsub;

import com.fairy.rabbitmq.MqMessage;
import com.fairy.rabbitmq.RabbitConstant;
import com.fairy.rabbitmq.RabbitmqUtils;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * 消息发布者
 * @author 鹿少年
 * @date 2022/11/8 20:12
 */
public class PubsubProduct {


    public static void main(String[] args) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");

        Connection connection = RabbitmqUtils.getConnection();
        Channel channel = connection.createChannel();

        for(int i = 1 ; i <= 100 ; i++) {
            MqMessage mqMessage = new MqMessage(System.currentTimeMillis()+"","messageA"+i,format.format(new Date()));
            String message = new Gson().toJson(mqMessage);
            //第一个参数交换机名字   其他参数和之前的一样
            channel.basicPublish(RabbitConstant.EXCHANGE_PUBSUB,"" , null , message.getBytes());
        }
        System.out.println("mq消息发送成功");
        channel.close();
        connection.close();
    }
}
