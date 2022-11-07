package com.fairy.rabbitmq;

/**
 * @author 鹿少年
 * @date 2022/11/7 22:42
 */
public class RabbitConstant {
    public static final String QUEUE_Simple = "simple-queue";
    public static final String QUEUE_SMS = "sms";
    public static final String EXCHANGE_WEATHER = "weather";
    public static final String EXCHANGE_WEATHER_ROUTING = "weather_routing";
    public static final String QUEUE_BAIDU = "baidu";
    public static final String QUEUE_SINA = "sina";
    public static final String EXCHANGE_WEATHER_TOPIC = "weather_topic";


    public static final String host = "node01";
    public static final String userName = "admin";
    public static final String password = "admin";
    public static final String virture = "/";
    public static final int port = 5672;
}
