package i2f.ai.std.rag;

import i2f.ai.std.rag.data.RagSearchResultItem;
import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.mutator.BaseMutator;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/3/25 15:09
 * @desc
 */
@Data
@NoArgsConstructor
@Tools(tags = {
        AiTags.RAG_VALUE
})
public class RagTools implements BaseMutator<RagTools> {
    protected RagWorker worker;

    public RagTools(RagWorker worker) {
        this.worker = worker;
    }

    @Tool(tags = {
            AiTags.READONLY_VALUE
    }, description = "search relative documents from knowledge(include internal docs, organ info etc.) base by similar query. Note: work on system level.")
    public List<RagSearchResultItem> rag_search(@ToolParam(value = "text", description = "the query content")
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
            item.setRank(i + 1);
            item.setContent(rag.getContent());
            ret.add(item);
        }

        return ret;
    }
}
