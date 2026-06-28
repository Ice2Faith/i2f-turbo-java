package i2f.spring.web.rest;

import i2f.io.stream.StreamUtil;
import i2f.net.http.data.HttpRequest;
import i2f.net.http.data.MultipartFile;
import i2f.reflect.ReflectResolver;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/6/25 11:12
 * @desc
 */
@Data
@NoArgsConstructor
public class SpringWebAutoHttpRequestBodyHandler implements ISpringWebHttpRequestBodyHandler {
    protected RestTemplate restTemplate;

    public SpringWebAutoHttpRequestBodyHandler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void writeBody(Object data, HttpRequest request, ClientHttpRequest output, Object... args) throws IOException {
        List<MultipartFile> files = request.getFiles();
        if (files != null && !files.isEmpty()) {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            // 表单字段
            if (data != null) {
                if (data instanceof Map) {
                    Map<?, ?> dataMap = (Map<?, ?>) data;
                    for (Map.Entry<?, ?> entry : dataMap.entrySet()) {
                        body.add(String.valueOf(entry.getKey()), entry.getValue());
                    }
                } else {
                    Map<String, Object> dataMap = new LinkedHashMap<>();
                    ReflectResolver.bean2map(data, dataMap);
                    for (Map.Entry<?, ?> entry : dataMap.entrySet()) {
                        body.add(String.valueOf(entry.getKey()), entry.getValue());
                    }
                }
            }
            // 文件字段
            for (MultipartFile file : files) {
                body.add(file.getName(), new InputStreamResource(file.getInputStream(), file.getFileName()) {
                    @Override
                    public String getFilename() {
                        return file.getFileName();
                    }
                });
            }

            HttpEntity<MultiValueMap<String, Object>> reqEntity = new HttpEntity<>(body);
            data = reqEntity;
        }
        if (data == null) {
            return;
        }
        if (data instanceof byte[]) {
            byte[] bytes = (byte[]) data;
            OutputStream os = output.getBody();
            os.write(bytes);
            os.flush();
            return;
        }
        if (data instanceof InputStream) {
            InputStream is = (InputStream) data;
            OutputStream os = output.getBody();
            StreamUtil.streamCopy(is, os, false, true);
            os.flush();
            return;
        }

        Class<?> dataClass = data.getClass();
        Type dataType = data instanceof RequestEntity ? ((RequestEntity) data).getType() : dataClass;

        MediaType contentType = output.getHeaders().getContentType();
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        for (HttpMessageConverter converter : messageConverters) {
            if (converter instanceof GenericHttpMessageConverter) {
                GenericHttpMessageConverter messageConverter = (GenericHttpMessageConverter) converter;
                if (messageConverter.canWrite(dataType, dataClass, contentType)) {
                    messageConverter.write(data, dataType, contentType, output);
                    return;
                }
            } else {
                if (converter.canWrite(dataClass, contentType)) {
                    converter.write(data, contentType, output);
                    return;
                }
            }
        }

        String message = "No HttpMessageConverter for " + dataClass.getName();
        if (contentType != null) {
            message = message + " and content type \"" + contentType + "\"";
        }

        throw new RestClientException(message);

    }
}
