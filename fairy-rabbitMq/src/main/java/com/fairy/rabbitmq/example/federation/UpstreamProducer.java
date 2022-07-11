package com.fairy.rabbitmq.example.federation;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 上有worker2机器发送消息到fed_exchange
 */
public class UpstreamProducer {
    private static final String EXCHANGE_NAME = "fed_exchange";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("worker2");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setVirtualHost("/mirror");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //发送者只管往exchange里发消息，而不用关心具体发到哪些queue里。
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String message = "LOG INFO 44444";
        channel.basicPublish(EXCHANGE_NAME, "routKey", null, message.getBytes());

        channel.close();
        connection.close();
    }
}
