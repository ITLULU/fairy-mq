package com.fairy.rocketmq.service;

import com.fairy.rocketmq.domain.RocketMqLog;

/**
 * @author 鹿少年
 * @date 2022/7/12 16:07
 */
public interface RocketMqLogService {

    void inserMqLog( String transId, String topic, String destination);

    RocketMqLog selectOne(String transactionId);
}
