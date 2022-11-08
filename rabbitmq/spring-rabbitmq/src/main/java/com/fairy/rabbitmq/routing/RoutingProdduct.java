package com.fairy.rabbitmq.routing;

import com.fairy.rabbitmq.MqMessage;
import com.fairy.rabbitmq.RabbitConstant;
import com.fairy.rabbitmq.RabbitmqUtils;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 路由模式 发送者
 * @author 鹿少年
 * @date 2022/11/8 20:29
 */
public class RoutingProdduct {

    public static void main(String[] args) throws Exception {
        Map area = new LinkedHashMap<String, String>();
        area.put("china.hunan.changsha.20201127", "中国湖南长沙20201127天气数据");
        area.put("china.hubei.wuhan.20201127", "中国湖北武汉20201127天气数据");
        area.put("china.hunan.zhuzhou.20201127", "中国湖南株洲20201128天气数据");
        area.put("us.cal.lsj.20201127", "美国加州洛杉矶20201127天气数据");

        area.put("china.hebei.shijiazhuang.20201128", "中国河北石家庄20201128天气数据");
        area.put("china.hubei.wuhan.20201128", "中国湖北武汉20201128天气数据");
        area.put("china.henan.zhengzhou.20201128", "中国河南郑州20201128天气数据");
        area.put("us.cal.lsj.20201128", "美国加州洛杉矶20201128天气数据");
        Connection connection = RabbitmqUtils.getConnection();
        Channel channel = connection.createChannel();


        Iterator<Map.Entry<String, String>> itr = area.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, String> me = itr.next();
            //第一个参数交换机名字   第二个参数作为 消息的routing key
            channel.basicPublish(RabbitConstant.EXCHANGE_ROUTING_Topic,me.getKey() , null , me.getValue().getBytes());
//            channel.basicPublish(RabbitConstant.EXCHANGE_ROUTING_direct,me.getKey() , null , me.getValue().getBytes());
            //如果是广播模式会将所以与交换机绑定的队列发送 不符合
//            channel.basicPublish(RabbitConstant.EXCHANGE_ROUTING_Fanout,me.getKey() , null , me.getValue().getBytes());

        }
        System.out.println("mq消息发送成功");
        channel.close();
        connection.close();
    }
}
