package com.fairy.rocketmq.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 鹿少年
 * @date 2022/7/12 16:05
 */
@Data
@TableName("op_rocketmq_log")
public class RocketMqLog implements Serializable {

    private Integer id;
    private String topic;
    private String destination;
    private String transactionId;
}
