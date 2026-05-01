package i2f.springboot.ops.openai.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/4/30 20:30
 * @desc
 */
@Data
@NoArgsConstructor
public class OpenAiMessageDto {
    private String role;
    private String content;
}
