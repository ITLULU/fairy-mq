package com.fairy.rabbitmq.topic;

import com.fairy.rabbitmq.RabbitConstant;
import com.fairy.rabbitmq.RabbitmqUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author 鹿少年
 * @date 2022/11/8 20:29
 */
public class TopicConsumerBeijing {

    public static void main(String[] args) throws IOException {
        //获取TCP长连接
        Connection connection = RabbitmqUtils.getConnection();
        //获取虚拟连接
        final Channel channel = connection.createChannel();
        //声明队列信息
        channel.queueDeclare(RabbitConstant.QUEUE_Topic_Beijing, true, false, false, null);

        //指定队列与交换机以及routing key之间的关系
        //topic通配符 通配符规则：# 匹配一个或多个词，* 匹配不多不少恰好1个词，例如：item.# 能够匹配 item.insert.abc 或者 item.insert，item.* 只能匹配 item.insert
        channel.queueBind(RabbitConstant.QUEUE_Topic_Beijing, RabbitConstant.EXCHANGE_Topic_Topic, "*.*.*.20201127");

        channel.basicQos(1);
        channel.basicConsume(RabbitConstant.QUEUE_Topic_Beijing , false , new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("beijing天气收到气象信息：" + new String(body));
                channel.basicAck(envelope.getDeliveryTag() , false);
            }
        });
    }
}
