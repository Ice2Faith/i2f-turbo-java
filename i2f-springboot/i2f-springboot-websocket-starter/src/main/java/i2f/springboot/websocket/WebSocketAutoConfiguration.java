package i2f.springboot.websocket;

import i2f.reflect.ReflectResolver;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2022/2/25 8:48
 * @desc
 * https://www.cnblogs.com/xuwenjin/p/12664650.html
 *
 * https://blog.csdn.net/java_mindmap/article/details/105898152
 */
@ConditionalOnExpression("${i2f.springboot.websocket.enable:true}")
@Slf4j
@Data
@NoArgsConstructor
@EnableWebSocket
@Configuration
@ConfigurationProperties(prefix = "i2f.springboot.websocket")
public class WebSocketAutoConfiguration implements WebSocketConfigurer {

    @Autowired
    private ApplicationContext applicationContext;

    private Map<String,RegistryItemProperties> registry=new LinkedHashMap<>();

    @Data
    @NoArgsConstructor
    public static class RegistryItemProperties{
        private boolean useContextBean=false;
        private String handler;
        private String interceptor;
        private String path;
        private String allowOrigin;
    }
    /**
     * 注入一个ServerEndpointExporter,该Bean会自动注册使用@ServerEndpoint注解申明的websocket endpoint
     * 提供给传统方式使用
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        log.info("websocket config done.");
        return new ServerEndpointExporter();
    }

    /**
     * 实现 WebSocketConfigurer 接口，使用springboot的方式注册
     * @param registry
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        for(Map.Entry<String,RegistryItemProperties> entry : this.registry.entrySet()){
            RegistryItemProperties item= entry.getValue();

            boolean useBean=item.isUseContextBean();

            String handlerName=item.getHandler();
            String interceptorName=item.getInterceptor();
            String path=item.getPath();
            String allowOrigin=item.getAllowOrigin();

            if(allowOrigin==null || "".equals(allowOrigin)){
                allowOrigin="*";
            }
            if(path==null || "".equals(path)){
                log.warn("websocket registry bad find path:"+path+" in id:"+entry.getKey());
                continue;
            }

            if(handlerName==null || "".equals(handlerName)){
                log.warn("websocket registry bad find handler-name:"+handlerName+" in id:"+entry.getKey());
                continue;
            }

            if(interceptorName==null || "".equals(interceptorName)){
                log.warn("websocket registry bad find interceptor-name:"+interceptorName+" in id:"+entry.getKey());
                continue;
            }

            WebSocketHandler handler=null;
            HandshakeInterceptor interceptor=null;
            if(useBean){
                handler= applicationContext.getBean(handlerName,WebSocketHandler.class);
                interceptor= applicationContext.getBean(interceptorName,HandshakeInterceptor.class);
            }else{
                Class<?> handlerClass= ReflectResolver.loadClass(handlerName);
                Class<?> interceptorClass=ReflectResolver.loadClass(interceptorName);

                try {
                    handler=(WebSocketHandler) ReflectResolver.getInstance(handlerClass);
                } catch (IllegalAccessException e) {

                }
                try {
                    interceptor=(HandshakeInterceptor)ReflectResolver.getInstance(interceptorClass);
                } catch (IllegalAccessException e) {

                }
            }

            if(handler==null){
                log.warn("websocket handler not found bean with name:"+handlerName+" in id:"+entry.getKey());
                continue;
            }

            if(interceptor==null){
                log.warn("websocket interceptor not found bean with name:"+interceptorName+" in id:"+entry.getKey());
            }

            registry.addHandler(handler,path)
                    .setAllowedOrigins(allowOrigin)
                    .addInterceptors(interceptor);
            log.info("websocket registry find:"+handlerName+" with path:"+path);
        }
    }


}
