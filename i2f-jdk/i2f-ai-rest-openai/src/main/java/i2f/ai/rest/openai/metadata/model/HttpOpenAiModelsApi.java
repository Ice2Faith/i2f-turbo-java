package i2f.ai.rest.openai.metadata.model;

import i2f.ai.rest.openai.rag.data.HttpOpenAiEmbeddingReqDto;
import i2f.ai.rest.openai.rag.data.HttpOpenAiEmbeddingRespDto;
import i2f.net.http.consts.HttpMethodConstants;
import i2f.net.http.data.HttpHeaders;
import i2f.net.http.rest.IRestClient;
import i2f.net.http.rest.data.RestHttpRequest;
import i2f.net.http.rest.data.RestHttpResponse;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2026/6/25 17:34
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class HttpOpenAiModelsApi {
    protected IRestClient restClient;
    protected String baseUrl;
    protected String apiKey;

    public String getModelsUrl() {
        String ret = baseUrl;
        if (ret.endsWith("/")) {
            ret = ret.substring(0, ret.length() - 1);
        }
        return ret + "/models";
    }


    public HttpOpenAiEmbeddingRespDto doHttpPost(HttpOpenAiEmbeddingReqDto req) {
        try {
            RestHttpResponse<HttpOpenAiEmbeddingRespDto> resp = restClient.rest(RestHttpRequest.builder()
                            .url(getModelsUrl())
                            .method(HttpMethodConstants.POST)
                            .headers(HttpHeaders.create()
                                    .apply(headers -> {
                                        if (apiKey != null && !apiKey.isEmpty()) {
                                            headers.add("Authorization", "Bearer " + apiKey);
                                        }
                                    })
                            )
                            .body(req)
                            .build(),
                    HttpOpenAiEmbeddingRespDto.class);
            HttpOpenAiEmbeddingRespDto ret = resp.getBody();
            return ret;
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
