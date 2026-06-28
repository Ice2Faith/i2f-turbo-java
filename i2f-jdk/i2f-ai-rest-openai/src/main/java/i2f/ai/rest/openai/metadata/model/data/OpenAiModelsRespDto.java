package i2f.ai.rest.openai.metadata.model.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/6/25 17:50
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class OpenAiModelsRespDto {
    protected String object;
    protected List<OpenAiModelsItem> data;
}
