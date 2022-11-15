package com.fairy.rabbitmq.reciver;

import com.fairy.rabbitmq.RabbitConstant;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import java.io.IOException;
import java.util.Map;

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
//        int i = 1 / 0;
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


    @RabbitListener(bindings = {
            @QueueBinding(
                    // 队列配置
                    value = @Queue(value = RabbitConstant.QUEUE_Topic_Beijing,
                            durable = "true",
                            // 配置死信队列的参数
                            arguments = {
                                    @Argument(name = "x-dead-letter-exchange", value = "target_exchange"),
                                    @Argument(name = "x-dead-letter-routing-key", value = "target_routing_key")
                            }),
                    // 交换机配置
                    exchange = @Exchange(
                            value = "rabbit_test_exchange",
                            durable = "true",
                            type = ExchangeTypes.TOPIC),
                    key = "rabbit.test.*")},
            // 可以指定容器工厂，默认使用 rabbitListenerContainerFactory
            containerFactory = "rabbitListenerContainerFactory",
            // 指定消费者的线程数量,一个线程会打开一个Channel，一个队列上的消息只会被消费一次（不考虑消息重新入队列的情况）,下面的表示至少开启5个线程，最多10个。线程的数目需要根据你的任务来决定，如果是计算密集型，线程的数目就应该少一些
            concurrency = "5-10")
    public void handleMessage(@Payload String message,
                              @Headers Map<String,Object> headers,
                              @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
                              // 消费者标签，用来区分多个消费者
                              @Header(AmqpHeaders.CONSUMER_TAG) String consumerTag,
                              // message_id
                              @Header(value = AmqpHeaders.MESSAGE_ID, required = false) String messageId,
                              // 该消息是否多次(>1)交付
                              @Header("amqp_redelivered") boolean redelivered) {
        System.out.println("====消费消息===handleMessage");
        System.out.println("消息：" + message);
        System.out.println("头：" + headers);
        System.out.println("deliveryTag：" + deliveryTag);
        System.out.println("messageId：" + messageId);
        System.out.println("redelivered：" + redelivered);
    }
}
