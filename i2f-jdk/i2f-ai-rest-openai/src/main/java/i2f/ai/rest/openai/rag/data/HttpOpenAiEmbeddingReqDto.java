package i2f.ai.rest.openai.rag.data;

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
public class HttpOpenAiEmbeddingReqDto {
    protected String model;
    protected List<String> input;
}
