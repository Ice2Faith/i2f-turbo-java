package i2f.springboot.mq.kafka;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2022/6/13 9:43
 * @desc
 */
@Slf4j
@Data
@ConditionalOnExpression("${i2f.springboot.config.kafka.enable:true}")
@Configuration
public class KafkaAutoConfiguration {

    @Autowired
    private KafkaConfigProperties configProvider;

    @ConditionalOnMissingBean(AdminClient.class)
    // 配置管理Bean
    @Bean
    public AdminClient kafkaAdminClient() {
        Map<String, Object> config = new HashMap<>();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, configProvider.getServerAddress());
        log.info("AdminClient config done.");
        return AdminClient.create(config);
    }

    // 配置生产工厂
    @Bean
    @ConditionalOnExpression("${i2f.springboot.config.kafka.producer.enable:true}")
    public KafkaProducer<String, String> kafkaProducer() {
        return new KafkaProducer<String, String>(configProvider.producerConfig());
    }

    @Bean
    @ConditionalOnExpression("${i2f.springboot.config.kafka.consumer.enable:true}")
    public KafkaConsumer<String, Object> kafkaConsumer() {
        return new KafkaConsumer<String, Object>(configProvider.consumerConfig());
    }
}
