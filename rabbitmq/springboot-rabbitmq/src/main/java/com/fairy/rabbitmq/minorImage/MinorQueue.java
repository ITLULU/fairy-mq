package com.fairy.rabbitmq.minorImage;

import com.fairy.rabbitmq.RabbitConstant;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MinorQueue {

    @Bean
    public Queue minorQueue() {
        Map<String,Object>args = new HashMap<>(2);
        //1,全部节点进行镜像队列
//        args.put("x-ha-policy","all");
        //2、指定节点进行镜像队列：
        args.put("x-ha-policy","nodes");
        args.put("x-ha-node","[rabbit@node01]");
        return new Queue(RabbitConstant.MINOR_QUEUE,true,false,false,args);
    }
}
