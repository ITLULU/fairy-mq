package com.fairy.rocketmq.controller;

import com.alibaba.fastjson.JSON;
import com.fairy.common.response.CommonResponse;
import com.fairy.rocketmq.domain.Order;
import com.fairy.rocketmq.service.MqSender;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hll
 * @version 1.0
 * @date 2022/7/6 14:30
 */
@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private MqSender sender;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Value("${rocketmq.consumer.topic}")
    private String TOPIC_NAME;

    @GetMapping("/send")
    public CommonResponse sendMessage() {

        Order order = new Order();
        order.setOrderAmount(12.0);
        order.setOrderId(12);
        order.setProductNum(12);
        sender.sendMessage(TOPIC_NAME,JSON.toJSONString(order));
        return CommonResponse.success();

    }
}
