package kafka;

/**
 * @author hll
 * @version 1.0
 * @date 2022/7/6 16:25
 */

import com.alibaba.fastjson.JSON;
import com.fairy.kafka.KafkaApp;
import com.fairy.kafka.model.dto.OrderDto;
import com.fairy.kafka.service.KafkaSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = KafkaApp.class)
public class SenderMessagerTest {

    @Autowired
    private KafkaSender kafkaSender;

    @Test
    public void sendTrans() {
        OrderDto dto = OrderDto.builder()
                .orderAmount(111.0)
                .orderId(1121)
                .productId(1212)
                .build();
        kafkaSender.doTransactionSend3("my-replicated-topic", 0, "1212", JSON.toJSONString(dto));
    }
}
