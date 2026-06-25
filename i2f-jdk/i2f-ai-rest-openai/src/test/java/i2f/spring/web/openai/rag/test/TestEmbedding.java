package i2f.spring.web.openai.rag.test;

import i2f.ai.rest.openai.rag.HttpOpenAiRagEmbeddingModel;
import i2f.ai.std.rag.RagEmbedding;
import i2f.ai.std.rag.RagEmbeddingModel;
import i2f.ai.std.rag.RagEmbeddingStore;
import i2f.ai.std.rag.RagWorker;
import i2f.ai.std.rag.impl.InMemoryRagEmbeddingStore;
import i2f.ai.std.rag.impl.SimpleRecursiveRagTextSplitter;
import i2f.net.http.impl.HttpUrlConnectProcessor;
import i2f.net.http.rest.IRestClient;
import i2f.net.http.rest.impl.HttpProcessorRestClient;

import java.util.Arrays;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/6/24 16:59
 * @desc
 */
public class TestEmbedding {
    public static void main(String[] args) throws Exception {

        RagEmbeddingModel embeddingModel = null;
        IRestClient restClient = HttpProcessorRestClient.builder()
                .httpProcessor(new HttpUrlConnectProcessor())
                .build();

        if (embeddingModel == null) {
            embeddingModel = HttpOpenAiRagEmbeddingModel.builder()
                    .restClient(restClient)
                    .baseUrl("http://localhost:11434/v1")
                    .model("qwen3-embedding:0.6b")
                    .apiKey("xxx")
                    .build();
        }

        List<RagEmbedding> embeddings = embeddingModel.embedAll(null, Arrays.asList("我喜欢吃", "你喜欢玩"));

        RagEmbeddingStore store = new InMemoryRagEmbeddingStore();
        RagWorker worker = new RagWorker(embeddingModel, store);

        SimpleRecursiveRagTextSplitter splitter = new SimpleRecursiveRagTextSplitter();
        worker.loadDefaultDocuments(splitter);
        List<RagEmbedding> ret = worker.similar("ONGL用法", 3);
        System.out.println("ok");
    }
}
