package i2f.springboot.ops.openai.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import i2f.ai.rest.openai.model.data.OpenAiCompletionReqDto;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/4/30 20:38
 * @desc
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
public class OpenAiCompletionDto extends OpenAiCompletionReqDto {

}
