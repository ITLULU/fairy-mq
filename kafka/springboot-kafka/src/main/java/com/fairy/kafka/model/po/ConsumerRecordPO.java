package com.fairy.kafka.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 消费数据记录
 * @author hll
 * @version 1.0
 * @date 2022/7/1 14:01
 */
@Builder
@AllArgsConstructor
@TableName("op_consumer_record")
@Data
public class ConsumerRecordPO implements Serializable {
    /**主题*/
    private String topic;
    /**分区*/
    private Integer  topicPartition;
    /**偏移量*/
    private Long topicOffset;
    /**消费时间*/
    @JsonFormat(locale = "zh", timezone = "Asia/Shanghai", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date consumerDate;
    @JsonFormat(locale = "zh", timezone = "Asia/Shanghai", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendDate;
}
