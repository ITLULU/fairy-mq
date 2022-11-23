package com.fairy.rabbitmq.delay;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 延迟队列
 */
public class DelayQueue {

    @Value("${rabbitmq.delay.normal.exchange}")
    private String exchange;
    @Value("${rabbitmq.delay.normal.routingKey}")
    private String routingKey;
    @Value("${rabbitmq.delay.normal.queue}")
    private String queue;
    @Value("${rabbitmq.delay.normal.dlxExchange}")
    private String dlxExchange;
    @Value("${rabbitmq.delay.normal.bindDleRoutingKey}")
    private String bindDleRoutingKey;

    @Bean
    public TopicExchange delayExchange() {
        return new TopicExchange(exchange);
    }

    // 配置延迟队列
    //延迟队列 (delay_queu_A) 设置 TTL 能让信息在延迟多久后成为死信，
    // 成为死信后的消息都会被投递到死信队列中，这样只需要消费者一直消费死信队列(dlx_queue_A) 里就好了，因为里面的消息都是希望被处理的延迟后的消息。
    @Bean
    public Queue delayQueueA() {
        // 设置死信发送至 dlx_exchange 交换机，设置路由键为 bind.dlx.A
        Map<String, Object> args = new HashMap<>(3);
        // 设置队列的延迟属性，6秒
        args.put("x-message-ttl", 6000);
        args.put("x-dead-letter-exchange", dlxExchange);
        args.put("x-dead-letter-routing-key", routingKey);
        return new Queue(queue, true, false, false, args);
    }

    @Bean
    public Binding bindingDelayExchange() {
        return BindingBuilder.bind(delayQueueA()).to(delayExchange()).with(routingKey);
    }

    // 配置死信队列
    @Bean
    public TopicExchange dlxExchange() {
        return new TopicExchange(dlxExchange);
    }

    @Bean
    public Queue dlxQueueA() {
        return new Queue(queue);
    }

    @Bean
    public Binding bindingDlxExchange() {
        return BindingBuilder.bind(dlxQueueA()).to(dlxExchange()).with(bindDleRoutingKey);
    }

}
