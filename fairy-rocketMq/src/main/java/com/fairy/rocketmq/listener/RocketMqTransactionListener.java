package com.fairy.rocketmq.listener;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fairy.common.utils.DateUtil;
import com.fairy.rocketmq.domain.RocketMqLog;
import com.fairy.rocketmq.mapper.RocketMqLogMapper;
import com.fairy.rocketmq.service.RocketMqLogService;
import com.google.common.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.apache.rocketmq.spring.support.RocketMQUtil;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ：hll
 * 关于@RocketMQTransactionListener 这个注解，有点奇怪。2.0.4版本中，是需要指定txProducerGroup指向一个消息发送者组。
 * 不同的组可以有不同的事务消息逻辑。
 * 但是到了2.1.1版本，只能指定rocketMQTemplateBeanMame，也就是说如果你有多个发送者组需要有不同的事务消息逻辑，
 * 那就需要定义多个RocketMQTemplate。
 * 而且这个版本中，虽然重现了我们在原生API中的事务消息逻辑，但是测试过程中还是发现一些奇怪的特性，用的时候要注意点。
 *
 * @RocketMQTransactionListener：配合RocketMQLocalTransactionListener接口一起使用，
 *
 * 2、配置文件
 **/
@Slf4j
//@RocketMQTransactionListener(txProducerGroup = "springBootGroup2")
@RocketMQTransactionListener(rocketMQTemplateBeanName = "rocketMQTemplate")
public class RocketMqTransactionListener implements RocketMQLocalTransactionListener {

    /**
     * 可以作个guava内置缓存
     */
    @Autowired
    private Cache<String,String> cache;
    @Autowired
    private RocketMqLogService mqLogService;
    /**
     * 执行本地事务  在消息被消费之前执行本地的事务操作 只有本地事务提交后发送到MQ中的事物消息才对Consumer可见
     * 否则如果本地事务执行失败，那么消息队列中的消息也会回滚
     * @param msg 消息
     * @param arg 参数
     * @return RocketMQLocalTransactionState
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        //事务Id
        String transId = msg.getHeaders().get(RocketMQHeaders.PREFIX + RocketMQHeaders.TRANSACTION_ID).toString();
        log.info("执行本地事务,事务ID:{}",transId);

        Integer orderId = Integer.valueOf(msg.getHeaders().get("orderId").toString());
        String  topic = msg.getHeaders().get((RocketMQHeaders.TOPIC)).toString();
        Date date = new Date(msg.getHeaders().getTimestamp());

        //目的地  String destination = TOPIC_NAME + ":" + tags[i % tags.length]; arg 发送时传递参数
        String destination = arg.toString();
        cache.put(String.valueOf(transId), msg.toString());

        //转成RocketMQ的Message对象
        org.apache.rocketmq.common.message.Message message = RocketMQUtil.convertToRocketMessage(new StringMessageConverter(), "UTF-8", destination, msg);
        //这个msg的实现类是GenericMessage，里面实现了toString方法
        //在Header中自定义的RocketMQHeaders.TAGS属性，到这里就没了。但是RocketMQHeaders.TRANSACTION_ID这个属性就还在。
        //而message的Header里面会默认保存RocketMQHeaders里的属性，但是都会加上一个RocketMQHeaders.PREFIX前缀
        log.info("executeLocalTransaction destination:{},orderId:{},date:{}" ,destination,orderId,DateUtil.parseDate(date,DateUtil.format));
       try{
           mqLogService.inserMqLog(transId, topic,destination);
           //模拟异常
//           int i=1/0;
           return RocketMQLocalTransactionState.COMMIT;
       }catch (Exception e){
           log.error("异常结果：{}",e);
           return RocketMQLocalTransactionState.ROLLBACK;
       }

    }

    /**
     * 本地事务的检查接口,即MQServer没有收到二次确认消息时调用的方法 去检查本地事务到底有没有成功
     * 如果超过一定时间还本地事务还没有提交，就会调用checkLocalTransaction执行本地事务回查。
     * @param msg 消息
     * @return RocketMQLocalTransactionState
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        String transactionId = msg.getHeaders().get(RocketMQHeaders.PREFIX + RocketMQHeaders.TRANSACTION_ID).toString();
        log.info("检查本地事务,事务ID:{}",transactionId);
        //根据事务id从日志表检索 回查 既然Mq事务提交成功
        RocketMqLog rocketMqLog = mqLogService.selectOne(transactionId);
        if(null != rocketMqLog){
            return RocketMQLocalTransactionState.COMMIT;
        }
        return RocketMQLocalTransactionState.ROLLBACK;
    }
}
