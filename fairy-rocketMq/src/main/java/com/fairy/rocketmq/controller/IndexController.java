package com.fairy.rocketmq.controller;

import com.alibaba.fastjson.JSON;
import com.fairy.common.response.CommonResponse;
import com.fairy.rocketmq.domain.Order;
import com.fairy.rocketmq.service.MqSender;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hll
 * @version 1.0
 * @date 2022/7/6 14:30
 */
@Slf4j
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
        sender.sendMessage(TOPIC_NAME, JSON.toJSONString(order));
        return CommonResponse.success();

    }

    @GetMapping("/sendone")
    public CommonResponse sendone() {
        for (int i = 0; i < 2; i++) {
            Order order = new Order();
            order.setOrderId(100+i*10);
            //尝试在Header中加入一些自定义的属性。
            Message<String> message = MessageBuilder.withPayload(JSON.toJSONString(order))
                    .setHeader(RocketMQHeaders.TRANSACTION_ID, "TransID_" + i)
                    //发到事务监听器里后，这个自己设定的TAGS属性会丢失。但是上面那个属性不会丢失。
                    .setHeader(RocketMQHeaders.KEYS, order.getOrderId())
                    //MyProp在事务监听器里也能拿到，为什么就单单这个RocketMQHeaders.TAGS拿不到？这只能去调源码了。
                    .setHeader("MyProp", "MyProp_"+ order.getOrderId())
                    .build();
            String destination = TOPIC_NAME + ":" + order.getOrderId();
            //这里发送事务消息时，还是会转换成RocketMQ的Message对象，再调用RocketMQ的API完成事务消息机制。
            SendResult  sendResult  =rocketMQTemplate.syncSend(destination, message);
            log.info("事务消息发送结果:{}", sendResult);

        }
        return CommonResponse.success();

    }


    @GetMapping("/transaction")
    public CommonResponse sendMessageInTransaction() throws InterruptedException {
        for (int i = 0; i < 2; i++) {
            Order order = new Order();
            order.setOrderId(i);
            //尝试在Header中加入一些自定义的属性。
            Message<String> message = MessageBuilder.withPayload(JSON.toJSONString(order))
                    .setHeader(RocketMQHeaders.TRANSACTION_ID, "TransID_" + i)
                    //发到事务监听器里后，这个自己设定的TAGS属性会丢失。但是上面那个属性不会丢失。
                    .setHeader("orderId", order.getOrderId())
                    .setHeader(RocketMQHeaders.TOPIC, TOPIC_NAME)
                    //MyProp在事务监听器里也能拿到，为什么就单单这个RocketMQHeaders.TAGS拿不到？这只能去调源码了。
                    .setHeader("MyProp", "MyProp_"+ order.getOrderId())
                    .build();
            String destination = TOPIC_NAME + ":" + order.getOrderId();
            //这里发送事务消息时，还是会转换成RocketMQ的Message对象，再调用RocketMQ的API完成事务消息机制。
            TransactionSendResult sendResult = rocketMQTemplate.sendMessageInTransaction(destination, message, destination);
            log.info("事务消息发送结果:{}", sendResult.getLocalTransactionState());
        }
        return CommonResponse.success();
    }

}
