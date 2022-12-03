package com.fairy.kafka.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fairy.kafka.mapper.ConsumerRecordMapper;
import com.fairy.kafka.model.po.ConsumerRecordPO;
import com.fairy.kafka.service.ConsumerRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hll
 * @version 1.0
 * @date 2022/7/6 15:30
 */
@Slf4j
@Service
public class ConsumerRecordServiceImpl extends ServiceImpl<ConsumerRecordMapper, ConsumerRecordPO> implements ConsumerRecordService {

    @Autowired
    private ConsumerRecordMapper consumerRecordMapper;


    @Override
    public ConsumerRecordPO getConsumerRecord(String topic, int partition, long offset) {
        return consumerRecordMapper.getConsumerRecord(topic,partition,offset);
    }
}
