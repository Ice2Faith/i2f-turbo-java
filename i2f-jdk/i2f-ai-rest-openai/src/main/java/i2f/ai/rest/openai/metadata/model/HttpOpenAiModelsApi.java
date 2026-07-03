package i2f.ai.rest.openai.metadata.model;

import i2f.ai.rest.openai.metadata.model.data.OpenAiModelsRespDto;
import i2f.mutator.BaseMutator;
import i2f.net.http.consts.HttpMethodConstants;
import i2f.net.http.data.HttpHeaders;
import i2f.net.http.rest.IRestClient;
import i2f.net.http.rest.data.RestHttpRequest;
import i2f.net.http.rest.data.RestHttpResponse;
import i2f.net.http.rest.impl.HttpProcessorRestClient;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2026/6/25 17:34
 * @desc
 */
@Data
@NoArgsConstructor
public class HttpOpenAiModelsApi implements BaseMutator<HttpOpenAiModelsApi> {
    protected IRestClient restClient = new HttpProcessorRestClient();
    protected String baseUrl;
    protected String apiKey;

    public String getModelsUrl() {
        String ret = baseUrl;
        if (ret.endsWith("/")) {
            ret = ret.substring(0, ret.length() - 1);
        }
        return ret + "/models";
    }


    public OpenAiModelsRespDto models() {
        try {
            RestHttpResponse<OpenAiModelsRespDto> resp = restClient.rest(new RestHttpRequest().toMutator()
                            .set(u -> u::setUrl, getModelsUrl())
                            .set(u -> u::setMethod, HttpMethodConstants.GET)
                            .set(u -> u::setHeaders, HttpHeaders.create()
                                    .apply(headers -> {
                                        if (apiKey != null && !apiKey.isEmpty()) {
                                            headers.add("Authorization", "Bearer " + apiKey);
                                        }
                                    })
                            )
                            .done(),
                    OpenAiModelsRespDto.class);
            OpenAiModelsRespDto ret = resp.getBody();
            return ret;
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
