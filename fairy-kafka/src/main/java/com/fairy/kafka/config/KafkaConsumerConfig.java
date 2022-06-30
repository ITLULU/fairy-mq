//package com.fairy.kafka.config;
//
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.config.KafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
//import org.springframework.kafka.listener.ConsumerProperties;
//import org.springframework.kafka.listener.ContainerProperties;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author 鹿少年
// * @version 1.0
// * @date 2022/5/29 17:37
// */
//@Configuration
//@EnableConfigurationProperties(ConsumerProperties.class)
//public class KafkaConsumerConfig {
//
//    @Autowired
//    private ConsumerProperties consumerProperties;
//
//    public Map<String, Object> consumerConfigs() {
//        Map props = new HashMap<>();
//        //消费者组
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerProperties.getGroupId());
//        //设置是否自动维护offset 默认为true
//        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
//        //自动提交的频率 单位 ms 默认值5000
////        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000);
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerProperties.getBootstrapServers());
//        //session超时，超过这个时间consumer没有发送心跳,就会触发rebalance操作 默认45000
//        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 120000);
//        //请求超时 默认值30000
//        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 120000);
//
//        //当kafka中没有初始offset或offset超出范围时将自动重置offset
//        //earliest:重置为分区中最小的offset
//        //latest:重置为分区中最新的offset(消费分区中新产生的数据)
//        //none:只要有一个分区不存在已提交的offset,就抛出异常
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        //批量消费时间间隔  默认值 300000
//        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, consumerProperties.getMaxPollInterval());
//        //批量消费最大数量 默认值500
//        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, consumerProperties.getMaxPollRecords());
//
//        //Key 反序列化类
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//        //Value 反序列化类
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//        //设置Consumer拦截器
////        props.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, MyConsumerInterceptor.class.getName());
//
//        return props;
//    }
//
//
//    @Bean
//    @ConditionalOnMissingBean(name = "consumerFactory")
//    public ConsumerFactory consumerFactory() {
//        DefaultKafkaConsumerFactory factory = new DefaultKafkaConsumerFactory(consumerConfigs());
//        return factory;
//    }
//
//
//    @Bean
//    @ConditionalOnMissingBean(name = "kafkaListenerContainerFactory")
//    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        //设置并发量，小于或等于Topic的分区数 设置消费者组中的线程数量
//        factory.setConcurrency(consumerProperties.getConcurrency());
//        //必须 设置为批量监听
//        factory.setBatchListener(true);
//        //消费者监听器自启
//        factory.setAutoStartup(true);
//        //消费一次提交一次  MANUAL 表示批量提交一次
//        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
//        return factory;
//    }
//
//
//    /**
//     * 自定义topic
//     */
// /*   @Bean("fairy-topic")
//    public NewTopic topic() {
//        return new NewTopic("fairy-topic", 4, (short) 3);
//    }*/
//
//}
