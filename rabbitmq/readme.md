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
## 7： 死信队列和 消息拒收nack
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
https://blog.csdn.net/u011126891/article/details/54376179想·