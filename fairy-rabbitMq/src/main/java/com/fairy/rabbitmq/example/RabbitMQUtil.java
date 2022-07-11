package com.fairy.rabbitmq.example;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQUtil {

	private static Connection connection;
	private static final String HOST_NAME="localhost";
//	private static final String HOST_NAME="worker2";
	private static final int HOST_PORT=5672;

	public static final String QUEUE_HELLO="hello";
	public static final String QUEUE_WORK="work";
	public static final String QUEUE_PUBLISH="publish";
	
	private RabbitMQUtil() {}
	
	public static Connection getConnection() throws Exception {
		if(null == connection) {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(HOST_NAME);
			factory.setPort(HOST_PORT);
			factory.setUsername("admin");
			factory.setPassword("admin");
			factory.setVirtualHost("/mirror");
			connection = factory.newConnection();
		}
		return connection;
	}
}
