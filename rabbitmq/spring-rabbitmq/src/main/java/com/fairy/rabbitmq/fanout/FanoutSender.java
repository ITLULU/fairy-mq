package com.fairy.rabbitmq.fanout;

import com.fairy.rabbitmq.RabbitmqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * @author 鹿少年
 * @date 2022/11/24 21:44
 */
public class FanoutSender {

    private static final String EXCHANGE_NAME = "fanoutExchange";

    /**
     * exchange有四种类型， fanout topic headers direct
     * fanout类型的exchange会往其上绑定的所有queue转发消息。
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Connection connection = RabbitmqUtils.getConnection();
        Channel channel = connection.createChannel();
        //发送者只管往exchange里发消息，而不用关心具体发到哪些queue里。
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        String message = "LOG INFO 222";
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());

        channel.close();
        connection.close();


    }
}
