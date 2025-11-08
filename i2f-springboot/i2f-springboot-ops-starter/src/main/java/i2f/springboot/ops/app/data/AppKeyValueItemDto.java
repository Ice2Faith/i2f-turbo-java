package i2f.springboot.ops.app.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/11/8 17:59
 * @desc
 */
@Data
@NoArgsConstructor
public class AppKeyValueItemDto {
    protected String key;
    protected Object value;

    public AppKeyValueItemDto(String key, Object value) {
        this.key = key;
        this.value = value;
    }
}
