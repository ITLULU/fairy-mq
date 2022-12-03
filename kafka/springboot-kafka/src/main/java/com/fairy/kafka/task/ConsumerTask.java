package com.fairy.kafka.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author hll
 * @version 1.0
 * @date 2022/7/1 15:45
 */
@Slf4j
//@Component
public class ConsumerTask {
    @Autowired
    private KafkaListenerEndpointRegistry registry;


    /**
     * 如果不设置自动启动监听可以通过 定时任务启动
     */
    @Scheduled(cron = "2/10 * * * * ?")
    public void startListener() {
        log.info("开启监听");
        MessageListenerContainer container = registry.getListenerContainer("group-listener");
     /*   if (!container.isRunning()) {
            container.start();
        }*/
        //恢复
          container.resume();
    }

    //    @Scheduled(cron = "0 08 12 * * ?")
    public void shutdownListener() {
        log.info("关闭监听");
        //暂停
        MessageListenerContainer container = registry.getListenerContainer("group-listener");
        container.stop();
    }
}
