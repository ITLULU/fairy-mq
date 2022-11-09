package com.fairy.rabbitmq.simple;

import com.fairy.rabbitmq.RabbitConstant;
import com.fairy.rabbitmq.RabbitmqUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author 鹿少年
 * @date 2022/11/7 22:40
 */
public class SimpleConsumer2 {

    public static void main(String[] args) throws IOException, TimeoutException {

        //获取TCP长连接
        Connection conn = RabbitmqUtils.getConnection();
        //创建通信“通道”，相当于TCP中的虚拟连接
        Channel channel = conn.createChannel();

        //创建队列,声明并创建一个队列，如果队列已存在，则使用这个队列
        //第一个参数：队列名称ID
        //第二个参数：是否持久化，false对应不持久化数据，MQ停掉数据就会丢失
        //第三个参数：是否队列私有化，false则代表所有消费者都可以访问，true代表只有第一次拥有它的消费者才能一直使用，其他消费者不让访问
        //第四个：是否自动删除,false代表连接停掉后不自动删除掉这个队列
        //其他额外的参数, null
        channel.exchangeDeclare(RabbitConstant.QUEUE_Simple,BuiltinExchangeType.DIRECT,true, false, false, null);

        //从MQ服务器中获取数据

        //创建一个消息消费者
        //第一个参数：队列名
        //第二个参数代表是否自动确认收到消息，false代表手动编程来确认消息，这是MQ的推荐做法
        //第三个参数要传入DefaultConsumer的实现类
        channel.basicConsume(RabbitConstant.QUEUE_Simple, false, new Consumer() {
            @Override
            public void handleConsumeOk(String consumerTag) {
                System.out.println("消息被消费"+consumerTag);
            }

            @Override
            public void handleCancelOk(String consumerTag) {
                System.out.println("消息被取消"+consumerTag);
            }

            @Override
            public void handleCancel(String consumerTag) throws IOException {
                System.out.println("当消费者因调用 Channel.basicCancel 以外的原因而被取消时调用。" +
                        "例如，队列已被删除。有关 Channel.basicCancel 导致的消费者取消通知，请参见 handleCancelOk."+consumerTag);
            }

            @Override
            public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
                System.out.println("当通道或基础连接已关闭时调用。"+consumerTag);
            }

            @Override
            public void handleRecoverOk(String consumerTag) {
                System.out.println("数据消费成功"+consumerTag);
            }

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body);
                System.out.println(String.format("消费者接收到的消息：%s, 交互机:%s, routingkey:%s ,消息的TagId：%s, " +
                                "consumerTags:%s",message,
                        envelope.getExchange(),
                        envelope.getRoutingKey(),envelope.getDeliveryTag(),consumerTag));

                //false只确认签收当前的消息，设置为true的时候则代表签收该消费者所有未签收的消息
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });


    }
}


