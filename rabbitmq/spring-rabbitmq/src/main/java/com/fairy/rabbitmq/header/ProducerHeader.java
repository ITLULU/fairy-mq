package com.fairy.rabbitmq.header;


import com.fairy.rabbitmq.RabbitConstant;
import com.fairy.rabbitmq.RabbitmqUtils;
import com.rabbitmq.client.*;
import com.rabbitmq.client.AMQP.BasicProperties.Builder;

import java.util.HashMap;
import java.util.Map;

public class ProducerHeader {

    public static void main(String[] args) throws Exception {
        // 创建连接和频道
        Connection connection = RabbitmqUtils.getConnection();
        Channel channel = connection.createChannel();

        /*
            定义一个交换机为header类型
            true:代表持久化
            true:代自动删除(没有consumer时自动销毁)
            false:代表不是内部使用的交换机
            null:传递的参数为null
         */
        String exchangeName = RabbitConstant.EXCHANGE_HEADER;

        // 声明交换机和类型headers
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.HEADERS, true, false, false, null);

        // 定义队列
        channel.queueDeclare(RabbitConstant.QUEUE_Header_BUSTYP1, true, false, false, null);

        // 声明header
        Map<String, Object> bindingHeaders = new HashMap<>();
        /*
            all:代表发送消息时必须匹配header中的所有key/val对
            any:代表发送消息时只需要匹配header中的任意一个key/val对
         */
        bindingHeaders.put("x-match", "all");
        bindingHeaders.put("key1", "147");
        bindingHeaders.put("key2", "258");
        bindingHeaders.put("key3", "369");

        // 将交换机绑定到队列,并指定header信息
        channel.queueBind(RabbitConstant.QUEUE_Header_BUSTYP1, exchangeName, "", bindingHeaders);

        String body = "header....";

        // 消息配置对象
        Builder properties = new AMQP.BasicProperties.Builder();
        // 发送信息携带的header
        Map<String, Object> requestHeader = new HashMap<>();
        requestHeader.put("key1", "147");
        requestHeader.put("key2", "258");
        requestHeader.put("key3", "369");

        properties.headers(requestHeader);

        // 将消息直接发送给交换机(携带指定的参数header),让交换机自己根据header条件去转发到指定queue
        channel.basicPublish(exchangeName, "", properties.build(), body.getBytes());

        channel.close();
        connection.close();
    }
}
