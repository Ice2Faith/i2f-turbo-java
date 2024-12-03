package i2f.extension.reverse.engineer.generator.database;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2024/12/3 11:50
 */
@Data
@NoArgsConstructor
public class PrimaryContext {
    private String name = "id";
    private String javaType = "Long";
}
