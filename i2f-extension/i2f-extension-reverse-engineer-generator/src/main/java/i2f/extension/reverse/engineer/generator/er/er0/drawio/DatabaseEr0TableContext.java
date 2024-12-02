package i2f.extension.reverse.engineer.generator.er.er0.drawio;

import i2f.database.metadata.data.TableMeta;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2024/12/2 10:27
 */
@Data
@NoArgsConstructor
public class DatabaseEr0TableContext {
    protected TableMeta meta;
    protected String id;
    protected String value;
    protected int x;
    protected int y;
    protected int width;
    protected int height;
}
