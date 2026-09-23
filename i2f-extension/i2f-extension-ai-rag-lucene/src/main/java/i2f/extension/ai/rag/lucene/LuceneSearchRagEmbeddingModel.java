package i2f.extension.ai.rag.lucene;

import i2f.ai.std.rag.RagEmbeddingModel;
import i2f.ai.std.rag.RagVector;

/**
 * @author Ice2Faith
 * @date 2026/9/23 9:19
 * @desc 基于 lucene 的 rag 嵌入模型
 * 注意：lucene实现是没有向量概念的，向量是通过固定字符串Unicode转换得到的，没有实际的向量含义
 * 因此，只能固定搭配对应的 EmbeddingStore 才能正常工作
 */
public class LuceneSearchRagEmbeddingModel implements RagEmbeddingModel {
    public static final LuceneSearchRagEmbeddingModel INSTANCE = new LuceneSearchRagEmbeddingModel();

    @Override
    public RagVector embedAsVector(String content) {
        double[] arr = LuceneRagEmbeddingUtil.string2vector(content);
        return RagVector.fromArray(arr);
    }
}
