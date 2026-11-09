package i2f.ai.std.rag;

import i2f.ai.std.mcp.server.McpServerExpose;
import i2f.ai.std.rag.data.RagDocument;
import i2f.ai.std.rag.data.RagSearchResultItem;
import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.mutator.BaseMutator;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2026/3/25 15:09
 * @desc
 */
@McpServerExpose
@Data
@NoArgsConstructor
@Tools(tags = {
        AiTags.RAG_VALUE
})
public class RagTools implements BaseMutator<RagTools> {
    public static final String RAG_SEARCH = "rag_search";
    public static final String RAG_NEARBY = "rag_nearby";

    protected RagWorker worker;

    public RagTools(RagWorker worker) {
        this.worker = worker;
    }

    @Tool(value = RAG_SEARCH,
            tags = {
            AiTags.READONLY_VALUE
    }, description = "search relative documents from knowledge(include internal docs, organ info etc.) base by similar query. Note: work on system level.")
    public Map<String, Object> rag_search(@ToolParam(value = "text", description = "the query content")
                                                String text,
                                                @ToolParam(value = "topN", description = "max returns count, cloud be null, default is 3, suggest lower than 10, for example 3 or 5")
                                                Integer topN) {
        List<RagSearchResultItem> ret = new ArrayList<>();
        if (topN == null) {
            topN = 3;
        }
        List<RagEmbedding> list = worker.similar(text, topN);
        for (int i = 0; i < list.size(); i++) {
            RagEmbedding rag = list.get(i);

            RagSearchResultItem item = new RagSearchResultItem();
            item.setId(rag.getId());
            item.setRank(i + 1);
            item.setContent(rag.getContent());
            ret.add(item);
        }

        if (!ret.isEmpty()) {
            RagDocumentsStore documentsStore = worker.getDocumentsStore();
            if (documentsStore != null) {
                List<String> ids = ret.stream().map(e -> e.getId()).collect(Collectors.toList());
                List<RagDocument> docs = documentsStore.getDocumentsByIds(ids);
                Map<String, RagDocument> map = new HashMap<>();
                for (RagDocument doc : docs) {
                    map.put(doc.getId(), doc);
                }

                for (RagSearchResultItem item : ret) {
                    RagDocument doc = map.get(item.getId());
                    if (doc != null) {
                        item.setDocumentId(doc.getDocumentId());
                        item.setSeparateNo(doc.getSeparateNo());
                    }
                }
            }
        }

        Map<String, Object> output = new HashMap<>();
        output.put("results", ret);
        output.put("hints", "you can use tool `" + RAG_NEARBY + "` to got other chunks(separates) in one document, using `documentId` and `separateNo`");
        return output;
    }

    @Tool(value = RAG_NEARBY,
            tags = {
                    AiTags.READONLY_VALUE
            }, description = "retrieve nearby/adjacent chunks (separates) of a document by `documentId` and a chunk-number range. Note: `documentId` should from `" + RAG_SEARCH + "`.")
    public List<RagDocument> rag_nearby(@ToolParam(value = "documentId", description = "the documentId from `" + RAG_SEARCH + "` result")
                                        String documentId,
                                        @ToolParam(value = "separateNoStart", description = "document separate start no, for example 0 or 1")
                                        Integer separateNoStart,
                                        @ToolParam(value = "separateNoStart", description = "document separate end no, for example 3 or 5")
                                        Integer separateNoEnd) {
        RagDocumentsStore documentsStore = worker.getDocumentsStore();
        if (documentsStore == null) {
            throw new IllegalStateException("system not configure(enabled) nearby document separates retrieve feature!");
        }
        if (separateNoStart == null) {
            separateNoStart = -1;
        }
        if (separateNoEnd == null) {
            separateNoEnd = 99;
        }
        List<RagDocument> list = documentsStore.nearbyDocuments(documentId, separateNoStart, separateNoEnd);

        return list;
    }
}
