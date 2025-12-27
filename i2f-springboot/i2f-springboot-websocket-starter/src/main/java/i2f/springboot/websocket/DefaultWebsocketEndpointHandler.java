package i2f.springboot.websocket;

import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

/**
 * @author Ice2Faith
 * @date 2022/4/6 17:38
 * @desc
 */
@Slf4j
@ConditionalOnExpression("${i2f.springboot.websocket.default-endpoint.enable:true}")
@ServerEndpoint(value = "${i2f.springboot.websocket.default-endpoint.path:/default/broadcast}")
@Component
public class DefaultWebsocketEndpointHandler extends BasicWebsocketEndpointHandler {

}
