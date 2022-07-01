package com.fairy.kafka.listenner;

import com.fairy.kafka.handler.RecordHandler;
import com.fairy.kafka.model.po.ConsumerRecordPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RecordHandler recordHandler;

    /**
     * 配置多个消费组
     * TopicPartition  定义分区 partitionOffsets
     * concurrency就是同组下的消费者个数，就是并发消费数，必须小于等于分区总数
     *
     * @param records
     * @param ack
     */
    @KafkaListener(groupId = "${kafka.consumer.group-id}", containerFactory = "manualIMListenerContainerFactory", topicPartitions = {@TopicPartition(topic = "${kafka.consumer.topic}", partitions = {"0"})})
    public void fairyGroupTopic(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        log.info("消费监听本次拉取数据量：{}", records.size());
        for (ConsumerRecord<String, String> record : records) {
            String value = record.value();
            log.info("消费者组fairyGroupTopic消费 topic 分区0,1数据：{},topic:{},partition:{},offset:{}", value, record.topic(), record.partition(), record.offset());
        }
        //实际开发中  消费数据这一步需要做幂等性  防止多次消费
        doFilter(records);
        //手动提交ack 移动偏移量
        ack.acknowledge();
    }

    /**
     * 对数据进行幂等校验 如果数据时没有消费的数据 可以将数据存储记录 以topic-partition-offset做为唯一索引来判断数据是否是已经消费的数据
     *
     * @param records
     */
    private void doFilter(List<ConsumerRecord<String, String>> records) {
        //1:查看消费记录数据库 校验数据是否是已经消费的数据
        for (ConsumerRecord<String, String> record : records) {
            ConsumerRecordPO recordPO = recordHandler.searchRecordFromDB(record);
            if (recordPO == null) {
                //新的数据 记录消费
                //同时将数据写入本地表  以后消费数据直接从本地表进行数据消费处理  对于处理时长比较长的数据 可以这样处理
                recordHandler.saveConsumerRecord(record);

            }

        }
    }


    @KafkaListener(groupId = "${kafka.consumer.group-id2}", containerFactory = "manualListenerContainerFactory", topicPartitions = {@TopicPartition(topic = "${kafka.consumer.topic}", partitions = {"0", "1"})})
    public void fairyGroupTopic2(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        log.info("消费监听本次拉取数据量：{}", records.size());
        for (ConsumerRecord<String, String> record : records) {
            String value = record.value();
            log.info("消费者组fairyGroupTopic2消费 topic 分区2,3,4数据：{},topic:{},partition:{},offset:{}", value, record.topic(), record.partition(), record.offset());
        }
        ack.acknowledge();
    }

    /**
     * 一次拉取一条数据
     *
     * @param record
     */
    @KafkaListener(groupId = "${kafka.consumer.group-id3}", containerFactory = "recordListenerContainerFactory", topicPartitions = {@TopicPartition(topic = "${kafka.consumer.topic}", partitions = {"0", "1"})})
    public void fairyGroupTopic3(ConsumerRecord<String, String> record) {
        String value = record.value();
        log.info("消费者组fairyGroupTopic3消费 topic 分区2,3,4数据：{},topic:{},partition:{},offset:{}", value, record.topic(), record.partition(), record.offset());
    }

    @KafkaListener(groupId = "${kafka.consumer.group-id4}", containerFactory = "timeListenerContainerFactory", topicPartitions = {@TopicPartition(topic = "${kafka.consumer.topic}", partitions = {"0", "1"})})
    public void fairyGroupTopic4(List<ConsumerRecord<String, String>> records) {
        log.info("消费监听本次拉取数据量：{}", records.size());
        for (ConsumerRecord<String, String> record : records) {
            String value = record.value();
            log.info("消费者组fairyGroupTopic4消费 topic 分区2,3,4数据：{},topic:{},partition:{},offset:{}", value, record.topic(), record.partition(), record.offset());
        }
    }


    @KafkaListener(groupId = "${kafka.consumer.group-id5}", containerFactory = "countListenerContainerFactory", topicPartitions = {@TopicPartition(topic = "${kafka.consumer.topic}", partitions = {"0", "1"})})
    public void fairyGroupTopic5(List<ConsumerRecord<String, String>> records) {
        log.info("消费监听本次拉取数据量：{}", records.size());
        for (ConsumerRecord<String, String> record : records) {
            String value = record.value();
            log.info("消费者组fairyGroupTopic5消费 topic 分区2,3,4数据：{},topic:{},partition:{},offset:{}", value, record.topic(), record.partition(), record.offset());
        }
    }


    @KafkaListener(groupId = "${kafka.consumer.group-id6}", containerFactory = "timeCountListenerContainerFactory", topicPartitions = {@TopicPartition(topic = "${kafka.consumer.topic}", partitions = {"0", "1"})})
    public void fairyGroupTopic6(List<ConsumerRecord<String, String>> records) {
        log.info("消费监听本次拉取数据量：{}", records.size());
        for (ConsumerRecord<String, String> record : records) {
            String value = record.value();
            log.info("消费者组fairyGroupTopic6消费 topic 分区2,3,4数据：{},topic:{},partition:{},offset:{}", value, record.topic(), record.partition(), record.offset());
        }
    }
}
