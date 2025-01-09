package i2f.spring.swl.advice;

import i2f.extension.jackson.serializer.JacksonJsonSerializer;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.swl.annotation.SecureParams;
import i2f.swl.cert.SwlCertManager;
import i2f.swl.cert.data.SwlCert;
import i2f.swl.cert.impl.SwlResourceCertManager;
import i2f.swl.core.exchanger.SwlExchanger;
import i2f.swl.data.SwlData;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2025/1/9 9:38
 */
@Data
@Order(-1)
@ControllerAdvice
public class SwlEncryptionResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    protected SwlExchanger transfer = new SwlExchanger();
    protected SwlCertManager certManager = new SwlResourceCertManager();
    protected SwlAdviceConfig config = new SwlAdviceConfig();
    protected IJsonSerializer jsonSerializer = new JacksonJsonSerializer();

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        Method method = returnType.getMethod();
        if (method == null) {
            return false;
        }
        SecureParams ann = method.getAnnotation(SecureParams.class);
        if (ann == null || !ann.out()) {
            return false;
        }
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        SwlCert cert = getServerCert();
        String json = jsonSerializer.serialize(body);
        SwlData responseData = transfer.sendByCert(cert, Arrays.asList(json));
        responseData.setContext(null);
        responseData.setAttaches(null);
        if (body instanceof String) {
            return jsonSerializer.serialize(responseData);
        } else {
            return responseData;
        }
    }

    public SwlCert getServerCert() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attrs.getRequest();
        String certId = request.getHeader(config.getHeaderName());
        if (StringUtils.isEmpty(certId)) {
            certId = request.getParameter(config.getHeaderName());
        }
        return certManager.loadServer(certId);
    }

}
