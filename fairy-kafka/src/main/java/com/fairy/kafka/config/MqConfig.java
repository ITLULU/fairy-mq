package com.fairy.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 鹿少年
 * @version 1.0
 * @date 2022/5/29 19:17
 */
@Slf4j
@Configuration
public class MqConfig {
    /**
     * 消费异常处理器
     *
     * @return
     */
    @Bean
    public ConsumerAwareListenerErrorHandler consumerAwareListenerErrorHandler() {
        return new ConsumerAwareListenerErrorHandler() {
            @Override
            public Object handleError(Message<?> message, ListenerExecutionFailedException exception, Consumer<?, ?> consumer) {
                //打印消费异常的消息和异常信息
                log.error("consumer failed! message: {}, exceptionMsg: {}, groupId: {}", message, exception.getMessage(), exception.getGroupId());
                return null;
            }
        };
    }

    @Value("${kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;
    /**
     * 创建一个kafka管理类，相当于rabbitMQ的管理类rabbitAdmin,没有此bean无法自定义的使用adminClient创建topic
     * @return
     */
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> props = new HashMap<>();
        //配置Kafka实例的连接地址
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        KafkaAdmin admin = new KafkaAdmin(props);
        return admin;
    }

    /**
     * kafka客户端，在spring中创建这个bean之后可以注入并且创建topic
     * @return
     */
    @Bean
    public AdminClient adminClient() {
        return AdminClient.create(kafkaAdmin().getConfig());
    }

}
