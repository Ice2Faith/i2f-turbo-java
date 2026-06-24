package i2f.spring.web.openai.rag.impl;

import i2f.ai.rest.openai.rag.HttpOpenAiRagEmbeddingModel;
import i2f.ai.rest.openai.rag.data.HttpOpenAiEmbeddingReqDto;
import i2f.ai.rest.openai.rag.data.HttpOpenAiEmbeddingRespDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @author Ice2Faith
 * @date 2026/6/24 19:31
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class SpringHttpOpenAiRagEmbeddingModel extends HttpOpenAiRagEmbeddingModel {
    protected RestTemplate restTemplate;

    @Override
    public HttpOpenAiEmbeddingRespDto doHttpPost(HttpOpenAiEmbeddingReqDto req) {
        String url = getEmbeddingUrl();
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        if (apiKey != null && !apiKey.isEmpty()) {
            headers.add("Authorization", "Bearer " + apiKey);
        }
        HttpEntity<HttpOpenAiEmbeddingReqDto> reqEntity = new HttpEntity<>(req, headers);
        ResponseEntity<HttpOpenAiEmbeddingRespDto> respEntity = restTemplate.exchange(url, HttpMethod.POST, reqEntity, HttpOpenAiEmbeddingRespDto.class);
        HttpOpenAiEmbeddingRespDto resp = respEntity.getBody();
        return resp;
    }
}
