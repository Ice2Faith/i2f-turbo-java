package i2f.springboot.limit.filter;

import i2f.spring.web.mapping.MappingUtil;
import i2f.springboot.limit.core.LimitManager;
import i2f.springboot.limit.properties.LimitRuleProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author Ice2Faith
 * @date 2025/11/11 17:50
 */
@ConditionalOnExpression("${i2f.springboot.limit.filter.enable:true}")
@Data
@NoArgsConstructor
@Configuration
public class LimitWebMvcConfigurer  {
    @Autowired
    private LimitManager limitManager;
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    @Autowired
    private LimitRuleProperties properties;

    @Bean
    public FilterRegistrationBean<LimitFilter> limitFilterFilterRegistrationBean(){
        FilterRegistrationBean<LimitFilter> bean=new FilterRegistrationBean<>();
        String pattern=properties.getFilter().getPattern();
        if(pattern==null){
            pattern="";
        }
        pattern=pattern.trim();
        if(pattern.isEmpty()){
            pattern="/*";
        }
        properties.getFilter().setPattern(pattern);
        bean.addUrlPatterns(pattern);
        bean.setOrder(properties.getFilter().getOrder());
        LimitFilter filter = new LimitFilter();
        filter.setManager(limitManager);
        filter.setMappingUtil(new MappingUtil(requestMappingHandlerMapping));
        bean.setFilter(filter);
        return bean;
    }

}
