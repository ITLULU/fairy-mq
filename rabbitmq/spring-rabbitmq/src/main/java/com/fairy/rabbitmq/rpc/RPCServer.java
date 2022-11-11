package com.fairy.rabbitmq.rpc;

import com.fairy.rabbitmq.RabbitConstant;
import com.fairy.rabbitmq.RabbitmqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

/**
 * @author 鹿少年
 * @date 2022/11/9 20:45
 */
public class RPCServer {

    private static int fib(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        return fib(n - 1) + fib(n - 2);
    }

    public static void main(String[] args) throws IOException {
        Connection connection = RabbitmqUtils.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(RabbitConstant.QUEUE_RPC, true, false, false, null);
        //Purges the contents of the given queue. 清除给定队列的内容
        channel.queuePurge(RabbitConstant.QUEUE_RPC);
        //一次只消费一条数据
        channel.basicQos(1);

        System.out.println(" [x] Awaiting RPC requests");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(delivery.getProperties().getCorrelationId())
                    .build();

            System.out.printf("deliverTag:"+delivery.getEnvelope().getDeliveryTag()+ " consumerTag:"+consumerTag+" delivery"+delivery);
            String response = "";
            try {
                String message = new String(delivery.getBody(), "UTF-8");
                int n = Integer.parseInt(message);

                System.out.println(" [.] fib(" + message + ")");
                response += fib(n);
                System.out.println("response:"+response);
            } catch (RuntimeException e) {
                System.out.println(" [.] " + e);
            } finally {
                channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, response.getBytes("UTF-8"));
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };

        channel.basicConsume(RabbitConstant.QUEUE_RPC, false, deliverCallback, (consumerTag -> {
            System.out.println("接受到消息"+consumerTag);
        }));
    }
}
