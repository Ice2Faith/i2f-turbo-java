package i2f.ai.std.rag.data;

import i2f.mutator.BaseMutator;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/11/9 16:13
 * @desc
 */
@Data
@NoArgsConstructor
public class RagDocument implements BaseMutator<RagDocument> {
    protected String id;
    protected String documentId;
    protected Integer separateNo;
    protected String content;

    public RagDocument copy() {
        RagDocument ret = new RagDocument();
        ret.setId(id);
        ret.setDocumentId(documentId);
        ret.setSeparateNo(separateNo);
        ret.setContent(content);
        return ret;
    }
}
