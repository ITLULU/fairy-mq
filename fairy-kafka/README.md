## 监听类型
```
listener: ack-mode
      # 当每一条记录被消费者监听器（ListenerConsumer）处理之后提交
      # RECORD
      # 当每一批poll()的数据被消费者监听器（ListenerConsumer）处理之后提交
      # BATCH
      # 当每一批poll()的数据被消费者监听器（ListenerConsumer）处理之后，距离上次提交时间大于TIME时提交
      # TIME
      # 当每一批poll()的数据被消费者监听器（ListenerConsumer）处理之后，被处理record数量大于等于COUNT时提交
      # COUNT
      # TIME |　COUNT　有一个条件满足时提交
      # COUNT_TIME
      # 当每一批poll()的数据被消费者监听器（ListenerConsumer）处理之后, 手动调用Acknowledgment.acknowledge()后提交
      # MANUAL
      # 手动调用Acknowledgment.acknowledge()后立即提交
      # MANUAL_IMMEDIATE
```

## 消费者监听
  >  @KafkaListener(groupId = "testGroup", topicPartitions = {
                 @TopicPartition(topic = "topic1", partitions = {"0", "1"}),
                   @TopicPartition(topic = "topic2", partitions = "0",
                           partitionOffsets = @PartitionOffset(partition = "1", initialOffset = "100"))
           },concurrency = "6")
      
> concurrency就是同组下的消费者个数，就是并发消费数，必须小于等于分区总数     
  
 ##   kafka的事务
    
````
      如果开启的事务，则后续发送消息必须使用@Transactional注解或者使用kafkaTemplate.executeInTransaction() ,否则抛出异常，异常信息如下：
      // 1 初始化事务
      void initTransactions();
      // 2 开启事务
      void beginTransaction() throws ProducerFencedException;
      // 3 在事务内提交已经消费的偏移量（主要用于消费者）
      void sendOffsetsToTransaction(Map<TopicPartition, OffsetAndMetadata> offsets,
       String consumerGroupId) throws ProducerFencedException;
      // 4 提交事务
      void commitTransaction() throws ProducerFencedException;
      // 5 放弃事务（类似于回滚事务的操作）
      void abortTransaction() throws ProducerFencedException;

````

````
   消费者组监听  如果监听两个分区 会出现本身是拿了pollNum数据，可是在消费的时候把另外一个分区的偏移量也提交了，这不是就造成数据丢失了
后面经过证实 ：在消费端监听消费主题下多个分区时，是并发一起消费的数据并没有丢失

在消费数据后，还没走到ack，断开消费端 数据offset就自动提交了，下次就消费不到了 

````