package i2f.springboot.trace.mdc.httpclient;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * RestTemplate 客户端的 MDC 请求头传递
 *
 * @author Ice2Faith
 * @date 2026/4/15 9:08
 * @desc
 */
@Data
@ConditionalOnExpression("${i2f.springboot.trace.mdc.rest-template.enable:true}")
@AutoConfigureAfter(MdcHttpClientInterceptor.class)
@ConditionalOnClass(RestTemplate.class)
public class MdcRestTemplateInterceptorProcessor implements BeanPostProcessor {

    protected MdcHttpClientInterceptor interceptor;

    public MdcRestTemplateInterceptorProcessor(@Autowired(required = false) MdcHttpClientInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof RestTemplate) {
            RestTemplate restTemplate = (RestTemplate) bean;
            List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
            if (this.interceptor != null) {
                interceptors.add(this.interceptor);
            }
        }
        return bean;
    }

}
