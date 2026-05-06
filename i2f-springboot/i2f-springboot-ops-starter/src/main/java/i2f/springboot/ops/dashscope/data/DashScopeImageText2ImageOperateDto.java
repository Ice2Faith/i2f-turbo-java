package i2f.springboot.ops.dashscope.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/4/28 19:40
 * @desc
 */
@Data
@NoArgsConstructor
public class DashScopeImageText2ImageOperateDto extends DashScopeBaseOperateDto {
    protected String imageUrl;
    protected String prompt;
    protected String negativePrompt;
    protected String size;
    protected int count;
    protected boolean watermark;
    protected boolean extendPrompt;
}
