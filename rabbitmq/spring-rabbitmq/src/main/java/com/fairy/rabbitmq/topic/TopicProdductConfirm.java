package com.fairy.rabbitmq.topic;

import com.fairy.rabbitmq.RabbitConstant;
import com.fairy.rabbitmq.RabbitmqUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 通配符模式 发送者
 *
 * @author 鹿少年
 * @date 2022/11/8 20:29
 */
public class TopicProdductConfirm {

    public static void main(String[] args) throws Exception {
        Map area = new LinkedHashMap<String, String>();
        area.put("china.hunan.changsha.20201127", "中国湖南长沙20201127天气数据");
        area.put("china.hubei.wuhan.20201127", "中国湖北武汉20201127天气数据");
        area.put("china.hunan.zhuzhou.20201127", "中国湖南株洲20201127天气数据");
        area.put("us.cal.lsj.20201127", "美国加州洛杉矶20201127天气数据");

        area.put("china.hebei.shijiazhuang.20201128", "中国河北石家庄20201128天气数据");
        area.put("china.hubei.wuhan.20201128", "中国湖北武汉20201128天气数据");
        area.put("china.henan.zhengzhou.20201128", "中国河南郑州20201128天气数据");
        area.put("us.cal.lsj.20201128", "美国加州洛杉矶20201128天气数据");
        Connection connection = RabbitmqUtils.getConnection();
        Channel channel = connection.createChannel();


        //开启confirm监听模式
        AMQP.Confirm.SelectOk selectOk = channel.confirmSelect();

        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long l, boolean b) throws IOException {
                //第二个参数代表接收的数据是否为批量接收，一般我们用不到。
                System.out.println("消息已被Broker接收,Tag:" + l+ " b:"+ b);
            }

            @Override
            public void handleNack(long l, boolean b) throws IOException {
                System.out.println("消息已被Broker拒收,Tag:" + l + " b:"+ b);
            }
        });
        //消费监听 没有被消费的
        channel.addReturnListener(new ReturnCallback() {
            @Override
            public void handle(Return returnMessage) {

                System.err.println("===========================");
                System.err.println("Return编码：" + returnMessage.getReplyCode() + "  -Return描述:" + returnMessage.getReplyText());
                System.err.println("交换机:" + returnMessage.getExchange() + "  -路由key:" + returnMessage.getRoutingKey());
                System.err.println("Return主题：" + new String(returnMessage.getBody()));
                System.err.println("===========================");
            }
        });
        Iterator<Map.Entry<String, String>> itr = area.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, String> me = itr.next();
            //Routing key 第二个参数相当于数据筛选的条件
            //第三个参数为：mandatory true代表如果消息无法正常投递则return回生产者，如果false，则直接将消息放弃。
            channel.basicPublish(RabbitConstant.EXCHANGE_Topic_Topic, me.getKey(), true, null, me.getValue().getBytes());
        }

        //如果关闭则无法进行监听，因此此处不需要关闭
        /*channel.close();
        connection.close();*/
    }
}
