package i2f.ai.std.rag;

import i2f.ai.std.rag.data.RagDocument;

import java.util.Collection;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/11/9 16:09
 * @desc 按文章+分片管理，适用于关系性数据库
 */
public interface RagDocumentsStore {
    List<String> getDocumentAllIds(String documentId);

    void removeDocuments(String documentId);

    List<RagDocument> getDocumentsByIds(Collection<String> ids);

    void saveAll(Collection<RagDocument> documents);

    List<RagDocument> nearbyDocuments(String documentId, int separateNoStart, int separateNoEnd);
}
