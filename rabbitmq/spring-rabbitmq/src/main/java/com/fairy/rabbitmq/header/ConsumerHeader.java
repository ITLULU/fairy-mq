package com.fairy.rabbitmq.header;

import java.io.IOException;

import com.fairy.rabbitmq.RabbitConstant;
import com.fairy.rabbitmq.RabbitmqUtils;
import com.rabbitmq.client.*;

public class ConsumerHeader {

    public static void main(String[] args) throws Exception {
        // 创建连接和频道
        Connection connection = RabbitmqUtils.getConnection();
        Channel channel = connection.createChannel();

        channel.basicConsume(RabbitConstant.QUEUE_Header_BUSTYP1, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                System.out.println("消费者接收header模式消息body：" + new String(body));
            }
        });

    }
}
