package com.fairy.rabbitmq.delay.plugin;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 延迟插件实现延迟队列的功能
 *
 */
public class XDelayQueue {

    /**
     * 定义延迟交换机
     * 使用 CustomExchange 方式创建自定义的交换机，类型为我们的 - 延迟交换机
     */
    @Bean
    public CustomExchange xDelayExchange() {
        String exchangeName = "x_delay_exchange";
        Map<String, Object> args = new HashMap<String, Object>(1);
        // 这里使用直连方式的路由，如果想使用不同的路由行为，可以修改，如 topic
        args.put("x-delayed-type", "direct");
        return new CustomExchange(exchangeName, "x-delayed-message", true, false, args);
    }
    @Bean
    public Queue delayQueueC() {
        String queueName = "delay_queue_C";
        return new Queue(queueName, true, false, false);
    }
    @Bean
    public Binding bindingDelayExchange() {
        String routingKey = "bind.delay.C";
        return BindingBuilder.bind(delayQueueC()).to(xDelayExchange()).with(routingKey).noargs();
    }
}
