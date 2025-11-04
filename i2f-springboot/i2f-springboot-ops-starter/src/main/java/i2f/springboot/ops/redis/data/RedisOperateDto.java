package i2f.springboot.ops.redis.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/11/4 21:27
 * @desc
 */
@Data
@NoArgsConstructor
public class RedisOperateDto {
    protected String pattern;
    protected Long count;

    protected List<String> keys;

    protected String key;
    protected String value;
    protected Long ttl;
}
