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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * @author Ice2Faith
 * @date 2025/1/9 9:45
 */
@Data
@Order(-1)
@ControllerAdvice
public class SwlDecryptionRequestBodyAdvice extends RequestBodyAdviceAdapter {
    protected SwlExchanger transfer = new SwlExchanger();
    protected SwlCertManager certManager = new SwlResourceCertManager();
    protected SwlAdviceConfig config = new SwlAdviceConfig();
    protected IJsonSerializer jsonSerializer = new JacksonJsonSerializer();

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        Method method = methodParameter.getMethod();
        if (method == null) {
            return false;
        }
        SecureParams ann = method.getAnnotation(SecureParams.class);
        if (ann == null || !ann.in()) {
            return false;
        }
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        String json = StreamUtils.copyToString(inputMessage.getBody(), StandardCharsets.UTF_8);
        SwlData request = (SwlData) jsonSerializer.deserialize(json, SwlData.class);
        SwlCert cert = getServerCert();
        SwlData receiveData = transfer.receiveByCert(request, cert);
        String content = receiveData.getParts().get(0);
        byte[] body = content.getBytes(StandardCharsets.UTF_8);
        return new HttpInputMessage() {
            @Override
            public InputStream getBody() throws IOException {
                return new ByteArrayInputStream(body);
            }

            @Override
            public HttpHeaders getHeaders() {
                return inputMessage.getHeaders();
            }
        };
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
