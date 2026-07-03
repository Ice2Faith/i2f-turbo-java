package i2f.ai.rest.openai.metadata.model.data;

import i2f.mutator.BaseMutator;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/6/25 17:51
 * @desc
 */
@Data
@NoArgsConstructor
public class OpenAiModelsItem implements BaseMutator<OpenAiModelsItem> {
    protected String id;
    protected String object;
    protected String created;
    protected String owned_by;
}
