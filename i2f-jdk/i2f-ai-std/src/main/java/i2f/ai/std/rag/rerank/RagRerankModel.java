package i2f.ai.std.rag.rerank;


import i2f.ai.std.rag.rerank.data.RagRerankDocument;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/4/9 8:48
 * @desc
 */
public interface RagRerankModel {
    List<RagRerankDocument> rerank(String question, List<String> contents, int topN);
}
