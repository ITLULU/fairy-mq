package com.fairy.rabbitmq.facotory;

import org.springframework.amqp.rabbit.support.DefaultMessagePropertiesConverter;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author 鹿少年
 * @date 2022/11/14 21:50
 */
@Configuration
public class QueueConfig {

    //    @Bean
//    public ConnectionFactory getConnectionFactory() {
//        com.rabbitmq.client.ConnectionFactory rabbitConnectionFactory =
//                new com.rabbitmq.client.ConnectionFactory();
//        rabbitConnectionFactory.setHost("node01");
//        rabbitConnectionFactory.setPort(5672);
//        rabbitConnectionFactory.setUsername("admin");
//        rabbitConnectionFactory.setPassword("admin");
//        rabbitConnectionFactory.setVirtualHost("/");
//
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitConnectionFactory);
//        //消息的确认机制（confirm）；
//        connectionFactory.setPublisherConfirms(true);
//        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
//        connectionFactory.setPublisherReturns(true);
//        //setCacheMode：设置缓存模式，共有两种，CHANNEL和CONNECTION模式。
//        //1、CONNECTION模式，这个模式下允许创建多个Connection，会缓存一定数量的Connection，每个Connection中同样会缓存一些Channel，
//        // 除了可以有多个Connection，其它都跟CHANNEL模式一样。
//        //2、CHANNEL模式，程序运行期间ConnectionFactory会维护着一个Connection，
//        // 所有的操作都会使用这个Connection，但一个Connection中可以有多个Channel，
//        // 操作rabbitmq之前都必须先获取到一个Channel，
//        // 否则就会阻塞（可以通过setChannelCheckoutTimeout()设置等待时间），
//        // 这些Channel会被缓存（缓存的数量可以通过setChannelCacheSize()设置）；
//        connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CONNECTION);   //设置CONNECTION模式，可创建多个Connection连接
//        //设置每个Connection中缓存Channel的数量，不是最大的。操作rabbitmq之前（send/receive message等）
//        // 要先获取到一个Channel.获取Channel时会先从缓存中找闲置的Channel，如果没有则创建新的Channel，
//        // 当Channel数量大于缓存数量时，多出来没法放进缓存的会被关闭。
//        connectionFactory.setChannelCacheSize(10);
//        //单位：毫秒；配合channelCacheSize不仅是缓存数量，而且是最大的数量。
//        // 从缓存获取不到可用的Channel时，不会创建新的Channel，会等待这个值设置的毫秒数
//        //同时，在CONNECTION模式，这个值也会影响获取Connection的等待时间，
//        // 超时获取不到Connection也会抛出AmqpTimeoutException异常。
//        connectionFactory.setChannelCheckoutTimeout(600);
//
//        //仅在CONNECTION模式使用，设置Connection的缓存数量。
//        connectionFactory.setConnectionCacheSize(3);
//        //setConnectionLimit：仅在CONNECTION模式使用，设置Connection的数量上限。
//        connectionFactory.setConnectionLimit(10);
//        return connectionFactory;
//    }
//

    //    @Bean(name = "rabbitAdmin")
//    public RabbitAdmin getRabbitAdmin() {
//        RabbitAdmin rabbitAdmin = new RabbitAdmin(getConnectionFactory());
//        rabbitAdmin.setAutoStartup(true);
//        return rabbitAdmin;
//    }


//    @Bean(name = "springMessageQueue")
//    public Queue createQueue(@Qualifier("rabbitAdmin") RabbitAdmin rabbitAdmin) {
//        Queue sendQueue = new Queue(AppConstants.SEND_QUEUE_NAME, true, false, false);
//        rabbitAdmin.declareQueue(sendQueue);
//        return sendQueue;
//    }
//
//    @Bean(name = "springMessageExchange")
//    public Exchange createExchange(@Qualifier("rabbitAdmin") RabbitAdmin rabbitAdmin) {
//        DirectExchange sendExchange = new DirectExchange(AppConstants.SEND_EXCHANGE_NAME, true, false);
//        rabbitAdmin.declareExchange(sendExchange);
//        return sendExchange;
//    }
//
//    @Bean(name = "springMessageBinding")
//    public Binding createMessageBinding(@Qualifier("rabbitAdmin") RabbitAdmin rabbitAdmin) {
//        Map<String, Object> arguments = new HashMap<String, Object>();
//        Binding sendMessageBinding =
//                new Binding(AppConstants.SEND_QUEUE_NAME, Binding.DestinationType.QUEUE,
//                        AppConstants.SEND_EXCHANGE_NAME, AppConstants.SEND_MESSAGE_KEY, arguments);
//        rabbitAdmin.declareBinding(sendMessageBinding);
//        return sendMessageBinding;
//    }
//
//    @Bean(name = "springReplyMessageQueue")
//    public Queue createReplyQueue(@Qualifier("rabbitAdmin") RabbitAdmin rabbitAdmin) {
//        Queue replyQueue = new Queue(AppConstants.REPLY_QUEUE_NAME, true, false, false);
//        rabbitAdmin.declareQueue(replyQueue);
//        return replyQueue;
//    }

//    @Bean(name = "springReplyMessageExchange")
//    public Exchange createReplyExchange(@Qualifier("rabbitAdmin") RabbitAdmin rabbitAdmin) {
//        DirectExchange replyExchange = new DirectExchange(AppConstants.REPLY_EXCHANGE_NAME, true, false);
//        rabbitAdmin.declareExchange(replyExchange);
//        return replyExchange;
//    }

//    @Bean(name = "springReplyMessageBinding")
//    public Binding createReplyMessageBinding(@Qualifier("rabbitAdmin") RabbitAdmin rabbitAdmin) {
//        Map<String, Object> arguments = new HashMap<String, Object>();
//        Binding replyMessageBinding =
//                new Binding(AppConstants.REPLY_QUEUE_NAME, Binding.DestinationType.QUEUE,
//                        AppConstants.REPLY_EXCHANGE_NAME, AppConstants.REPLY_MESSAGE_KEY, arguments);
//        rabbitAdmin.declareBinding(replyMessageBinding);
//        return replyMessageBinding;
//    }

    @Bean(name = "threadExecutor")
    public ThreadPoolTaskExecutor createThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor =
                new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(5);
        threadPoolTaskExecutor.setMaxPoolSize(10);
        threadPoolTaskExecutor.setQueueCapacity(200);
        threadPoolTaskExecutor.setKeepAliveSeconds(20000);
        return threadPoolTaskExecutor;
    }

    @Bean(name = "serializerMessageConverter")
    public MessageConverter getMessageConverter() {
        return new SimpleMessageConverter();
    }

    @Bean(name = "messagePropertiesConverter")
    public MessagePropertiesConverter getMessagePropertiesConverter() {
        return new DefaultMessagePropertiesConverter();
    }
}
