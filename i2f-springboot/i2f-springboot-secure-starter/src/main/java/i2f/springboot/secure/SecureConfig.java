package i2f.springboot.secure;


import i2f.cache.base.ICache;
import i2f.cache.expire.IExpireCache;
import i2f.cache.impl.container.MapCache;
import i2f.cache.impl.expire.ObjectExpireCacheWrapper;
import i2f.springboot.secure.consts.SecureConsts;
import i2f.springboot.secure.core.SecureTransferFilter;
import i2f.springboot.secure.data.SecureCtrl;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2022/6/29 15:50
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.secure.enable:true}")
@Data
@Configuration
@ConfigurationProperties(prefix = "i2f.springboot.config.secure")
public class SecureConfig {
    private String asymStorePath = "../";

    private String responseCharset = "UTF-8";

    private int asymKeySize = SecureConsts.RSA_KEY_SIZE_1024;

    private int symmKeySize = SecureConsts.AES_KEY_SIZE_128;

    private int nonceTimeoutSeconds = 6 * 60;

    private boolean enableNonceTimestampAllowDiff = true;
    private int nonceTimestampAllowDiffSeconds = 10 * 60;

    private boolean enableDynamicAsymKey = true;

    private long dynamicRefreshDelaySeconds = 6 * 60;

    private int dynamicMaxHistoriesCount = 5;

    private long clientKeyExpireSeconds = 30 * 60;

    private long clientKeyExpirePoolDelaySeconds = 3 * 60;

    private String headerName = SecureConsts.DEFAULT_SECURE_HEADER_NAME;

    private String headerSeparator = SecureConsts.DEFAULT_HEADER_SEPARATOR;

    private String dynamicKeyHeaderName = SecureConsts.SECURE_DYNAMIC_KEY_HEADER;
    private String clientKeyHeaderName = SecureConsts.SECURE_CLIENT_KEY_HEADER;

    private String clientAsymSignName = SecureConsts.DEFAULT_SECURE_CLIENT_ASYM_SIGN_NAME;

    // 客户端秘钥对的获取策略，是否是本地生成交换策略
    private boolean enableSwapAsymKey = SecureConsts.DEFAULT_SECURE_SWAP_ASYM_KEY;

    private boolean enableClientIpBind = true;

    private String encUrlPath = SecureConsts.DEFAULT_ENC_URL_PATH;

    private String parameterName = SecureConsts.DEFAULT_SECURE_PARAMETER_NAME;

    private SecureCtrl defaultControl = new SecureCtrl(true, true);

    private SecureWhiteListConfig whiteList;

    @Data
    @NoArgsConstructor
    public static class SecureWhiteListConfig {
        private Set<String> bothPattens;
        private Set<String> inPattens;
        private Set<String> outPattens;
    }

    @Autowired
    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    //    @Bean
    public FilterRegistrationBean<SecureTransferFilter> filterFilterRegistrationBean() {
        SecureTransferFilter filter = new SecureTransferFilter();
        autowireCapableBeanFactory.autowireBean(filter);
        FilterRegistrationBean<SecureTransferFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/**");
        registrationBean.setOrder(-1);
        registrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD);
        return registrationBean;
    }

    @ConditionalOnMissingBean(IExpireCache.class)
    @Bean
    public IExpireCache<String, Object> cache() {
        return new ObjectExpireCacheWrapper<>(new MapCache<>(new ConcurrentHashMap<>()));
    }

}
