package i2f.ai.std.rag.data;

import i2f.mutator.BaseMutator;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/7/3 11:11
 * @desc
 */
@Data
@NoArgsConstructor
public class RagSearchResultItem implements BaseMutator<RagSearchResultItem> {
    protected String id;
    protected int rank;
    protected String content;
}
