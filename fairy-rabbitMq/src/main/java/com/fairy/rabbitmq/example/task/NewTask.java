package com.fairy.rabbitmq.example.task;

import com.fairy.rabbitmq.example.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;


public class NewTask {

	/**
	 * 发布一个task，交由多个Worker去处理。 每个task只要由一个Worker完成就行。
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Connection connection = RabbitMQUtil.getConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(RabbitMQUtil.QUEUE_WORK, true, false, false, null);
		String message = "task 1";
		channel.basicPublish("", RabbitMQUtil.QUEUE_WORK,
				MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());

		channel.close();
		connection.close();
	}
}
