package i2f.extension.embedding;

import i2f.ai.std.rag.RagEmbeddingModel;
import i2f.ai.std.rag.RagVector;
import i2f.extension.embedding.core.EmbeddingAndTokenCount;
import i2f.extension.embedding.core.OnnxBertBiEncoder;
import i2f.extension.embedding.core.PoolingMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Ice2Faith
 * @date 2026/9/30 16:14
 * @desc
 */
public class OnnxRagEmbeddingModel implements RagEmbeddingModel, AutoCloseable {
    protected OnnxBertBiEncoder model;

    public OnnxRagEmbeddingModel(OnnxBertBiEncoder model) {
        this.model = model;
    }

    public static OnnxRagEmbeddingModel of(OnnxBertBiEncoder model) {
        return new OnnxRagEmbeddingModel(model);
    }

    public static OnnxRagEmbeddingModel of(InputStream model, InputStream tokenizer, PoolingMode poolingMode) {
        return new OnnxRagEmbeddingModel(OnnxBertBiEncoder.of(model, tokenizer, poolingMode));
    }

    public static OnnxRagEmbeddingModel ofClasspath(String model, String tokenizer, PoolingMode poolingMode) {
        return new OnnxRagEmbeddingModel(OnnxBertBiEncoder.ofClasspath(model, tokenizer, poolingMode));
    }

    public static OnnxRagEmbeddingModel ofFile(File model, File tokenizer, PoolingMode poolingMode) throws IOException {
        return new OnnxRagEmbeddingModel(OnnxBertBiEncoder.ofFile(model, tokenizer, poolingMode));
    }

    @Override
    public RagVector embedAsVector(String content) {
        EmbeddingAndTokenCount embed = model.embed(content);
        return RagVector.fromFloatArray(embed.getEmbedding());
    }

    @Override
    public void close() throws Exception {
        if (this.model != null) {
            this.model.close();
        }
    }
}
