package i2f.springboot.ops.common;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/11/21 13:50
 */
@Data
@NoArgsConstructor
public class OpsHostIdDto {
    protected String hostId;
    protected boolean proxyHostId;
}
