package com.fairy.rabbitmq.facotory;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 鹿少年
 * @date 2022/11/10 22:46
 */
@Configuration
public class MyFactoryConfig {

    @Bean(name="myListenerFactory")
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setMaxConcurrentConsumers(4);
        //一次拉去数量
        factory.setPrefetchCount(2);
        factory.setConnectionFactory(connectionFactory);
        //配置手动确认
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }

    /**
     * 配置rabbitmqTemplate
     *
     * 推送mq有四种情况：
     *      消息推送到 MQ，但是在 MQ 里找不到交换机
     *      消息推送到 MQ，找到交换机了，当时没有找到队列
     *      消息推送到 MQ，交换机和队列都没找到
     *      消息成功推送
     *
     */
    @Bean
    @ConditionalOnBean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        // 设置消息从生产者发送至 rabbitmq broker 成功的回调 （保证信息到达 broker）
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            // ack=true:消息成功发送到Exchange
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("ConfirmCallback:     " + "相关数据：" + correlationData);
                System.out.println("ConfirmCallback:     " + "确认是否到达交换机：" + ack);
                System.out.println("ConfirmCallback:     " + "原因：" + cause);
            }
        });
        // 设置信息从交换机发送至 queue 失败的回调
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                System.out.println("ReturnCallback:     " + "消息：" + message);
                System.out.println("ReturnCallback:     " + "回应码：" + replyCode);
                System.out.println("ReturnCallback:     " + "回应信息：" + replyText);
                System.out.println("ReturnCallback:     " + "交换机：" + exchange);
                System.out.println("ReturnCallback:     " + "路由键：" + routingKey);
            }
        });
        // 为 true 时，消息通过交换器无法匹配到队列时会返回给生产者，为 false 时，匹配不到会直接丢弃
        rabbitTemplate.setMandatory(true);
        // 设置发送时的转换
        // rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

}
