package com.fairy.rabbitmq.listener;

import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 鹿少年
 * @date 2022/11/13 23:08
 */
@Component
public class ListenerMesageRegister {
    @Autowired
    private RabbitListenerEndpointRegistry registry;


    public void stopContainer(String containerId){
        //得到容器的对象
        MessageListenerContainer container = registry.getListenerContainer(containerId);
        //判断容器状态
        if(container.isRunning()){
            //开启容器
            container.stop();
            System.out.println("关闭容器");
        }

    }

    public void startContainer(String containerId){
        //得到容器的对象
        MessageListenerContainer container = registry.getListenerContainer(containerId);
        //判断容器状态
        if(!container.isRunning()){
            //开启容器
            container.start();
            System.out.println("开启容器");
        }

    }
}
