package i2f.extension.ai.openai.rag;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.embeddings.CreateEmbeddingResponse;
import com.openai.models.embeddings.Embedding;
import com.openai.models.embeddings.EmbeddingCreateParams;
import i2f.ai.std.rag.RagEmbeddingModel;
import i2f.ai.std.rag.RagVector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/3/31 9:10
 * @desc
 */
public class OpenAiRagEmbeddingModel implements RagEmbeddingModel {
    public static final String OPENAI_DASHSCOPE_BASE_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1";

    public static final String DEFAULT_MODEL = "text-embedding-v4";
    public static final int DEFAULT_DIMENSION = 1024;

    protected OpenAIClient client;
    protected String model = DEFAULT_MODEL;
    protected int dimension = DEFAULT_DIMENSION;
    protected int onceMaxSize = 10;

    public OpenAiRagEmbeddingModel(OpenAIClient client) {
        this.client = client;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        protected OpenAIOkHttpClient.Builder builder = OpenAIOkHttpClient.builder();
        protected String model;

        public Builder baseUrl(String baseUrl) {
            builder.baseUrl(baseUrl);
            return this;
        }

        public Builder apiKey(String apiKey) {
            builder.apiKey(apiKey);
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public OpenAiRagEmbeddingModel build() {
            OpenAiRagEmbeddingModel ret = new OpenAiRagEmbeddingModel(builder.build());
            if (model != null && !model.isEmpty()) {
                ret.model(model);
            }
            return ret;
        }
    }

    public OpenAiRagEmbeddingModel model(String model) {
        this.model = model;
        return this;
    }

    @Override
    public RagVector embedAsVector(String content) {
        List<RagVector> list = embedOnce(Collections.singletonList(content));
        return list.get(0);
    }


    protected List<RagVector> embedOnce(List<String> list) {
        if (list.size() > onceMaxSize) {
            throw new IllegalStateException("open-ai once embedding max support " + onceMaxSize + " items");
        }
        List<RagVector> ret = new ArrayList<>();
        // 构建请求参数
        EmbeddingCreateParams params = EmbeddingCreateParams.builder()
                .inputOfArrayOfStrings(list)
                .dimensions(dimension)
                .model(model)
                .build();

        CreateEmbeddingResponse response = client.embeddings().create(params);
        List<Embedding> embeddings = response.data();

        for (Embedding embedding : embeddings) {
            ret.add(RagVector.fromFloatList(embedding.embedding()));
        }
        return ret;
    }


    @Override
    public List<RagVector> embedAllAsVector(Collection<String> content) {
        List<RagVector> ret = new ArrayList<>();
        List<String> once = new ArrayList<>();
        for (String item : content) {
            once.add(item);
            if (once.size() >= onceMaxSize) {
                List<RagVector> next = embedOnce(once);
                ret.addAll(next);
                once.clear();
            }
        }
        if (!once.isEmpty()) {
            List<RagVector> next = embedOnce(once);
            ret.addAll(next);
            once.clear();
        }
        return ret;
    }
}
