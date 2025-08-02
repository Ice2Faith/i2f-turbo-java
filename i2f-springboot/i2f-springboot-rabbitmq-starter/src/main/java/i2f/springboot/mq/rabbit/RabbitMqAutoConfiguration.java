package i2f.springboot.mq.rabbit;

import i2f.springboot.mq.rabbit.impl.RabbitConfirmCallbackLogImpl;
import i2f.springboot.mq.rabbit.impl.RabbitReturnCallbackLogImpl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ltb
 * @date 2021/9/13
 */
@Slf4j
@Data
@ConditionalOnExpression("${i2f.springboot.config.rabbit.enable:true}")
@Configuration
@ConfigurationProperties(prefix = "i2f.springboot.config.rabbit")
public class RabbitMqAutoConfiguration {

    private boolean mandatory=true;

    private boolean logConfirmCallback=false;

    private boolean logReturnCallback=false;

    @ConditionalOnMissingBean(RabbitTemplate.class)
    @Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(mandatory);

        if(logConfirmCallback) {
            rabbitTemplate.setConfirmCallback(new RabbitConfirmCallbackLogImpl());
        }

        if(logReturnCallback) {
            rabbitTemplate.setReturnCallback(new RabbitReturnCallbackLogImpl());
        }

        return rabbitTemplate;
    }
}
