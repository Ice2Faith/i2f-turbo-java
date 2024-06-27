package i2f.springcloud.gateway;

import i2f.springcloud.gateway.filters.RequestAttrGatewayFilterFactory;
import i2f.springcloud.gateway.filters.global.RequestLogGlobalFilter;
import i2f.springcloud.gateway.predicates.RequestAttrRoutePredicateFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * @author Ice2Faith
 * @date 2024/6/27 17:33
 * @desc
 */
@Data
@Slf4j
@ConditionalOnExpression("${i2f.springcloud.gateway.enable:true}")
@Import({
        GatewayCorsConfig.class,
        RequestAttrRoutePredicateFactory.class,
        RequestAttrGatewayFilterFactory.class,
        RequestLogGlobalFilter.class
})
@ConfigurationProperties(prefix = "i2f.springcloud.gateway")
public class GatewayAutoConfiguration implements InitializingBean {
    private boolean enableAccessLog = false;
    public static final String GATEWAY_ACCESS_LOG_PROPERTY_NAME = "reactor.netty.http.server.accessLogEnabled";

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("GatewayAutoConfiguration config done.");
        System.setProperty(GATEWAY_ACCESS_LOG_PROPERTY_NAME, enableAccessLog + "");
    }
}
