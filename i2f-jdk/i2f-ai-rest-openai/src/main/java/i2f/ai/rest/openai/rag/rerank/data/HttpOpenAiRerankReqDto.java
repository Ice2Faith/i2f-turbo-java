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
public class HttpOpenAiRerankReqDto {
    protected String model;
    protected String query;
    protected List<String> documents;
    protected Integer top_n;
    protected Boolean return_documents;
}
