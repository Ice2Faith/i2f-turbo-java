package i2f.spring.web.rest;

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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/6/24 20:13
 * @desc
 */
@Data
@NoArgsConstructor
public class SpringWebRestClient implements IRestClient {
    protected RestTemplate restTemplate;

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
        MultiValueMap<String, String> reqHeaders = new LinkedMultiValueMap<>();
        HttpHeaders rawHeaders = request.getHeaders();
        for (Map.Entry<String, ArrayList<String>> entry : rawHeaders.entrySet()) {
            reqHeaders.addAll(entry.getKey(), entry.getValue());
        }
        HttpEntity<Object> reqEntity = new HttpEntity<>(request.getBody(), reqHeaders);
        ResponseEntity<T> respEntity = restTemplate.exchange(url,
                HttpMethod.resolve(rawMethod.toUpperCase()),
                reqEntity,
                responseType);

        return (RestHttpResponse<T>) RestHttpResponse.builder()
                .statusCode(respEntity.getStatusCodeValue())
                .statusMessage(respEntity.getStatusCode().getReasonPhrase())
                .headers(HttpHeaders.create()
                        .addAll(respEntity.getHeaders())
                )
                .body(respEntity.getBody())
                .build();
    }
}
