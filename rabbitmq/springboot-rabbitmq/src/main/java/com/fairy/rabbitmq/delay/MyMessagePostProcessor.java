package com.fairy.rabbitmq.delay;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;

/**
 * 因为要给消息设置 TTL，这里创建了一个 MessagePostProcessor 的实例来设置过期时间
 */
class MyMessagePostProcessor implements MessagePostProcessor {
    // 延迟时间 毫秒
    private String delayTime;

    MyMessagePostProcessor(String delayTime) {
        this.delayTime = delayTime;
    }

    /**
     * 在发送消息前可以修改消息的一些属性参数信息
     * @param message the message.
     * @return
     * @throws AmqpException
     */
    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        // 设置延迟时间
        message.getMessageProperties().setExpiration(delayTime);
        return message;
    }
}
