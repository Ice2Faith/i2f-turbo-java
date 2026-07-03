package i2f.ai.std.rag.data;

import i2f.builder.BaseBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/7/3 11:11
 * @desc
 */
@Data
@NoArgsConstructor
public class RagSearchResultItem implements BaseBuilder<RagSearchResultItem> {
    protected int rank;
    protected String content;
}
