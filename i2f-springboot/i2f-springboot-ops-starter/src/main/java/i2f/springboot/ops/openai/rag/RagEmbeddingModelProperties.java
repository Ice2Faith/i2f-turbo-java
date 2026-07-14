package i2f.springboot.ops.openai.rag;

import i2f.ai.std.rag.RagHelper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Ice2Faith
 * @date 2026/7/3 11:40
 * @desc
 */
@ConfigurationProperties(prefix = "ai.rags.embedding")
@Data
@NoArgsConstructor
public class RagEmbeddingModelProperties {
    protected String baseUrl;
    protected String apiKey;
    protected String model;
    protected int dimension;
    protected boolean enableLoadDocs = false;
    protected String docsPath = RagHelper.DEFAULT_RAG_DIR;
    protected int maxSegmentSizeInChars = 512;
    protected int docsEmbedBatchSize = -1;
    protected double maxOverlapRate = 0.15;
    protected boolean enableMarkitdownDocReader = false;
    protected boolean enablePandocDocReader = false;
}
