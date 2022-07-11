package com.fairy.rabbitmq.example.sharding;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ShardingProducer {
    private static final String EXCHANGE_NAME = "sharding_exchange";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setVirtualHost("/mirror");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //发送者只管往exchange里发消息，而不用关心具体发到哪些queue里。
        channel.exchangeDeclare(EXCHANGE_NAME, "x-modulus-hash");
        String message = "LOG INFO 44444";
        for (int i = 0; i < 10000; i++) {
            channel.basicPublish(EXCHANGE_NAME, String.valueOf(i), null, message.getBytes());
        }

        channel.close();
        connection.close();
    }
}
