package com.fairy.rabbitmq.reciver;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RejectReciever {

    /**
     * 这种方式会自动创建不存在的 normal_work_queue
     * @param msg
     * @param channel
     * @param deliveryTag
     * @throws IOException
     */
    @RabbitListener(queuesToDeclare = @Queue("normal_work_queue"))
    public void receiver(String msg, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        System.out.println("工作队列 normal_ work_queue 消费信息：" + msg);
        System.out.println("将消息拒绝放回队列中");
        channel.basicReject(deliveryTag, false);
        // 两种方式都可以，但 requeue 必须为 false
//        channel.basicNack(deliveryTag, false, false);
    }

    @RabbitListener(queues = "queue_dlx_A")
    public void receiver2(String msg, Channel channel) throws IOException {
        System.out.println("死信交换机下 queue_dlx_A 队列接收到消息：" + msg);
        channel.basicAck(1, false);
    }

    @RabbitListener(queues = "queue_dlx_B")
    public void receiver3(String msg, Channel channel) throws IOException {
        System.out.println("死信交换机下 queue_dlx_B 队列接收到消息：" + msg);
        channel.basicAck(1, false);
    }
}
