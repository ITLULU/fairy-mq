package com.fairy.rabbitmq.pubsub;

import com.fairy.rabbitmq.RabbitConstant;
import com.fairy.rabbitmq.RabbitmqUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 消息订阅者 只要订阅了对应的主题就可以接收到消息
 * @author 鹿少年
 * @date 2022/11/8 20:12
 */
public class PubsubConsumerShanghai {

    public static void main(String[] args) throws IOException {
        //获取TCP长连接
        Connection connection = RabbitmqUtils.getConnection();
        //获取虚拟连接
        final Channel channel = connection.createChannel();
        //声明队列信息
        channel.queueDeclare(RabbitConstant.QUEUE_Pubsu_Shanghai, true, false, false, null);

        //queueBind用于将队列与交换机绑定
        //参数1：队列名 参数2：交互机名  参数三：路由key（暂时用不到)
        channel.queueBind(RabbitConstant.QUEUE_Pubsu_Shanghai, RabbitConstant.EXCHANGE_PUBSUB, "");
        channel.basicQos(1);
        channel.basicConsume(RabbitConstant.QUEUE_Pubsu_Shanghai, false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws
                    IOException {
                System.out.println("shanghai收到来着发送方的信息：" + new String(body));
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }
}
