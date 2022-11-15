package com.fairy.rabbitmq.reciver;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import java.io.IOException;
import java.util.Map;

/**
 * 路由 通配符 监听
 */
@Configuration
public class RoutingReciver {

//    @RabbitListener(bindings = {
//            @QueueBinding(
//                    value = @Queue(value = "${rabbitmq.queue.routing.beijing}", durable = "true", autoDelete = "false"),
//                    exchange = @Exchange(
//                            value = "${rabbitmq.exchange.routing}",
//                            durable = "true",
//                            type = ExchangeTypes.TOPIC),
//                    key = "china.#")},concurrency = "3",exclusive = false)
//    public void receive(@Payload String msg, Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
//        System.out.println("路由监听接受到发送者发送的信息：" + msg);
//        // 确认消息
////        channel.basicAck(deliveryTag, false);
//    }

    @RabbitListener(containerFactory = "myListenerFactory", bindings = {
            @QueueBinding(
                    value = @Queue(value = "${rabbitmq.queue.routing.beijing}", durable = "true", autoDelete = "false"),
                    exchange = @Exchange(
                            value = "${rabbitmq.exchange.routing}",
                            durable = "true",
                            type = ExchangeTypes.TOPIC),
                    key = "china.#")}, id = "autoStart")
    public void receive(@Payload Message message, Channel channel,
                        @Headers Map<String, Object> headers,
                        @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey, @Header(AmqpHeaders.MESSAGE_ID) String messageId, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        System.out.println("路由监听接受到发送者发送的信息：" + new String(message.getBody()));
//        int i = 1 / 0;
        // 确认消息
        channel.basicAck(deliveryTag, false);
    }

    @RabbitListener(containerFactory = "myListenerFactory", bindings = {
            @QueueBinding(
                    value = @Queue(value = "${rabbitmq.queue.routing.shanghai}", durable = "true", autoDelete = "false"),
                    exchange = @Exchange(
                            value = "${rabbitmq.exchange.routing}",
                            durable = "true",
                            type = ExchangeTypes.TOPIC),
                    key = "china.#")}, id = "autoStart-shanghai")
    public void receive2(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        System.out.println("receive2路由监听接受到发送者发送的信息：" + new String(message.getBody()));
//        int i = 1 / 0;
        // 确认消息
        channel.basicAck(deliveryTag, false);
    }
}
