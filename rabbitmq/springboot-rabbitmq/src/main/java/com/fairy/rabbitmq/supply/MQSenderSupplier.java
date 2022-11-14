package com.fairy.rabbitmq.supply;

import com.fairy.rabbitmq.RabbitConstant;
import com.fairy.rabbitmq.facotory.ApplicationUtils;
import com.rabbitmq.client.AMQP;
import com.sun.deploy.association.utility.AppConstants;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * @author 鹿少年
 * @date 2022/11/14 21:53
 */
public class MQSenderSupplier implements Supplier<String> {

    private String message;

    public MQSenderSupplier(String message) {
        this.message = message;
    }

    @Override
    public String get() {
        Date sendTime = new Date();
        String correlationId = UUID.randomUUID().toString();

        MessagePropertiesConverter messagePropertiesConverter =
                (MessagePropertiesConverter) ApplicationUtils.getBean("messagePropertiesConverter");

        RabbitTemplate rabbitTemplate =
                (RabbitTemplate)ApplicationUtils.getBean("rabbitTemplate");

        AMQP.BasicProperties props =
                new AMQP.BasicProperties("text/plain",
                        "UTF-8",
                        null,
                        2,
                        0, correlationId,
                         System.currentTimeMillis()+"",
                        null,
                        null, sendTime, null, null,
                        "SpringProducer", null);

        MessageProperties sendMessageProperties =
                messagePropertiesConverter.toMessageProperties(props, null, "UTF-8");
        sendMessageProperties.setReceivedExchange(RabbitConstant.EXCHANGE_PUBSUB_Direct);
        sendMessageProperties.setReceivedRoutingKey("china.#");
        sendMessageProperties.setRedelivered(true);

        Message sendMessage = MessageBuilder.withBody(message.getBytes())
                .andProperties(sendMessageProperties)
                .build();

        Message replyMessage =
                rabbitTemplate.sendAndReceive(RabbitConstant.EXCHANGE_PUBSUB_Direct,
                       "china.#", sendMessage);

        String replyMessageContent = null;
        try {
            replyMessageContent = new String(replyMessage.getBody(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return replyMessageContent;
    }
}