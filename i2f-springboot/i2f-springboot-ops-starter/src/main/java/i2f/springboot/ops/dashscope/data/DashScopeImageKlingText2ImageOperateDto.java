package i2f.springboot.ops.dashscope.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/5/8 15:01
 * @desc
 */
@Data
@NoArgsConstructor
public class DashScopeImageKlingText2ImageOperateDto extends DashScopeBaseOperateDto {
    protected Map<String, Object> input;
    protected Map<String, Object> parameters;
}
