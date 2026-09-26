package i2f.spring.web.rest;

import i2f.mutator.BaseMutator;
import i2f.net.http.data.HttpHeaders;
import i2f.net.http.rest.IRestClient;
import i2f.net.http.rest.data.RestHttpRequest;
import i2f.net.http.rest.data.RestHttpResponse;
import i2f.url.FormUrlEncodedEncoder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2026/6/24 20:13
 * @desc
 */
@Data
@NoArgsConstructor
public class SpringWebRestClient implements IRestClient, BaseMutator<SpringWebRestClient> {
    protected RestTemplate restTemplate;

    public SpringWebRestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public <T> RestHttpResponse<T> rest(RestHttpRequest request, Class<T> responseType) throws IOException {
        String rawMethod = request.getMethod();
        Object rawParams = request.getParams();
        String url = request.getUrl();
        if (rawParams != null) {
            String form = FormUrlEncodedEncoder.toForm(rawParams);
            if (url.contains("?")) {
                if (url.endsWith("&")) {
                    url = url + form;
                } else {
                    url = url + "&" + form;
                }
            } else {
                url = url + "?" + form;
            }
        }
        org.springframework.http.HttpHeaders reqHeaders = new org.springframework.http.HttpHeaders();
        HttpHeaders rawHeaders = request.getHeaders();
        if (rawHeaders != null) {
            for (Map.Entry<String, ArrayList<String>> entry : rawHeaders.entrySet()) {
                reqHeaders.addAll(entry.getKey(), entry.getValue());
            }
        }
        HttpEntity<Object> reqEntity = new HttpEntity<>(request.getBody(), reqHeaders);
        ResponseEntity<T> respEntity = restTemplate.exchange(url,
                HttpMethod.valueOf(rawMethod.toUpperCase()),
                reqEntity,
                responseType);

        return new RestHttpResponse<T>().toMutator()
                .set(u -> u::setStatusCode, respEntity.getStatusCode().value())
                .set(u -> u::setStatusMessage, String.valueOf(respEntity.getStatusCode()))
                .set(u -> u::setHeaders, HttpHeaders.create()
                        .apply(headers -> {
                            org.springframework.http.HttpHeaders respHeaders = respEntity.getHeaders();
                            Set<String> names = respHeaders.headerNames();
                            for (String name : names) {
                                List<String> list = respHeaders.get(name);
                                headers.add(name, list);
                            }
                        })
                )
                .set(u -> u::setBody, respEntity.getBody())
                .done();
    }
}
