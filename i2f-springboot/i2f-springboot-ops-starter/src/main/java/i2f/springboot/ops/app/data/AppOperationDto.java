package i2f.springboot.ops.app.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/11/14 8:41
 */
@Data
@NoArgsConstructor
public class AppOperationDto {
    protected String hostId;

    protected String script;
    protected long waitForSeconds;
}
