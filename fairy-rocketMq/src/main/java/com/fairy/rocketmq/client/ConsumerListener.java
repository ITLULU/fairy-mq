package com.fairy.rocketmq.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * RocketMQListener接口：消费者都需实现该接口的消费方法onMessage(msg)。
 * RocketMQPushConsumerLifecycleListener接口：当@RocketMQMessageListener中的配置不足以满足我们的需求时，可以实现该接口直接更改消费者类DefaultMQPushConsumer配置
 * 监听器
 *
 * @author hll
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "${rocketmq.consumer.group2}", topic = "${rocketmq.consumer.topic}", consumeMode = ConsumeMode.CONCURRENTLY)
public class ConsumerListener implements RocketMQListener<String>, RocketMQPushConsumerLifecycleListener {
    @Override
    public void onMessage(String message) {
        log.info("Received message : " + message);
        //模拟异常场景
//        int i= 1/0;
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        // 每次拉取的间隔，单位为毫秒 间隔1秒拉取一次
        consumer.setPullInterval(1000);
        // 设置每次从队列中拉取的消息数为10
        consumer.setPullBatchSize(10);
        //消费超时 默认15秒
        consumer.setConsumeTimeout(TimeUnit.SECONDS.toSeconds(20));
        //拉取最新的
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
    }
}
