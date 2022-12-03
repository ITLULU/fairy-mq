package com.fairy.kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author 鹿少年
 * @date 2022/7/13 14:23
 */
@Configuration
@ConfigurationProperties(prefix = "kafka.mq")
@Data
public class TopicConfigurations {
    private List<ConsumerTopic> topics;


    @Data
    public static class ConsumerTopic {
        String name;
        Integer numPartitions;
        Short replicationFactor;
    }
}
