package i2f.ai.std.rag.rerank.data;

import i2f.builder.BaseBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/4/9 8:49
 * @desc
 */
@Data
@NoArgsConstructor
public class RagRerankDocument implements BaseBuilder<RagRerankDocument> {
    protected String text;
    protected double score;
    protected int index;
}
