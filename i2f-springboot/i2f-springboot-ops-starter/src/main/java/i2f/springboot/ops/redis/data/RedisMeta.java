package i2f.springboot.ops.redis.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/5/22 20:05
 * @desc
 */
@Data
@NoArgsConstructor
public class RedisMeta {
    protected Boolean useCustom;
    protected String host;
    protected int port = 6379;
    protected String password;
    protected int database = 0;
}
