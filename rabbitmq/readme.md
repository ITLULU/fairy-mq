# rabbitmq知识:


## 1: 基础架构 概念 
## 2： 工作模式 交换类型
## 3： 发送者· 消费者 监听 消息
消息监听，在 @RabbitListener 注解的方法中，使用 @Payload 和 @Headers 注解可以获取消息中的 body 和 headers 消息。它们都会被 MessageConvert 转换器解析转换后(使用 fromMessage 方法进行转换)，将结果绑定在对应注解的方法中。

```
    @RabbitListener(containerFactory = "myListenerFactory", bindings = {
@QueueBinding(
        value = @Queue(value = "${rabbitmq.queue.routing.beijing}", durable = "true", autoDelete = "false"),
        exchange = @Exchange(
                value = "${rabbitmq.exchange.routing}",
                durable = "true",
                type = ExchangeTypes.TOPIC),
        key = "china.#")}, id = "autoStart")
public void receive(@Payload Message message, Channel channel,
            @Headers Map<String, Object> headers,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey, @Header(AmqpHeaders.MESSAGE_ID) String messageId, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
System.out.println("路由监听接受到发送者发送的信息：" + new String(message.getBody()));
//        int i = 1 / 0;
// 确认消息
channel.basicAck(deliveryTag, false);
}
```


```
@RabbitListener 和 @RabbitHandler 搭配使用
@RabbitListener 可以标注在类上面，需配合 @RabbitHandler 注解一起使用。
@RabbitListener 标注在类上面表示当有收到消息的时候，就交给 @RabbitHandler 注解的方法进行分发处理，具体使用哪个方法处理，根据 MessageConverter 转换后的参数类型
```


## 4： 消息convert转换 
MessagePropertiesConverter 和 MessageConverter
有一个DefaultMessagePropertiesConverter， ，MessageConverter 其实现有 SimpleMessageConverter（默认）、Jackson2JsonMessageConverter 等。
如果自定义了消息转换器需要在factory注册上


## 5： 消息确认 不丢失
## 6： 消息延迟
通TTL和死信队列实现延迟消息
## 7： 死信队列和 消息拒收nack

```
被设置了TTL的消息在过期后会成为 Dead Letter。其实在 RabbitMQ 中，一共有三种消息的“死亡”形式：

消息被拒绝（basic.reject或basic.nack）并且requeue = false.
消息 TTL 过期
队列达到最大长度（队列满了，无法再添加数据到mq中）
```
死信处理过程

```
DLX 也是一个正常的 Exchange，和一般的 Exchange 没有区别，它能在任何的队列上被指定，实际上就是设置某个队列的属性。
当这个队列中有死信时，RabbitMQ 就会自动的将这个消息重新发布到设置的 Exchange上去，进而被路由到另一个队列。
可以监听这个队列中的消息做相应的处理。
```

定义业务（普通）队列的时候指定参数：
```
x-dead-letter-exchange: 用来设置死信后发送的交换机
x-dead-letter-routing-key：用来设置死信的 routingKey
```



## 8: 消息过多时，消费不过来时出现的消息堆积现象如何处理
## 9： 消息消费批量数据 与simple get
## 10： 发送者发送消息的机制 和消费者消费消息的机制
## 11： 消息发送的尝试重试机制
MessageRecoverer 重试发送失败




将异常数据以String形式输出：
```
private String getStackTraceAsString(Throwable cause) {
StringWriter stringWriter = new StringWriter();
PrintWriter printWriter = new PrintWriter(stringWriter, true);
cause.printStackTrace(printWriter);
return stringWriter.getBuffer().toString();
}
```


x

## 批量消息发送
https://blog.csdn.net/u011126891/article/details/54376179

## rabbitmq监控

## rabbitmq插件 延迟消息实现