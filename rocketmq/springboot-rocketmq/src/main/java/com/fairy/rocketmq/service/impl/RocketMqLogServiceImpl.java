package com.fairy.rocketmq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fairy.rocketmq.domain.RocketMqLog;
import com.fairy.rocketmq.mapper.RocketMqLogMapper;
import com.fairy.rocketmq.service.RocketMqLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 鹿少年
 * @date 2022/7/12 16:08
 */
@Slf4j
@Service
public class RocketMqLogServiceImpl implements RocketMqLogService {

    @Autowired
    private RocketMqLogMapper rocketMqLogMapper;

    @Transactional
    @Override
    public void inserMqLog( String transId, String topic, String destination) {
        rocketMqLogMapper.inserMqLog(transId,topic,destination);
    }

    @Override
    public RocketMqLog selectOne(String transactionId) {
        QueryWrapper<RocketMqLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("transaction_id",transactionId);
        return rocketMqLogMapper.selectOne(queryWrapper);
    }
}
