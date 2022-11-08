package com.fairy.rabbitmq.topic;

import com.fairy.rabbitmq.RabbitConstant;
import com.fairy.rabbitmq.RabbitmqUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author 鹿少年
 * @date 2022/11/8 20:29
 */
public class TopicConsumerShanghai {
    public static void main(String[] args) throws IOException {
        //获取TCP长连接
        Connection connection = RabbitmqUtils.getConnection();
        //获取虚拟连接
        final Channel channel = connection.createChannel();
        //声明队列信息
        channel.queueDeclare(RabbitConstant.QUEUE_Topic_ShangHai, true, false, false, null);

        //指定队列与交换机以及routing key之间的关系
        //通配符模式
        channel.queueBind(RabbitConstant.QUEUE_Topic_ShangHai, RabbitConstant.EXCHANGE_Topic_Topic,  "china.#");

        channel.basicQos(1);
        channel.basicConsume(RabbitConstant.QUEUE_Topic_ShangHai , false , new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("shanghai天气收到气象信息：" + new String(body));
                channel.basicAck(envelope.getDeliveryTag() , false);
            }
        });
    }


}
