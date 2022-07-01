package com.fairy.kafka.controller;

import com.alibaba.fastjson.JSON;
import com.fairy.kafka.client.Order;
import com.fairy.kafka.service.KafkaSender;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author 鹿少年
 * @version 1.0
 * @date 2022/5/29 16:32
 */
@RestController
@RequestMapping("/mq")
public class MqSenderController {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("${kafka.consumer.topic}")
    private String TOPIC_NAME;

    @Autowired
    private KafkaSender kafkaSender;

    @GetMapping("/send")
    public String sender() throws InterruptedException {
        int msgNum = 6;
        final CountDownLatch countDownLatch = new CountDownLatch(msgNum);
        for (int i = 1; i <= msgNum; i++) {
            Order order = new Order(i, 100 + i, 1, 1000.00);
            ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(TOPIC_NAME
                    , order.getOrderId().toString(), JSON.toJSONString(order));
            kafkaTemplate.send(producerRecord);
            countDownLatch.countDown();
        }
        countDownLatch.await(5, TimeUnit.SECONDS);
        return "ok";
    }

    @GetMapping("/send/one")
    public String senderone() throws InterruptedException {
        Order order = new Order(22, 100, 1, 1000.00);
        Map<String, Object> headers = new HashMap<>();

        headers.put(KafkaHeaders.TOPIC, TOPIC_NAME);
        headers.put(KafkaHeaders.PARTITION_ID, 1);
        headers.put(KafkaHeaders.MESSAGE_KEY, "key-1111");
        headers.put(KafkaHeaders.RECEIVED_MESSAGE_KEY, "recived-key-1111");

        GenericMessage message = new GenericMessage(JSON.toJSONString(order), new MessageHeaders(headers));
        kafkaTemplate.send(message);
        return "ok";
    }

    @GetMapping("/send/trans1")
    public String sendetrans1() throws InterruptedException {
        Order order = new Order(22, 100, 1, 1000.00);
        Map<String, Object> headers = new HashMap<>();

        headers.put(KafkaHeaders.TOPIC, TOPIC_NAME);
        headers.put(KafkaHeaders.PARTITION_ID, 1);
        headers.put(KafkaHeaders.MESSAGE_KEY, "key-1111");
        headers.put(KafkaHeaders.RECEIVED_MESSAGE_KEY, "recived-key-1111");
        headers.put(KafkaHeaders.RECEIVED, "received");

        Message message = new GenericMessage(JSON.toJSONString(order), new MessageHeaders(headers));
        kafkaSender.doTransactionSend(message);
        return "ok";
    }

    @GetMapping("/send/trans2")
    public String sendetrans2() throws InterruptedException {
        Order order = new Order(22, 100, 1, 1000.00);
        Map<String, Object> headers = new HashMap<>();

        headers.put(KafkaHeaders.TOPIC, TOPIC_NAME);
        headers.put(KafkaHeaders.PARTITION_ID, 1);
        headers.put(KafkaHeaders.MESSAGE_KEY, "key-1111");
        headers.put(KafkaHeaders.RECEIVED_MESSAGE_KEY, "recived-key-1111");

        Message message = new GenericMessage(JSON.toJSONString(order), new MessageHeaders(headers));
        kafkaSender.doTransactionSend2(message);
        return "ok";
    }

    @GetMapping("/send/trans3")
    public String sendetrans3() throws InterruptedException {
        Order order = new Order(22, 100, 1, 1000.00);
        kafkaSender.doTransactionSend3(TOPIC_NAME,0,"key111",JSON.toJSONString(order));
        return "ok";
    }

    @GetMapping("/send/syn")
    public String sendSYN() throws InterruptedException {
        Order order = new Order(22, 100, 1, 1000.00);
        Map<String, Object> headers = new HashMap<>();

        headers.put(KafkaHeaders.TOPIC, TOPIC_NAME);
        headers.put(KafkaHeaders.MESSAGE_KEY, "key-1111");
        headers.put(KafkaHeaders.RECEIVED_MESSAGE_KEY, "recived-key-1111");

        Message message = new GenericMessage(JSON.toJSONString(order), new MessageHeaders(headers));
        kafkaSender.synSendMessage(message);
        return "ok";
    }

    @GetMapping("/send/asyn")
    public String sendAsy() throws InterruptedException {
        Order order = new Order(22, 100, 1, 1000.00);
        Map<String, Object> headers = new HashMap<>();

        headers.put(KafkaHeaders.TOPIC, TOPIC_NAME);
        headers.put(KafkaHeaders.MESSAGE_KEY, "key-1111");
        headers.put(KafkaHeaders.RECEIVED_MESSAGE_KEY, "recived-key-1111");

        Message message = new GenericMessage(JSON.toJSONString(order), new MessageHeaders(headers));
        kafkaSender.asynSendMessage(message);
        return "ok";
    }

    @GetMapping("/send/time")
    public String sendtime() throws InterruptedException {
        Order order = new Order(22, 100, 1, 1000.00);
        kafkaTemplate.send(TOPIC_NAME, 1, System.currentTimeMillis(), "key", JSON.toJSONString(order));
        return "ok";
    }

    @GetMapping("/send/time1")
    public String sendtime1() throws InterruptedException {
        Order order = new Order(22, 100, 1, 1000.00);
        kafkaTemplate.send(TOPIC_NAME, "key", JSON.toJSONString(order));
        return "ok";
    }
}
