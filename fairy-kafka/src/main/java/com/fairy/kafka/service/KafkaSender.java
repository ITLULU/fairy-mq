package com.fairy.kafka.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * @author 鹿少年
 * @version 1.0
 * @date 2022/5/29 21:16
 */
@Slf4j
@Service
public class KafkaSender {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    @Qualifier("transKafkaTemplate")
    private KafkaTemplate transKafkaTemplate;

    public void synSendMessage(Message msg) {
        try {
            kafkaTemplate.send(msg).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void asynSendMessage(Message msg) {
        try {
            ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(msg);
            future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
                @Override
                public void onFailure(Throwable ex) {
                    log.error("处理失败");
                }

                @Override
                public void onSuccess(SendResult<String, String> result) {
                    log.info("onSuccess");

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doTransactionSend(Message record) {
        Object result = transKafkaTemplate.executeInTransaction(new KafkaOperations.OperationsCallback<String, String, Object>() {
            @Override
            public Object doInOperations(KafkaOperations<String, String> operations) {
                operations.send(record);
                int i =1/0;
                return true;
            }
        });
        log.info("---result:{}----", result);
    }

    @Transactional
    public void doTransactionSend2(Message record) {
        transKafkaTemplate.send(record);
    }
    @Transactional
    public void doTransactionSend3(String topic, Integer partition , String key,Object data) {
        transKafkaTemplate.send(topic,partition,System.currentTimeMillis(),key,data);
    }
}
