package com.fairy.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * 消息确认监听 消费确认
 */
@Component
public class AckListener implements ChannelAwareMessageListener {

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        // 接受消息的标签
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            //1.接收转换消息
            System.out.println(new String(message.getBody()));

            //2. 处理业务逻辑
            System.out.println("处理业务逻辑...");

            int i = 3 / 0;//出现错误

            //3. 手动签收
            /*
                参数1：接受消息的标签
                参数2：允许多条消息同时被处理
             */
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            //e.printStackTrace();

            //4.拒绝签收

            /*
                参数1：接受消息的标签
                参数2：允许多条消息同时被处理
                参数3：重回队列,如果设置为true，则消息重新回到queue，broker会重新发送该消息给消费端
             */
            channel.basicNack(deliveryTag, true, true);

            // 也可以拒绝签收 参数二: 是否重回队列
//            channel.basicReject(deliveryTag, true);
        }
    }
}
