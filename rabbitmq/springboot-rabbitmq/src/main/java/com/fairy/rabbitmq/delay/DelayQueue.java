package com.fairy.rabbitmq.delay;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 延迟队列
 */
public class DelayQueue {
    @Bean
    public TopicExchange delayExchange() {
        String exchangeName = "delay_exchange";
        return new TopicExchange(exchangeName);
    }

    // 配置延迟队列
    //延迟队列 (delay_queu_A) 设置 TTL 能让信息在延迟多久后成为死信，
    // 成为死信后的消息都会被投递到死信队列中，这样只需要消费者一直消费死信队列(dlx_queue_A) 里就好了，因为里面的消息都是希望被处理的延迟后的消息。
    @Bean
    public Queue delayQueueA() {
        String queueName = "delay_queue_A";
        // 设置死信发送至 dlx_exchange 交换机，设置路由键为 bind.dlx.A
        String dlxExchangeName = "dlx_exchange";
        String bindDlxRoutingKeyA = "bind.dlx.A";

        Map<String, Object> args = new HashMap<>(3);
        // 设置队列的延迟属性，6秒
        args.put("x-message-ttl", 6000);
        args.put("x-dead-letter-exchange", dlxExchangeName);
        args.put("x-dead-letter-routing-key", bindDlxRoutingKeyA);
        return new Queue(queueName, true, false, false, args);
    }
    @Bean
    public Binding bindingDelayExchange() {
        String routingKey = "bind.delay.A";
        return BindingBuilder.bind(delayQueueA()).to(delayExchange()).with(routingKey);
    }

    // 配置死信队列
    @Bean
    public TopicExchange dlxExchange() {
        String exchangeName = "dlx_exchange";
        return new TopicExchange(exchangeName);
    }
    @Bean
    public Queue dlxQueueA() {
        String queueName = "dlx_queue_A";
        return new Queue(queueName);
    }
    @Bean
    public Binding bindingDlxExchange() {
        String routingKey = "#.A";
        return BindingBuilder.bind(dlxQueueA()).to(dlxExchange()).with(routingKey);
    }

}
