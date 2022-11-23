package com.fairy.rabbitmq.quoram;

import com.fairy.rabbitmq.RabbitConstant;
import com.fairy.rabbitmq.RabbitmqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 鹿少年
 * @date 2022/11/23 22:26
 */
public class QuoramSender {

    public static void main(String[] args) throws IOException {
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
        Map<String, Object> params = new HashMap<>();
        params.put("x-queue-type", "quorum");
        channel.exchangeDeclare(RabbitConstant.QUEUE_Simple, BuiltinExchangeType.DIRECT, true, false, false, params);

        //stream 队列
//        Map<String, Object> params = new HashMap<>();
//        params.put("x-queue-type", "stream");
//        params.put("x-max-length-bytes", 20_000_000_000L); // maximum stream size: 20 GB
//        params.put("x-stream-max-segment-size-bytes", 100_000_000); // size of segment files: 100 MB

        String message = "rabbitmq simple简单模式 hello world 队列不需要绑定交换机 也不需要交换机";
        //四个参数
        //exchange 交换机，暂时用不到，在后面进行发布订阅时才会用到
        //队列名称
        //额外的设置属性
        //最后一个参数是要传递的消息字节数组
        channel.basicPublish("", RabbitConstant.QUEUE_Simple, null, message.getBytes());

        channel.close();
        conn.close();
        System.out.println("===发送成功===");
    }

}
