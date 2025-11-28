package i2f.springboot.ops.app.data.logging;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/11/28 15:44
 */
@Data
@NoArgsConstructor
public class AppLoggingDto {
    protected String location;
    protected String level;
    protected String effectLevel;
}
