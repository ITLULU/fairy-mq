package com.fairy.rabbitmq.stream;

import com.fairy.rabbitmq.RabbitmqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 鹿少年
 * @date 2022/11/24 21:29
 */
public class StreamSender {
    private static final String QUEUE_NAME = "streamQueue";

    public static void main(String[] args) throws Exception {
        Connection connection = RabbitmqUtils.getConnection();
        Channel channel = connection.createChannel();
        //声明队列会在服务端自动创建。
//		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        Map<String,Object> params = new HashMap<>();
        //声明Stream队列
        params.put("x-queue-type","stream");
        params.put("x-max-length-bytes", 20_000_000_000L); // maximum stream size: 20 GB
        params.put("x-stream-max-segment-size-bytes", 100_000_000); // size of segment files: 100 MB
        channel.queueDeclare(QUEUE_NAME, true, false, false, params);

        String message = "Hello World!333";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }
}

