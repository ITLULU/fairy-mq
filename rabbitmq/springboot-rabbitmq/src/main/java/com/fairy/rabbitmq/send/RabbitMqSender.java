package com.fairy.rabbitmq.send;

/**
 * 消息发送者
 */

import com.fairy.rabbitmq.supply.MQSenderSupplier;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author 鹿少年
 * @date 2022/11/9 20:20
 */
@Component
public class RabbitMqSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     *
     * @param queue
     * @param msg
     */
    public void sendMessge(String queue, String msg) throws UnsupportedEncodingException {
        rabbitTemplate.convertAndSend(queue, msg);
//        Message message =  MessageBuilder.withBody(msg.getBytes("utf-8")).build();
//        CorrelationData correlationData = new CorrelationData(System.currentTimeMillis()+"");
//        rabbitTemplate.sendAndReceive(message,correlationData);
    }

    public void sendMessgeWithCorrelation(String queue, String msg) throws UnsupportedEncodingException {
        Message message = MessageBuilder.withBody(msg.getBytes("utf-8")).build();
        CorrelationData correlationData = new CorrelationData(System.currentTimeMillis() + "");
        Message returnMessage = MessageBuilder.withBody("return message".getBytes()).build();
        correlationData.setReturnedMessage(returnMessage);
        rabbitTemplate.convertAndSend(queue, message, correlationData);
    }

    /**
     * 发送通配符模式消息
     *
     * @param queue
     * @param exchange
     * @param routingKey
     * @param msg
     */
    public void sendMessge(String queue, String exchange, String routingKey, String msg) throws UnsupportedEncodingException {
        Message message = MessageBuilder.withBody(msg.getBytes("utf-8")).build();
        CorrelationData correlationData = new CorrelationData(System.currentTimeMillis() + "");
        rabbitTemplate.sendAndReceive(exchange, routingKey, message, correlationData);
    }

    public void sendMessge(String exchange, String routingKey, String msg) throws UnsupportedEncodingException {
        Message message = MessageBuilder.withBody(msg.getBytes("utf-8")).build();
        rabbitTemplate.sendAndReceive(exchange, routingKey, message);
    }


    @Autowired
    private ThreadPoolTaskExecutor executor;

    public String sendMessage(String message) {
        CompletableFuture<String> resultCompletableFuture =
                CompletableFuture.supplyAsync(new MQSenderSupplier(message), executor);
        try {
            String result = resultCompletableFuture.get();
            return result;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

}
