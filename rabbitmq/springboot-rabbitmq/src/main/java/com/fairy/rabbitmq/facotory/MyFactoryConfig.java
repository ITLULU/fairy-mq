package com.fairy.rabbitmq.facotory;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.ErrorHandler;

/**
 * @author 鹿少年
 * @date 2022/11/10 22:46
 */
@Configuration
public class MyFactoryConfig {

    @Bean(name = "myListenerFactory")
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setMaxConcurrentConsumers(10);
        //一次拉去数量
        factory.setPrefetchCount(2);
        factory.setConcurrentConsumers(5);
        factory.setBatchSize(10);
        factory.setErrorHandler(new ErrorHandler() {
            @Override
            public void handleError(Throwable t) {
                System.out.println("异常消息:"+t.getMessage());
            }
        });
        factory.setConnectionFactory(connectionFactory);
        //配置手动确认
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);

        factory.setAdviceChain(
                RetryInterceptorBuilder
                        .stateless()
                        .recoverer(new RejectAndDontRequeueRecoverer())
                        .retryOperations(rabbitRetryTemplate())
                        .build()
        );
        return factory;
    }

    /**
     * 配置rabbitmqTemplate
     * <p>
     * 推送mq有四种情况：
     * 消息推送到 MQ，但是在 MQ 里找不到交换机
     * 消息推送到 MQ，找到交换机了，当时没有找到队列
     * 消息推送到 MQ，交换机和队列都没找到
     * 消息成功推送
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // 设置消息从生产者发送至 rabbitmq broker 成功的回调 （保证信息到达 broker）
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            // ack=true:消息成功发送到Exchange
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("ConfirmCallback:     " + "相关数据：" + correlationData);
                System.out.println("ConfirmCallback:     " + "确认是否到达交换机：" + ack);
                System.out.println("ConfirmCallback:     " + "原因：" + cause);
            }
        });
        // 设置信息从交换机发送至 queue 失败的回调
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                System.out.println("ReturnCallback:     " + "消息：" + message);
                System.out.println("ReturnCallback:     " + "回应码：" + replyCode);
                System.out.println("ReturnCallback:     " + "回应信息：" + replyText);
                System.out.println("ReturnCallback:     " + "交换机：" + exchange);
                System.out.println("ReturnCallback:     " + "路由键：" + routingKey);
            }
        });
        // 为 true 时，消息通过交换器无法匹配到队列时会返回给生产者，为 false 时，匹配不到会直接丢弃
        rabbitTemplate.setMandatory(true);
        // 设置发送时的转换
        // rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

//    @Bean
//    public ConnectionFactory connectionFactory() {
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//        connectionFactory.setHost("node01");
//        connectionFactory.setPort(5672);
//        connectionFactory.setUsername("admin");
//        connectionFactory.setPassword("admin");
//        //设置virtualHost。
//        connectionFactory.setVirtualHost("/");
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


    @Bean(name = "myListenerFactory2")
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory2(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        //一次拉去数量
        factory.setPrefetchCount(2);
        // 并发消费者数量
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(20);
        factory.setConnectionFactory(connectionFactory);
        //配置手动确认
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);

        factory.setChannelTransacted(true);
        factory.setAdviceChain(
                RetryInterceptorBuilder
                        .stateless()
                        //发送到死信队列
                        .recoverer(new RejectAndDontRequeueRecoverer())
                        .retryOperations(rabbitRetryTemplate())
                        .build()
        );
        return factory;
    }

    @Bean
    public RetryTemplate rabbitRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        // 设置监听（不是必须）
        retryTemplate.registerListener(new RetryListener() {
            @Override
            public <T, E extends Throwable> boolean open(RetryContext retryContext, RetryCallback<T, E> retryCallback) {
                // 执行之前调用 （返回false时会终止执行）
                return true;
            }

            @Override
            public <T, E extends Throwable> void close(RetryContext retryContext, RetryCallback<T, E> retryCallback, Throwable throwable) {
                // 重试结束的时候调用 （最后一次重试 ）
                System.out.println("最后一次重试"+throwable.getMessage());
            }

            @Override
            public <T, E extends Throwable> void onError(RetryContext retryContext, RetryCallback<T, E> retryCallback, Throwable throwable) {
                //  异常 都会调用
                System.out.println(String.format("-----第%s次调用", retryContext.getRetryCount()));
            }
        });

        // 个性化处理异常和重试 （不是必须）
        /* Map<Class<? extends Throwable>, Boolean> retryableExceptions = new HashMap<>();
        //设置重试异常和是否重试
        retryableExceptions.put(AmqpException.class, true);
        //设置重试次数和要重试的异常
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(5,retryableExceptions);*/

        retryTemplate.setBackOffPolicy(backOffPolicyByProperties());
        retryTemplate.setRetryPolicy(retryPolicyByProperties());
        return retryTemplate;
    }

    @Bean
    public ExponentialBackOffPolicy backOffPolicyByProperties() {
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        long maxInterval = 1000;
        long initialInterval = 100;
        double multiplier = 1.0;
        // 重试间隔
        backOffPolicy.setInitialInterval(initialInterval * 1000);
        // 重试最大间隔
        backOffPolicy.setMaxInterval(maxInterval * 1000);
        // 重试间隔乘法策略
        backOffPolicy.setMultiplier(multiplier);
        return backOffPolicy;
    }

    @Bean
    public SimpleRetryPolicy retryPolicyByProperties() {
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        return retryPolicy;
    }


}
