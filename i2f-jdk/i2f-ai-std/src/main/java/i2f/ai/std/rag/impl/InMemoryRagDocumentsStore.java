package i2f.ai.std.rag.impl;

import i2f.ai.std.rag.RagDocumentsStore;
import i2f.ai.std.rag.data.RagDocument;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2026/11/9 16:26
 * @desc
 */
@Data
@NoArgsConstructor
public class InMemoryRagDocumentsStore implements RagDocumentsStore {
    // documentId,List
    protected final Map<String, CopyOnWriteArrayList<RagDocument>> cache = new ConcurrentHashMap<>();

    @Override
    public List<String> getDocumentAllIds(String documentId) {
        List<String> ret = new ArrayList<>();
        if (documentId == null) {
            return ret;
        }
        CopyOnWriteArrayList<RagDocument> list = cache.get(documentId);
        if (list == null) {
            return ret;
        }
        for (RagDocument item : list) {
            ret.add(item.getId());
        }
        return ret;
    }

    @Override
    public void removeDocuments(String documentId) {
        if (documentId == null) {
            return;
        }
        cache.remove(documentId);
    }

    @Override
    public List<RagDocument> getDocumentsByIds(Collection<String> ids) {
        List<RagDocument> ret = new ArrayList<>();
        for (Map.Entry<String, CopyOnWriteArrayList<RagDocument>> entry : cache.entrySet()) {
            CopyOnWriteArrayList<RagDocument> value = entry.getValue();
            if (value == null) {
                continue;
            }
            for (RagDocument item : value) {
                ret.add(item.copy());
            }
        }

        return ret;
    }

    @Override
    public void saveAll(Collection<RagDocument> documents) {
        if (documents == null) {
            return;
        }
        for (RagDocument item : documents) {
            CopyOnWriteArrayList<RagDocument> list = cache.computeIfAbsent(item.getDocumentId(), k -> new CopyOnWriteArrayList<>());
            list.add(item);
        }
    }

    @Override
    public List<RagDocument> nearbyDocuments(String documentId, int separateNoStart, int separateNoEnd) {
        List<RagDocument> ret = new ArrayList<>();
        if (documentId == null) {
            return ret;
        }
        CopyOnWriteArrayList<RagDocument> list = cache.get(documentId);
        if (list == null) {
            return ret;
        }
        for (RagDocument item : list) {
            if (item.getSeparateNo() != null) {
                if (item.getSeparateNo() >= separateNoStart && item.getSeparateNo() <= separateNoEnd) {
                    ret.add(item);
                }
            }
        }
        ret.sort(Comparator.comparing(RagDocument::getSeparateNo));
        return ret;
    }
}
