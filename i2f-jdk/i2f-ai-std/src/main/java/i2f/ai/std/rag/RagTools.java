package i2f.ai.std.rag;

import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/3/25 15:09
 * @desc
 */
@Data
@NoArgsConstructor
@Tools
public class RagTools {
    protected RagWorker worker;

    public RagTools(RagWorker worker) {
        this.worker = worker;
    }

    @Tool(description = "获取与文本内容具有高相关性的文档资料")
    public String ragSearch(@ToolParam(description = "文本内容") String text,
                            @ToolParam(description = "最多返回的条数，一般建议小于10，常用 3 或者 5") int topN) {
        List<RagEmbedding> list = worker.similar(text, topN);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            RagEmbedding rag = list.get(i);
            if (i > 0) {
                builder.append("\n");
            }
            builder.append("------------------------------------------").append("\n");
            builder.append("## 相关度排名：").append(i + 1).append("\n");
            builder.append("### 正文如下").append("\n");
            builder.append(rag.getContent());
        }

        return builder.toString();
    }
}
