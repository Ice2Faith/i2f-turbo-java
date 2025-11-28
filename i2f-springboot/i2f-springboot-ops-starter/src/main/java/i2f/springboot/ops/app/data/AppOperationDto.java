package i2f.springboot.ops.app.data;

import i2f.springboot.ops.app.data.logging.AppLoggingDto;
import i2f.springboot.ops.common.OpsHostIdDto;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/11/14 8:41
 */
@Data
@NoArgsConstructor
public class AppOperationDto extends OpsHostIdDto {
    protected String className;

    protected String script;
    protected long waitForSeconds;

    protected AppLoggingDto logging;
}
