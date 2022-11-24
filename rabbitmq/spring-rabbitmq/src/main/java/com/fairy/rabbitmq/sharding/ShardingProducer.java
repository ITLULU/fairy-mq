package com.fairy.rabbitmq.sharding;

import com.fairy.rabbitmq.RabbitmqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author 鹿少年
 * @date 2022/11/24 21:25
 */
public class ShardingProducer {
    private static final String EXCHANGE_NAME = "sharding_exchange";

    public static void main(String[] args) throws Exception{
        Connection connection = RabbitmqUtils.getConnection();
        Channel channel = connection.createChannel();
        //发送者只管往exchange里发消息，而不用关心具体发到哪些queue里。
        channel.exchangeDeclare(EXCHANGE_NAME, "x-modulus-hash");
        String message = "LOG INFO 44444";
        for(int i = 0 ; i < 10000 ; i ++){
            channel.basicPublish(EXCHANGE_NAME, String.valueOf(i), null, message.getBytes());
        }

        channel.close();
        connection.close();
    }
}

