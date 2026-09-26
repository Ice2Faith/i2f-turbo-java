package i2f.ai.rest.openai.rag.rerank;

import i2f.ai.rest.openai.rag.rerank.data.HttpOpenAiRerankReqDto;
import i2f.ai.rest.openai.rag.rerank.data.HttpOpenAiRerankRespDto;
import i2f.ai.rest.openai.rag.rerank.data.OpenAiRerankResult;
import i2f.ai.rest.openai.rag.rerank.data.OpenAiRerankResultDocument;
import i2f.ai.std.rag.rerank.RagRerankModel;
import i2f.ai.std.rag.rerank.data.RagRerankDocument;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/6/25 8:57
 * @desc
 */
@Data
@NoArgsConstructor
public class HttpOpenAiRagRerankModel implements RagRerankModel, BaseMutator<HttpOpenAiRagRerankModel> {
    protected IRestClient restClient = new HttpProcessorRestClient();
    protected String baseUrl;
    protected String apiKey;
    protected String model;

    public String getRerankUrl() {
        String ret = baseUrl;
        if (ret.endsWith("/")) {
            ret = ret.substring(0, ret.length() - 1);
        }
        return ret + "/rerank";
    }

    @Override
    public List<RagRerankDocument> rerank(String question, List<String> contents, int topN) {
        HttpOpenAiRerankReqDto req = new HttpOpenAiRerankReqDto();
        req.setModel(model);
        req.setQuery(question);
        req.setDocuments(contents);
        req.setReturn_documents(false);
        req.setTop_n(topN);

        HttpOpenAiRerankRespDto resp = rerank(req);
        if (resp == null) {
            throw new IllegalStateException("not rerank data found!");
        }

        List<OpenAiRerankResult> results = resp.getResults();
        if (results == null || results.isEmpty()) {
            throw new IllegalStateException("not rerank data found!");
        }

        List<RagRerankDocument> ret = new ArrayList<>();

        for (OpenAiRerankResult item : results) {
            RagRerankDocument doc = new RagRerankDocument();
            doc.setIndex(item.getIndex());
            OpenAiRerankResultDocument rdoc = item.getDocument();
            if (rdoc != null && rdoc.getText() != null) {
                doc.setText(rdoc.getText());
            } else {
                String text = contents.get(item.getIndex());
                doc.setText(text);
            }
            doc.setScore(item.getRelevance_score());

            ret.add(doc);
        }

        return ret;
    }

    public HttpOpenAiRerankRespDto rerank(HttpOpenAiRerankReqDto req) {
        try {
            RestHttpResponse<HttpOpenAiRerankRespDto> resp = restClient.rest(new RestHttpRequest().toMutator()
                            .set(u -> u::setUrl, getRerankUrl())
                            .set(u -> u::setMethod, HttpMethodConstants.POST)
                            .set(u -> u::setHeaders, HttpHeaders.create()
                                    .apply(headers -> {
                                        if (apiKey != null && !apiKey.isEmpty()) {
                                            headers.add("Authorization", "Bearer " + apiKey);
                                        }
                                    })
                            )
                            .set(u -> u::setBody, req)
                            .done(),
                    HttpOpenAiRerankRespDto.class);
            HttpOpenAiRerankRespDto ret = resp.getBody();
            return ret;
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
