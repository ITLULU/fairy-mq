package com.fairy.rocketmq.config;

import com.fairy.common.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.apache.rocketmq.spring.support.RocketMQUtil;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.StringMessageConverter;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ：hll
 * 关于@RocketMQTransactionListener 这个注解，有点奇怪。2.0.4版本中，是需要指定txProducerGroup指向一个消息发送者组。
 * 不同的组可以有不同的事务消息逻辑。
 * 但是到了2.1.1版本，只能指定rocketMQTemplateBeanMame，也就是说如果你有多个发送者组需要有不同的事务消息逻辑，
 * 那就需要定义多个RocketMQTemplate。
 * 而且这个版本中，虽然重现了我们在原生API中的事务消息逻辑，但是测试过程中还是发现一些奇怪的特性，用的时候要注意点。
 **/
@Slf4j
//@RocketMQTransactionListener(txProducerGroup = "springBootGroup2")
@RocketMQTransactionListener(rocketMQTemplateBeanName = "rocketMQTemplate")
public class MyRocketMqTransaction implements RocketMQLocalTransactionListener {

    private ConcurrentHashMap<Object, Message> localTrans = new ConcurrentHashMap<>();

    /**
     * 执行本地事务
     * @param msg 消息
     * @param arg 参数
     * @return RocketMQLocalTransactionState
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        //事务Id
        Object transId = msg.getHeaders().get(RocketMQHeaders.PREFIX + RocketMQHeaders.TRANSACTION_ID);
        //目的地  String destination = TOPIC_NAME + ":" + tags[i % tags.length]; arg 发送时传递参数
        String destination = arg.toString();
        localTrans.put(transId, msg);

        //转成RocketMQ的Message对象
        org.apache.rocketmq.common.message.Message message = RocketMQUtil.convertToRocketMessage(new StringMessageConverter(), "UTF-8", destination, msg);
        String tags = message.getTags();
        //这个msg的实现类是GenericMessage，里面实现了toString方法
        //在Header中自定义的RocketMQHeaders.TAGS属性，到这里就没了。但是RocketMQHeaders.TRANSACTION_ID这个属性就还在。
        //而message的Header里面会默认保存RocketMQHeaders里的属性，但是都会加上一个RocketMQHeaders.PREFIX前缀
        log.info("executeLocalTransaction msg :{},destination:{},tags:{}" , msg,destination,tags);

        if (StringUtils.contains(tags, "TagA")) {
            return RocketMQLocalTransactionState.COMMIT;
        } else if (StringUtils.contains(tags, "TagB")) {
            return RocketMQLocalTransactionState.ROLLBACK;
        } else {
            return RocketMQLocalTransactionState.UNKNOWN;
        }
    }

    /**
     * 本地事务的检查接口,即MQServer没有收到二次确认消息时调用的方法 去检查本地事务到底有没有成功
     * @param msg 消息
     * @return RocketMQLocalTransactionState
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        String transId = msg.getHeaders().get(RocketMQHeaders.PREFIX + RocketMQHeaders.TRANSACTION_ID).toString();
        Message originalMessage = localTrans.get(transId);
        Date date = new Date(msg.getHeaders().getTimestamp());
        //这里能够获取到自定义的transaction_id属性
//        log.info("checkLocalTransaction 发送时间：{} originalMessage:{},  msg ={}",DateUtil.parseDate(date,DateUtil.format), originalMessage,msg);
        //获取标签时，自定义的RocketMQHeaders.TAGS拿不到，但是框架会封装成一个带RocketMQHeaders.PREFIX的属性
        String tags1 = msg.getHeaders().get(RocketMQHeaders.TAGS).toString();
        String MyProp = msg.getHeaders().get("MyProp").toString();
        String tags = msg.getHeaders().get(RocketMQHeaders.PREFIX + RocketMQHeaders.TAGS).toString();
        log.info("tage:{},tags1:{},MyProp:{}",tags,tags1,MyProp);
        if (StringUtils.contains(tags, "TagC")) {
            return RocketMQLocalTransactionState.COMMIT;
        } else if (StringUtils.contains(tags, "TagD")) {
            return RocketMQLocalTransactionState.ROLLBACK;
        } else {
            return RocketMQLocalTransactionState.UNKNOWN;
        }
    }
}
