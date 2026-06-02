package i2f.springboot.ops.openai.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/4/30 20:28
 * @desc
 */
@Data
@NoArgsConstructor
public class OpenAiOperateDto {
    protected OpenAiMeta meta;
    protected OpenAiCompletionVo completion;
    protected List<OpenAiToolApprovalDto> toolApprovalList;
    protected boolean encryptOutput = false;
    protected boolean enableTools = false;
}
