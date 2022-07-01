package com.fairy.kafka.model.po;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 消费数据记录
 * @author hll
 * @version 1.0
 * @date 2022/7/1 14:01
 */
@Data
public class ConsumerRecordPO implements Serializable {
    /**主题*/
    private String topic;
    /**分区*/
    private String  partition;
    /**偏移量*/
    private Long offset;
    /**消费时间*/
    private Date consumerDate;
}
