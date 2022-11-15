package com.fairy.rabbitmq.queue;

import com.fairy.rabbitmq.RabbitConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 鹿少年
 * @date 2022/11/10 22:34
 */
@Configuration
public class MqQueue {

    /**
     * 定义队列 helloworld
     *
     * @return
     */
    @Bean
    public Queue directQueue() {
        return new Queue(RabbitConstant.QUEUE_Simple);
    }

    /**
     * 工作队列
     */
    @Bean
    public Queue workQueue() {
        return new Queue(RabbitConstant.QUEUE_WorkQueue);
    }

    /**
     * 发布订阅
     */
    @Bean
    public Queue pubsubBeijingQueue() {
        return new Queue(RabbitConstant.QUEUE_Pubsu_Beijing);
    }


    @Bean
    public Queue pubsubShanghaiQueue() {
        return new Queue(RabbitConstant.QUEUE_Pubsu_Shanghai);
    }

    //声明exchange
    @Bean
    public FanoutExchange pubsubExchange() {
        return new FanoutExchange(RabbitConstant.EXCHANGE_PUBSUB_Fanout);
    }

    //绑定
    @Bean
    public Binding pubsubBeijingQueueExchangeBind() {
        return BindingBuilder.bind(pubsubBeijingQueue()).to(pubsubExchange());
    }

    @Bean
    public Binding pubsubShanghaiQueueExchangeBind() {
        return BindingBuilder.bind(pubsubShanghaiQueue()).to(pubsubExchange());
    }

    /**
     * header
     */
    //声明queue
    @Bean
    public Queue headQueueTxTyp1() {
        return new Queue(RabbitConstant.QUEUE_Header_TXTYP1);
    }

    @Bean
    public Queue headQueueBusTyp1() {
        return new Queue(RabbitConstant.QUEUE_Header_BUSTYP1);
    }

    @Bean
    public Queue headQueueTxBusTyp() {
        return new Queue(RabbitConstant.QUEUE_Header_TXBUSTYP1);
    }

    @Bean
    public HeadersExchange headerExchange() {
        return new HeadersExchange(RabbitConstant.EXCHANGE_HEADER);
    }

    //声明Binding
    //绑定header中txtyp=1的队列。header的队列匹配可以用mathces和exisits
    @Bean
    public Binding bindHeaderTxTyp1() {
        return BindingBuilder.bind(headQueueTxTyp1()).to(headerExchange()).where("txTyp").matches("1");
    }

    //绑定Header中busTyp=1的队列。
    @Bean
    public Binding bindHeaderBusTyp1() {
        return BindingBuilder.bind(headQueueBusTyp1()).to(headerExchange()).where("busTyp").matches("1");
    }

    //绑定Header中txtyp=1或者busTyp=1的队列。
    @Bean
    public Binding bindHeaderTxBusTyp1() {
        Map<String, Object> condMap = new HashMap<>();
        condMap.put("txTyp", "1");
        condMap.put("busTyp", "1");
//		return BindingBuilder.bind(headQueueTxBusTyp()).to(setHeaderExchange()).whereAny(new String[] {"txTyp","busTyp"}).exist();
        return BindingBuilder.bind(headQueueTxBusTyp()).to(headerExchange()).whereAny(condMap).match();
    }

    /**
     * 路由
     */
    @Bean
    public Queue routingBeijingQueue() {
        return new Queue(RabbitConstant.QUEUE_Routing_Beijing);
    }

    @Bean
    public Queue routingShanghaiQueue() {
        return new Queue(RabbitConstant.QUEUE_Routing_Shanghai);
    }

    //声明exchange
    @Bean
    public TopicExchange routingExchange() {
        return new TopicExchange(RabbitConstant.EXCHANGE_ROUTING_Topic);
    }

    //绑定
    @Bean
    public Binding rotingBeijingQueueExchangeBind() {
        return BindingBuilder.bind(routingBeijingQueue()).to(routingExchange()).with("china.hunan.changsha.20201127");
    }

