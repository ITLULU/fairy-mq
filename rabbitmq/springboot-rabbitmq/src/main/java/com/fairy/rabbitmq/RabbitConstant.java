package com.fairy.rabbitmq;

/**
 * @author 鹿少年
 * @date 2022/11/7 22:42
 */
public class RabbitConstant {
    public static final String QUEUE_Simple = "simple-queue";
    public static final String QUEUE_WorkQueue = "worke-queue";
    public static final String QUEUE_Pubsu_Beijing = "beijing-queue";
    public static final String QUEUE_Pubsu_Shanghai = "shanghai-queue";

    public static final String QUEUE_Routing_Shanghai = "shanghai-queue";
    public static final String QUEUE_Routing_Beijing = "beijing-queue";

    public static final String QUEUE_Topic_Beijing = "beijing-queue";
    public static final String QUEUE_Topic_ShangHai = "shanghai-queue";

    public static final String QUEUE_Fanout = "fanout-queue";

    public static final String QUEUE_RPC = "rpc-queue";
    //direct 类型交换机 定向，把消息交给符合指定routing key 的队列
    public static final String EXCHANGE_PUBSUB_Direct = "pubsub-direct-exchange";
    //Fanout 广播，将消息交给所有绑定到交换机的队列
    public static final String EXCHANGE_PUBSUB_Fanout = "pubsub-fanout-exchange";
    //topic类型交换机 通配符，把消息交给符合routing pattern（路由模式） 的队列
    public static final String EXCHANGE_ROUTING_Topic = "routing-topic-exchange";
    //定向
    public static final String EXCHANGE_ROUTING_direct = "routing-direct-exchange";
    //广播
    public static final String EXCHANGE_ROUTING_Fanout = "routing-fanout-exchange";


    public static final String EXCHANGE_Topic_Topic = "topic-topic-exchange";

    public static final String host = "node01";
    public static final String userName = "admin";
    public static final String password = "admin";
    public static final String virture = "/";
    public static final int port = 5672;

    //header模式
    public static final String EXCHANGE_HEADER = "headerExchange";
    public static final String QUEUE_Header_TXTYP1 = "txTyp1";
    public static final String QUEUE_Header_BUSTYP1 = "busTyp1";
    public static final String QUEUE_Header_TXBUSTYP1 = "txbusTyp1";


    public static final String QUEUE_QUORUM = "quorum_queue";
    public static final String QUEUE_STREAM = "stream_queue";

    public static final String death_EXCHAGE = "rabbit_dlx_exchange";


    public static final String MINOR_QUEUE = "MINOR_QUEUE";
}
