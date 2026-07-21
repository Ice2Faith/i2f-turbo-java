package i2f.springboot.ops.openai.rag;

import i2f.ai.std.rag.BucketRagEmbeddingStore;
import i2f.ai.std.rag.RagEmbedding;
import i2f.ai.std.rag.RagEmbeddingModel;
import i2f.ai.std.rag.data.RagSearchResultItem;
import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.ToolCallContextHolder;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.mutator.BaseMutator;
import i2f.springboot.ops.openai.data.OpenAiOperateDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.*;

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
public class MemoryTools implements BaseMutator<MemoryTools> {
    private static final Set<String> exposeTools;

    static {
        Set<String> names = new HashSet<>();
        Method[] methods = MemoryTools.class.getDeclaredMethods();
        for (Method method : methods) {
            Tool ann = method.getAnnotation(Tool.class);
            if (ann == null) {
                continue;
            }
            String name = method.getName();
            String value = ann.value();
            if (value != null && !value.isEmpty()) {
                name = value;
            }
            names.add(name);
        }
        exposeTools = Collections.unmodifiableSet(names);
    }

    public static Set<String> toolNames() {
        return new HashSet<>(exposeTools);
    }

    public static final String BUCKET_PUBLIC = "public";
    protected RagEmbeddingModel model;
    protected BucketRagEmbeddingStore embeddingStore;

    public MemoryTools(RagEmbeddingModel model, BucketRagEmbeddingStore embeddingStore) {
        this.model = model;
        this.embeddingStore = embeddingStore;
    }

    @Tool(tags = {
            AiTags.READONLY_VALUE
    }, description = "检索记忆")
    public List<RagSearchResultItem> memory_search(@ToolParam(description = "检索相关记忆内容") String text,
                                                   @ToolParam(description = "最多返回的条数，允许为 null 默认为 3，一般建议小于10，例如 3 或者 5") Integer topN) {
        List<RagSearchResultItem> ret = new ArrayList<>();
        if (topN == null) {
            topN = 3;
        }
        OpenAiOperateDto req = ToolCallContextHolder.get("req");
        List<String> buckets = new ArrayList<>();
        buckets.add(BUCKET_PUBLIC);
        String bucket = req.getMemoryBucket();
        if (bucket != null && !bucket.trim().isEmpty()) {
            buckets.add(bucket.trim());
        }
        RagEmbedding embedding = model.embed(text);
        List<RagEmbedding> list = embeddingStore.similar(embedding, topN, buckets);
        for (int i = 0; i < list.size(); i++) {
            RagEmbedding rag = list.get(i);

            RagSearchResultItem item = new RagSearchResultItem();
            item.setRank(i + 1);
            item.setContent(rag.getContent());
            ret.add(item);
        }

        return ret;
    }

    @Tool(tags = {
            AiTags.WRITABLE_VALUE
    }, description = "保存记忆内容")
    public String memory_save(@ToolParam(description = "记忆内容") String text) {
        OpenAiOperateDto req = ToolCallContextHolder.get("req");
        RagEmbedding embedding = model.embed(text);
        String bucket = req.getMemoryBucket();
        if (bucket != null) {
            bucket = bucket.trim();
        }
        if (bucket == null || bucket.isEmpty()) {
            bucket = BUCKET_PUBLIC;
        }
        String id = embeddingStore.store(embedding, bucket);
        return "stored memory with id=" + id + " at bucket=" + bucket;
    }
}
