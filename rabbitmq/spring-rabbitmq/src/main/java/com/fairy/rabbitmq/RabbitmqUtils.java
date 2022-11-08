package com.fairy.rabbitmq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


/**
 * @author 鹿少年
 * @date 2022/11/7 22:41
 */
public class RabbitmqUtils {

    private static ConnectionFactory connectionFactory = new ConnectionFactory();
    static {
        connectionFactory.setHost(RabbitConstant.host);
        connectionFactory.setPort(RabbitConstant.port);//5672是RabbitMQ的默认端口号
        connectionFactory.setUsername(RabbitConstant.userName);
        connectionFactory.setPassword(RabbitConstant.password);
        connectionFactory.setVirtualHost(RabbitConstant.virture);
    }
    public static Connection getConnection(){
        Connection conn = null;
        try {
            conn = connectionFactory.newConnection();
            return conn;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
