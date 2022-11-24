package com.fairy.rabbitmq.federation;

import com.fairy.rabbitmq.RabbitmqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 下游服务通过Federation同步到Upsteram的消息，并消费。
 *
 * @author 鹿少年
 * @date 2022/11/24 21:20
 */
public class UpstreamProducer {
    private static final String EXCHANGE_NAME = "fed_exchange";

    public static void main(String[] args) throws Exception {
        Connection connection = RabbitmqUtils.getConnection();
        Channel channel = connection.createChannel();
        //发送者只管往exchange里发消息，而不用关心具体发到哪些queue里。
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String message = "LOG INFO 44444";
        channel.basicPublish(EXCHANGE_NAME, "routKey", null, message.getBytes());

        channel.close();
        connection.close();
    }
}
