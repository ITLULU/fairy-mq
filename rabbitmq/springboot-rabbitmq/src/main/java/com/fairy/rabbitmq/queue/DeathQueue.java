package com.fairy.rabbitmq.queue;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DeathQueue {

    /**
     * 正常队列指定死信队列参数信息
     *
     * @return
     */
    @Bean
    public Queue workNormalQueue() {
        String queueName = "nomarl_work_queue";
        // 要指定的死信交换机
        String deadExchangeName = "rabbit_dlx_exchange";
        // 路由键  这里模拟交给死信交换机下的 A 队列中
        String deadRoutingKey = "routing.a.key";
//        在正常工作队列 work_queue 的配置中注入了 Map<String,Object> 参数，用来配置
//        x-dead-letter-exchange 标识一个交换机
//        x-dead-letter-routing-key 来标识一个绑定键。
        Map<String, Object> args = new HashMap<>(2);
        args.put("x-dead-letter-exchange", deadExchangeName);
        args.put("x-dead-letter-routing-key", deadRoutingKey);

        // 队列的过期时间
//        params.put("x-message-ttl",5000);
        // 设置队列的最大长度限制
//        params.put("x-max-length",10);
        return new Queue(queueName, true, false, false, args);
    }
    // 声明死信队列
    @Bean("queue_dlx")
    public Queue queueDlx() {
        return QueueBuilder.durable("queue_dlx").build();
    }


    // 声明死信交换机
    @Bean("test_exchange_dlx")
    public TopicExchange testExchangeDlx() {
        return ExchangeBuilder.fanoutExchange("exchange_dlx").durable(true).build();
    }

    // 死信交换机绑定死信队列
    @Bean
    public Binding bindingDeathQueue(){

        return BindingBuilder.bind(queueDlx()).to(testExchangeDlx()).with("dlx");
    }
}
