package com.fairy.rocketmq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fairy.rocketmq.domain.RocketMqLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 鹿少年
 * @date 2022/7/12 16:06
 */
@Mapper
public interface RocketMqLogMapper extends BaseMapper<RocketMqLog> {
    @Insert("insert into op_rocketmq_log(topic,transaction_id,destination) values (#{transId},#{topic},#{destination})")
    void inserMqLog(String transId, String topic, String destination);
}
