package i2f.springboot.ops.xxljob.data;

import i2f.springboot.ops.common.OpsHostIdDto;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/11/4 21:53
 * @desc
 */
@Data
@NoArgsConstructor
public class XxlJobOperateDto extends OpsHostIdDto {

    protected String method;
    protected String param;

    protected Boolean async;
    protected Long waitForSeconds;
}
