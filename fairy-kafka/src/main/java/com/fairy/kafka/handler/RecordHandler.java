package com.fairy.kafka.handler;

import com.fairy.kafka.model.po.ConsumerRecordPO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

/**
 * @author hll
 * @version 1.0
 * @date 2022/7/1 13:58
 */
@Component
public class RecordHandler {
    /**
     * 检查数据库是否存在该条已经消费的记录  对应这些日志记录 可以设置定时任务每隔几天或者几小时取定时清除一下
     *
     * @param record
     */
    public ConsumerRecordPO searchRecordFromDB(ConsumerRecord<String, String> record) {
        //1:查看数据库 查看是否存在对应的消费记录
        return null;
    }

    /**
     * 记录消费信息 插入数据表
     * @param record
     */
    public void saveConsumerRecord(ConsumerRecord<String, String> record) {
    }
}
