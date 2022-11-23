package com.fairy.rabbitmq.delay.plugin;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 延迟插件实现延迟队列的功能
 *
 */
@Configuration
public class XDelayQueue {

    @Value("${rabbitmq.delay.plugins.exchange}")
    private String exchange;
    @Value("${rabbitmq.delay.plugins.routingKey}")
    private String routingKey;
    @Value("${rabbitmq.delay.plugins.type}")
    private String type;

    @Value("${rabbitmq.delay.plugins.queue}")
    private String queue;
    /**
     * 定义延迟交换机
     * 使用 CustomExchange 方式创建自定义的交换机，类型为我们的 - 延迟交换机
     */
    @Bean
    public CustomExchange xDelayExchange() {
        Map<String, Object> args = new HashMap<String, Object>(1);
        // 这里使用直连方式的路由，如果想使用不同的路由行为，可以修改，如 topic
        args.put("x-delayed-type", "direct");
        return new CustomExchange(exchange, type, true, false, args);
    }
    @Bean
    public Queue delayQueueC() {
        return new Queue(queue, true, false, false);
    }
    @Bean
    public Binding bindingDelayExchange() {
        return BindingBuilder.bind(delayQueueC()).to(xDelayExchange()).with(routingKey).noargs();
    }
}
