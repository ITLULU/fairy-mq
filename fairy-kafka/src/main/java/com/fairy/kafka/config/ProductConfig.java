package com.fairy.kafka.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.transaction.KafkaTransactionManager;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * @author hll
 * @version 1.0
 * @date 2022/6/30 15:49
 */
@Configuration
public class ProductConfig {


    /**
     * kafkaProductFactory
     *
     * @param producerFactory
     * @return
     */
  /*  @Bean("kafkaTemplate")
    public KafkaTemplate<String, String> kafkaTemplate(@Autowired ProducerFactory producerFactory) {
        KafkaTemplate kafkaTemplate = new KafkaTemplate<>(producerFactory);
        return kafkaTemplate;
    }
*/

/*    @Bean("transKafkaTemplate")
    public KafkaTemplate<String, String> transKafkaTemplate(ProducerFactory producerFactory) {
        Map<String, Object> pros = producerFactory.getConfigurationProperties();
        pros.put("transaction-id-prefix", "trx_");
//        pros.put("transaction-id-prefix", "trx_" + System.currentTimeMillis() + new Random().nextLong());
        KafkaTemplate kafkaTemplate = new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(pros));
        return kafkaTemplate;
    }*/

    /**
     * 以该方式配置事务管理器：就不能以普通方式发送消息，只能通过 kafkaTemplate.executeInTransaction
     * 或在方法上加 @Transactional 注解来发送消息，否则报错
     */
    @Bean
    public KafkaTransactionManager kafkaTransactionManager(ProducerFactory producerFactory) {
        KafkaTransactionManager<String, String> kafkaTransactionManager = new KafkaTransactionManager<>(producerFactory);
        return kafkaTransactionManager;
    }
}
