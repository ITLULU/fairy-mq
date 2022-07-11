package com.fairy.rabbitmq.producer;

import com.fairy.rabbitmq.util.MyConstants;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.UUID;


@RestController
public class ProducerController {

	@Autowired
	private RabbitTemplate rabbitTemplate;
	
//	@Autowired
//	private AmqpTemplate amqpTemplate;

	/**
	 * direct发送接口
	 * @param message
	 * @return
	 * @throws AmqpException
	 * @throws UnsupportedEncodingException
	 */
	@GetMapping(value="/directSend")
	public Object directSend(String message) throws AmqpException, UnsupportedEncodingException {
		//设置部分请求参数
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
		messageProperties.setPriority(2);
		//设置消息转换器，如json
		rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
		//将对象转换成json再发送。
//		rabbitTemplate.convertandsend("",Object);
		//发消息
		rabbitTemplate.send("directqueue",new Message(message.getBytes("UTF-8"),messageProperties));
		return "message sended : "+message;
	}

	/**
	 * fanout发送接口
	 * @param message
	 * @return
	 * @throws AmqpException
	 * @throws UnsupportedEncodingException
	 */
	@GetMapping(value="/fanoutSend")
	public Object fanoutSend(String message) throws AmqpException, UnsupportedEncodingException {
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
		//fanout模式只往exchange里发送消息。分发到exchange下的所有queue
		rabbitTemplate.send(MyConstants.EXCHANGE_FANOUT, "", new Message(message.getBytes("UTF-8"),messageProperties));

		Message message2 = MessageBuilder.withBody(message.getBytes()).setMessageId(UUID.randomUUID().toString()).build();
		rabbitTemplate.send(message2);
		return "message sended : "+message;
	}

	/**
	 * topic发送接口
	 * @param routingKey
	 * @param message
	 * @return
	 * @throws AmqpException
	 * @throws UnsupportedEncodingException
	 */
	@GetMapping(value="/topicSendHunanIT")
	public Object topicSend(String routingKey,String message) throws AmqpException, UnsupportedEncodingException {
		if(null == routingKey) {
			routingKey="hebei.IT";
		}
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
		//fanout模式只往exchange里发送消息。分发到exchange下的所有queue
		rabbitTemplate.send("topicExchange", routingKey, new Message(message.getBytes("UTF-8"),messageProperties));
		return "message sended : routingKey >"+routingKey+";message > "+message;
	}

	/**
	 * header发送接口
	 * @param txTyp
	 * @param busTyp
	 * @param message
	 * @return
	 * @throws AmqpException
	 * @throws UnsupportedEncodingException
	 */
	@GetMapping(value="/headerSend")
	public Object headerSend(String txTyp,String busTyp,String message) throws AmqpException, UnsupportedEncodingException {
		if(null == txTyp) {
			txTyp="0";
		}
		if(null == busTyp) {
			busTyp="0";
		}
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
		messageProperties.setHeader("txTyp", txTyp);
		messageProperties.setHeader("busTyp", busTyp);
		//fanout模式只往exchange里发送消息。分发到exchange下的所有queue
		rabbitTemplate.send("headerExchange", "uselessRoutingKey", new Message(message.getBytes("UTF-8"),messageProperties));
		return "message sended : txTyp >"+txTyp+";busTyp > "+busTyp;
	}

	/**
	 * quorum队列发送接口
	 * @param message
	 * @return
	 * @throws AmqpException
	 * @throws UnsupportedEncodingException
	 */
	@GetMapping(value="/directQuorum")
	public Object directQuorum(String message) throws AmqpException, UnsupportedEncodingException {
		//设置部分请求参数
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
		messageProperties.setPriority(2);
		//设置消息转换器，如json
		rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
		//将对象转换成json再发送。
//		rabbitTemplate.convertandsend("",Object);
		//发消息
		rabbitTemplate.send(MyConstants.QUEUE_QUORUM,new Message(message.getBytes("UTF-8"),messageProperties));
		return "message sended : "+message;
	}

//	@ApiOperation(value="stream队列发送接口",notes="直接发送到队列。stream队列")
//	@GetMapping(value="/directStream")
	public Object directStream(String message) throws AmqpException, UnsupportedEncodingException {
		//设置部分请求参数
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
		messageProperties.setPriority(2);
		//设置消息转换器，如json
		rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
		//将对象转换成json再发送。
//		rabbitTemplate.convertandsend("",Object);
		//发消息
		rabbitTemplate.send(MyConstants.QUEUE_STREAM,new Message(message.getBytes("UTF-8"),messageProperties));
		return "message sended : "+message;
	}
}
