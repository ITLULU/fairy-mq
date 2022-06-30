package com.fairy.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 鹿少年
 * @version 1.0
 * @date 2022/5/29 15:50
 */
@Slf4j
@Component
public class KafkaListner {

    /**
     * 配置多个消费组
     *
     * @param records
     * @param ack
     */
    @KafkaListener(groupId = "${kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory", topicPartitions = {@TopicPartition(topic = "${kafka.consumer.topic}", partitions = {"0", "1"})})
    public void fairyGroupTopic(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        log.info("消费监听本次拉取数据量：{}",records.size());
        for (ConsumerRecord<String, String> record : records) {
            String value = record.value();
            log.info("消费者组fairyGroupTopic消费 topic 分区0,1数据：{},topic:{},partition:{},offset:{}", value,record.topic(),record.partition(),record.offset());

        }
        ack.acknowledge();
    }


    @KafkaListener(groupId = "${kafka.consumer.group-id2}", containerFactory = "kafkaListenerContainerFactory", topicPartitions = {@TopicPartition(topic = "${kafka.consumer.topic}", partitions = {"2", "3","4"})})
    public void fairyGroupTopic2(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        log.info("消费监听本次拉取数据量：{}",records.size());
        for (ConsumerRecord<String, String> record : records) {
            String value = record.value();
            log.info("消费者组fairyGroupTopic2消费 topic 分区2,3,4数据：{},topic:{},partition:{},offset:{}", value,record.topic(),record.partition(),record.offset());
        }
        ack.acknowledge();

    }
}
