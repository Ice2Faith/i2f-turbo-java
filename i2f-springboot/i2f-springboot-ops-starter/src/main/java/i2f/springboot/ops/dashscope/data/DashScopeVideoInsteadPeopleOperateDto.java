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
public class DashScopeVideoInsteadPeopleOperateDto extends DashScopeBaseOperateDto {
    protected String videoUrl;
    protected String imageUrl;
    protected boolean watermark;
    protected String mode;
}
