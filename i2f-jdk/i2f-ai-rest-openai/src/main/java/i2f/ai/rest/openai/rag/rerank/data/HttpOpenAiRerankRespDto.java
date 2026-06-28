package i2f.ai.rest.openai.rag.rerank.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/6/24 17:00
 * @desc
 */
@Data
@NoArgsConstructor
public class HttpOpenAiRerankRespDto {
    protected String id;
    protected String object;
    protected String created;
    protected String model;
    protected List<OpenAiRerankResult> results;
    protected OpenAiRerankUsage usage;
}
