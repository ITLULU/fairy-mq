package com.fairy.rocketmq.service;

/**
 * @author 鹿少年
 * @version 1.0
 * @date 2022/7/11 21:48
 */
public interface MqSender {
    /**
     * 直接发送消息
     *
     * @param topic_name
     * @param toJSONString
     */
    void sendMessage(String topic_name, String toJSONString);

    /**
     * 顺序发送
     *
     * @param topic
     * @param tag
     * @param msg
     */
    void syncSendOrderly(String topic, String tag, String msg);

    /**
     * 异步发送
     *
     * @param topic
     * @param tag
     * @param msg
     */
    void sendAsynMessag(String topic, String tag, String msg);

    /**
     * 发送事务消息
     *
     * @param topic
     * @param msg
     */
    void sendMessageInTransaction(String topic, String msg);

    /**
     * 直接发送
     *
     * @param topic
     * @param tag
     * @param msg
     */
    void send(String topic, String tag, String msg);
}
