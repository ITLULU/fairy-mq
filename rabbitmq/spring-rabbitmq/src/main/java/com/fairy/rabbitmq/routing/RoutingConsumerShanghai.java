package com.fairy.rabbitmq.routing;

import com.fairy.rabbitmq.RabbitConstant;
import com.fairy.rabbitmq.RabbitmqUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author 鹿少年
 * @date 2022/11/8 20:29
 */
public class RoutingConsumerShanghai {
    public static void main(String[] args) throws IOException {
        //获取TCP长连接
        Connection connection = RabbitmqUtils.getConnection();
        //获取虚拟连接
        final Channel channel = connection.createChannel();
        //声明队列信息
        channel.queueDeclare(RabbitConstant.QUEUE_Routing_Shanghai, true, false, false, null);

        //指定队列与交换机以及routing key之间的关系
        channel.queueBind(RabbitConstant.QUEUE_Routing_Shanghai, RabbitConstant.EXCHANGE_ROUTING_Topic, "us.cal.lsj.20201127");
        channel.queueBind(RabbitConstant.QUEUE_Routing_Shanghai, RabbitConstant.EXCHANGE_ROUTING_Topic, "china.hubei.wuhan.20201127");
        channel.queueBind(RabbitConstant.QUEUE_Routing_Shanghai, RabbitConstant.EXCHANGE_ROUTING_Topic, "us.cal.lsj.20201128");
        channel.queueBind(RabbitConstant.QUEUE_Routing_Shanghai, RabbitConstant.EXCHANGE_ROUTING_Topic, "china.henan.zhengzhou.20201128");


//        channel.queueBind(RabbitConstant.QUEUE_Routing_Shanghai, RabbitConstant.EXCHANGE_ROUTING_direct, "us.cal.lsj.20201127");
//        channel.queueBind(RabbitConstant.QUEUE_Routing_Shanghai, RabbitConstant.EXCHANGE_ROUTING_direct, "china.hubei.wuhan.20201127");
//        channel.queueBind(RabbitConstant.QUEUE_Routing_Shanghai, RabbitConstant.EXCHANGE_ROUTING_direct, "us.cal.lsj.20201128");
//        channel.queueBind(RabbitConstant.QUEUE_Routing_Shanghai, RabbitConstant.EXCHANGE_ROUTING_direct, "china.henan.zhengzhou.20201128");

        //如果是广播模式会将所以与交换机绑定的队列发送 不符合
//        channel.queueBind(RabbitConstant.QUEUE_Routing_Shanghai, RabbitConstant.EXCHANGE_ROUTING_Fanout, "us.cal.lsj.20201127");
//        channel.queueBind(RabbitConstant.QUEUE_Routing_Shanghai, RabbitConstant.EXCHANGE_ROUTING_Fanout, "china.hubei.wuhan.20201127");
//        channel.queueBind(RabbitConstant.QUEUE_Routing_Shanghai, RabbitConstant.EXCHANGE_ROUTING_Fanout, "us.cal.lsj.20201128");
//        channel.queueBind(RabbitConstant.QUEUE_Routing_Shanghai, RabbitConstant.EXCHANGE_ROUTING_Fanout, "china.henan.zhengzhou.20201128");

        channel.basicQos(1);
        channel.basicConsume(RabbitConstant.QUEUE_Routing_Shanghai , false , new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("shanghai天气收到气象信息：" + new String(body));
                channel.basicAck(envelope.getDeliveryTag() , false);
            }
        });
    }


}
