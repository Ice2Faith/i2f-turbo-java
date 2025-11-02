package i2f.springboot.ops.common;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/11/1 22:17
 * @desc
 */
@Data
@NoArgsConstructor
public class OpsSecureDto {
    protected String timestamp;
    protected String nonce;
    protected String key;
    protected String sign;
    protected String digital;
    protected String payload;
}
