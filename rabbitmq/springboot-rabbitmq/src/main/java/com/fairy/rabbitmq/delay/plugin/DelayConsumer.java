package com.fairy.rabbitmq.delay.plugin;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/***
 * 延迟队列消费
 */
public class DelayConsumer {

    @RabbitListener(queues = "delay_queue_C")
    public void receiverC(@Payload String msg, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        System.out.println("当前时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "，延迟队列 - dlx_queue_C 收到消息：" + msg);
        channel.basicAck(deliveryTag, false);
    }
}
