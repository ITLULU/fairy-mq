package com.fairy.rabbitmq.reciver;

import com.fairy.rabbitmq.RabbitConstant;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author 鹿少年
 * @date 2022/11/10 22:52
 */
@Configuration
public class DirectReceiver {

    @RabbitListener(queues = RabbitConstant.QUEUE_Simple, containerFactory = "myListenerFactory")
    public void simpelHelloWorldReceive2(String messageStr, Message message, Channel channel) throws IOException {
        System.out.println("simpelHelloWorldReceive2 received message : " + messageStr);
        //模拟消费异常
        int i = 1 / 0;
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    //直连模式的多个消费者，会分到其中一个消费者进行消费。类似task模式
    //通过注入RabbitContainerFactory对象，来设置一些属性，相当于task里的channel.basicQos
//    @RabbitListener(queues= RabbitConstant.QUEUE_Simple,containerFactory="myListenerFactory")
//    public void simpelHelloWorldReceive1(Message message, Channel channel, String messageStr) throws IOException {
//        System.out.println("simpelHelloWorldReceive1 received message : " +messageStr);
//        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//    }

//    @RabbitListener(queues = RabbitConstant.QUEUE_Simple, containerFactory = "myListenerFactory")
//    public void simpelHelloWorldReceive1(@Payload Message message, Channel channel) throws IOException {
//        System.out.println("simpelHelloWorldReceive1 received message : " + new String(message.getBody(), "utf-8"));
//        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//    }
}
