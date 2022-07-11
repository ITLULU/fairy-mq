package com.fairy.rabbitmq.example.direct;

import com.fairy.rabbitmq.example.RabbitMQUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

import java.util.HashMap;
import java.util.Map;

public class Sender {

	private static final String QUEUE_NAME = "hello";

	public static void main(String[] args) throws Exception {
		Connection connection = RabbitMQUtil.getConnection();
		Channel channel = connection.createChannel();
		//声明队列会在服务端自动创建。
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		//声明Quorum队列的方式就是添加一个x-queue-type参数，指定为quorum。默认是classic
//		Map<String,Object> params = new HashMap<>();
//		params.put("x-queue-type","quorum");
//		channel.queueDeclare(QUEUE_NAME, true, false, false, params);
		//声明Stream队列的方式。
//		params.put("x-queue-type","stream");
//		params.put("x-max-length-bytes", 20_000_000_000L); // maximum stream size: 20 GB
//		params.put("x-stream-max-segment-size-bytes", 100_000_000); // size of segment files: 100 MB
//		channel.queueDeclare(QUEUE_NAME, true, false, false, params);

		String message = "Hello World!333";

		AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
		builder.deliveryMode(MessageProperties.PERSISTENT_TEXT_PLAIN.getDeliveryMode());
		builder.priority(MessageProperties.PERSISTENT_TEXT_PLAIN.getPriority());
		//携带消息ID
		builder.messageId(""+channel.getNextPublishSeqNo());
		Map<String, Object> headers = new HashMap<>();
		//携带订单号
		headers.put("order", "123");
		builder.headers(headers);

		channel.basicPublish("", QUEUE_NAME, builder.build(), message.getBytes("UTF-8"));
		System.out.println(" [x] Sent '" + message + "'");
		
		channel.close();
		connection.close();
	}
}
