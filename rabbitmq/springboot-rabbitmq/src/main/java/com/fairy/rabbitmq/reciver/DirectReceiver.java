package com.fairy.rabbitmq.reciver;

import com.fairy.rabbitmq.RabbitConstant;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Return;
import com.rabbitmq.client.ReturnCallback;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;

/**
 * @author 鹿少年
 * @date 2022/11/10 22:52
 */
@Configuration
public class DirectReceiver {

    @RabbitListener(queues=RabbitConstant.QUEUE_Simple)
    public void simpelHelloWorldReceive2(String messageStr,Message message, Channel channel) throws IOException {
        System.out.println("simpelHelloWorldReceive2 received message : " +messageStr);
        //必须手动确认接收才可以下次不在消费
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println( String.format("消息被消费成功 ack确认deliveryTag:%s,multiple:%s ",deliveryTag,multiple));
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println( String.format("消息被没有消费成功 nack确认deliveryTag:%s,multiple:%s ",deliveryTag,multiple));

            }
        });

        channel.addReturnListener(new ReturnCallback() {
            @Override
            public void handle(Return returnMessage) {
                System.out.println( String.format("消息没有对于的交换机 转发 或者队列不存在 returnMessage:%s",returnMessage));

            }
        });

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    //直连模式的多个消费者，会分到其中一个消费者进行消费。类似task模式
    //通过注入RabbitContainerFactory对象，来设置一些属性，相当于task里的channel.basicQos
//    @RabbitListener(queues= RabbitConstant.QUEUE_Simple,containerFactory="myListenerFactory")
//    public void simpelHelloWorldReceive1(Message message, Channel channel, String messageStr) throws IOException {
//        System.out.println("simpelHelloWorldReceive1 received message : " +messageStr);
//        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//    }

    @RabbitListener(queues= RabbitConstant.QUEUE_Simple,containerFactory="myListenerFactory")
    public void simpelHelloWorldReceive1(List<Message> messageList, Channel channel) throws IOException {
        for (Message message :messageList){
            System.out.println("simpelHelloWorldReceive1 received message : " +new String(message.getBody(),"utf-8"));
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }

    }
}
