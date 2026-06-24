package i2f.ai.rest.openai.rag;

import i2f.ai.rest.openai.rag.data.HttpOpenAiEmbeddingReqDto;
import i2f.ai.rest.openai.rag.data.HttpOpenAiEmbeddingRespDto;
import i2f.ai.rest.rest.IRestClient;
import i2f.ai.rest.rest.data.HttpHeaders;
import i2f.ai.rest.rest.data.RestHttpRequest;
import i2f.ai.rest.rest.data.RestHttpResponse;
import i2f.ai.std.rag.RagEmbeddingModel;
import i2f.ai.std.rag.RagVector;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.IOException;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/6/24 17:19
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class HttpOpenAiRagEmbeddingModel implements RagEmbeddingModel {
    protected IRestClient restClient;
    protected String baseUrl;
    protected String apiKey;
    protected String model;

    public String getEmbeddingUrl() {
        String ret = baseUrl;
        if (ret.endsWith("/")) {
            ret = ret.substring(0, ret.length() - 1);
        }
        return ret + "/embeddings";
    }

    @Override
    public RagVector embedAsVector(String content) {
        List<RagVector> list = embedAllAsVector(Collections.singletonList(content));
        return list.get(0);
    }

    @Override
    public List<RagVector> embedAllAsVector(Collection<String> content) {
        HttpOpenAiEmbeddingReqDto req = new HttpOpenAiEmbeddingReqDto();
        req.setModel(model);
        req.setInput(new ArrayList<>(content));

        HttpOpenAiEmbeddingRespDto resp = doHttpPost(req);
        if (resp == null) {
            throw new IllegalStateException("not embedding data found!");
        }
        List<HttpOpenAiEmbeddingRespDto.EmbeddingResult> data = resp.getData();
        if (data == null) {
            throw new IllegalStateException("not embedding data found!");
        }
        Map<Integer, RagVector> map = new TreeMap<>();
        for (HttpOpenAiEmbeddingRespDto.EmbeddingResult item : data) {
            map.put(item.getIndex(), RagVector.fromList(item.getEmbedding()));
        }
        List<RagVector> ret = new ArrayList<>();
        int index = 0;
        for (Map.Entry<Integer, RagVector> entry : map.entrySet()) {
            Integer i = entry.getKey();
            if (i != index) {
                throw new IllegalStateException("embedding item [" + index + "] not embedded.");
            }
            ret.add(entry.getValue());
            index++;
        }
        return ret;
    }

    public HttpOpenAiEmbeddingRespDto doHttpPost(HttpOpenAiEmbeddingReqDto req) {
        try {
            RestHttpResponse<HttpOpenAiEmbeddingRespDto> resp = restClient.rest(RestHttpRequest.builder()
                            .url(getEmbeddingUrl())
                            .method(RestHttpRequest.POST)
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
