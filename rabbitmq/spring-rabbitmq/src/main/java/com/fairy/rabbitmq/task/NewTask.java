package com.fairy.rabbitmq.task;

import com.fairy.rabbitmq.RabbitmqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

/**
 * @author 鹿少年
 * @date 2022/11/24 21:31
 */
public class NewTask {

    /**
     * 发布一个task，交由多个Worker去处理。 每个task只要由一个Worker完成就行。
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Connection connection = RabbitmqUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("work", true, false, false, null);
        String message = "task 1";
        channel.basicPublish("", "work",
                MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());

        channel.close();
        connection.close();
    }
}