    @Bean
    public Binding routingShanghaiQueueExchangeBind() {
        return BindingBuilder.bind(routingShanghaiQueue()).to(routingExchange()).with("us.cal.lsj.20201128");
    }

    /**
     * 通配符
     */

    @Bean
    public Queue topicShanghaiQueue() {
        return new Queue(RabbitConstant.QUEUE_Topic_ShangHai);
    }

    @Bean
    public Queue topicBeijingQueue() {
        return new Queue(RabbitConstant.QUEUE_Topic_Beijing);
    }

    //声明exchange
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(RabbitConstant.EXCHANGE_Topic_Topic);
    }

    //声明binding，需要声明一个roytingKey
    @Bean
    public Binding bindTopicHebei1() {
        return BindingBuilder.bind(topicShanghaiQueue()).to(topicExchange()).with("*.*.*.20201127");
    }

    @Bean
    public Binding bindTopicHebei2() {
        return BindingBuilder.bind(topicBeijingQueue()).to(topicExchange()).with("china.#");
    }

    /**
     * 声明一个Quorum队列
     *
     * @return
     */
    @Bean
    public Queue quorumQueue() {
        Map<String, Object> params = new HashMap<>();
        params.put("x-queue-type", "quorum");

        return new Queue(RabbitConstant.QUEUE_QUORUM, true, false, false, params);
    }


    @Bean
    public Queue streamQueue() {
        Map<String, Object> params = new HashMap<>();
        params.put("x-queue-type", "stream");
        params.put("x-max-length-bytes", 20_000_000_000L); // maximum stream size: 20 GB
        params.put("x-stream-max-segment-size-bytes", 100_000_000); // size of segment files: 100 MB

        return new Queue(RabbitConstant.QUEUE_STREAM, true, false, false, params);
    }


    /**
     * 死信交换机，自定义 死信队列
     */

    // 声明死信交换机 rabbit_dlx_exchange
    @Bean
    public TopicExchange dlxExchange() {
        return new TopicExchange("rabbit_dlx_exchange", true, false);
    }

    @Bean
    public Queue dlxQueueA() {
        return new Queue("queue_dlx_A");
    }

    @Bean
    public Queue dlxQueueB() {
        return new Queue("queue_dlx_B");
    }

    @Bean
    public Binding bindDlxAQueue() {
        String routingKey = "routing.a.#";
        return BindingBuilder.bind(dlxQueueA()).to(dlxExchange()).with(routingKey);
    }

    @Bean
    public Binding bindDlxBQueue() {
        String routingKey = "routing.b.#";
        return BindingBuilder.bind(dlxQueueB()).to(dlxExchange()).with(routingKey);
    }

    // 声明正常工作的交换机 rabbit_work_exchange
    @Bean
    public TopicExchange workExchange() {
        return new TopicExchange("rabbit_work_exchange");
    }

    @Bean
    public Queue workNormalQueue() {
        String queueName = "nomarl_work_queue";
        // 要指定的死信交换机
        String deadExchangeName = "rabbit_dlx_exchange";
        // 路由键  这里模拟交给死信交换机下的 A 队列中
        String deadRoutingKey = "routing.a.key";
//        在正常工作队列 work_queue 的配置中注入了 Map<String,Object> 参数，用来配置
//        x-dead-letter-exchange 标识一个交换机
//        x-dead-letter-routing-key 来标识一个绑定键。
        Map<String, Object> args = new HashMap<>(2);
        args.put("x-dead-letter-exchange", deadExchangeName);
        args.put("x-dead-letter-routing-key", deadRoutingKey);
        return new Queue(queueName, true, false, false, args);
    }
    @Bean
    public Binding bindWorkQueue() {
        String routingKey = "#";
        return BindingBuilder.bind(workQueue()).to(workExchange()).with(routingKey);
    }
}
