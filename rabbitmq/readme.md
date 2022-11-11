# rabbitmq知识:


## 1: 基础架构 概念 
## 2： 工作模式 交换类型
## 3： 发送者· 消费者 监听 消息
## 4： 消息convert转换 
有一个DefaultMessagePropertiesConverter
## 5： 消息确认 不丢失
## 6： 消息延迟
## 7： 死信队列和 消息拒收nack
## 8: 消息过多时，消费不过来时出现的消息堆积现象如何处理
## 9： 消息消费批量数据 与simple get
## 10： 发送者发送消息的机制 和消费者消费消息的机制
## 11： 消息发送的尝试重试机制
MessageRecoverer 重试发送失败




将异常数据以String形式输出：
private String getStackTraceAsString(Throwable cause) {
StringWriter stringWriter = new StringWriter();
PrintWriter printWriter = new PrintWriter(stringWriter, true);
cause.printStackTrace(printWriter);
return stringWriter.getBuffer().toString();
}