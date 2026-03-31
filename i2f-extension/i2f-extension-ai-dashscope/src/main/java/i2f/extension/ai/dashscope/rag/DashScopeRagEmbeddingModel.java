package i2f.extension.ai.dashscope.rag;

import com.alibaba.dashscope.embeddings.*;
import com.alibaba.dashscope.exception.NoApiKeyException;
import i2f.ai.std.rag.RagEmbeddingModel;
import i2f.ai.std.rag.RagVector;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/3/31 8:41
 * @desc
 */
@Data
@NoArgsConstructor
public class DashScopeRagEmbeddingModel implements RagEmbeddingModel {
    public static final String OPENAI_DASHSCOPE_BASE_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1";

    public static final String DEFAULT_MODEL = "text-embedding-v4";
    public static final int DEFAULT_DIMENSION = 1024;
    protected String apiKey;
    protected String model = DEFAULT_MODEL;
    protected int dimension = DEFAULT_DIMENSION;
    protected int onceMaxSize = 10;

    public DashScopeRagEmbeddingModel apiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public DashScopeRagEmbeddingModel model(String model) {
        this.model = model;
        return this;
    }

    public DashScopeRagEmbeddingModel dimension(int dimension) {
        this.dimension = dimension;
        return this;
    }

    @Override
    public RagVector embedAsVector(String content) {
        List<RagVector> list = embedOnce(Collections.singletonList(content));
        return list.get(0);
    }

    protected List<RagVector> embedOnce(List<String> list) {
        try {
            if (list.size() > onceMaxSize) {
                throw new IllegalStateException("dashscope once embedding max support " + onceMaxSize + " items");
            }
            List<RagVector> ret = new ArrayList<>();
            // 构建请求参数
            TextEmbeddingParam param = TextEmbeddingParam
                    .builder()
                    .apiKey(apiKey)
                    .model(model)  // 使用text-embedding-v4模型
                    .texts(list)  // 输入文本
                    .parameter("dimension", DEFAULT_DIMENSION)  // 指定向量维度（仅 text-embedding-v3及 text-embedding-v4支持该参数）
                    .build();

            // 创建模型实例并调用
            TextEmbedding textEmbedding = new TextEmbedding();
            TextEmbeddingResult result = textEmbedding.call(param);

            Integer statusCode = result.getStatusCode();
            if (200 != statusCode) {
                String code = result.getCode();
                String message = result.getMessage();
                throw new IllegalStateException("dashscope embedding error: " + code + ", " + message);
            }

            TextEmbeddingOutput output = result.getOutput();
            List<TextEmbeddingResultItem> embeddings = output.getEmbeddings();
            if (embeddings.isEmpty()) {
                throw new IllegalStateException("dashscope embedding missing output");
            }
            for (TextEmbeddingResultItem embedding : embeddings) {
                ret.add(RagVector.fromList(embedding.getEmbedding()));
            }
            return ret;
        } catch (NoApiKeyException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
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
