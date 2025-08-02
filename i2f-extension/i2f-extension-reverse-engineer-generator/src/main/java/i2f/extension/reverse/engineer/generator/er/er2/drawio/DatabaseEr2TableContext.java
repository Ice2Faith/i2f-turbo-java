package i2f.extension.reverse.engineer.generator.er.er2.drawio;

import i2f.database.metadata.data.TableMeta;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2022/6/15 16:32
 * @desc
 */
@Data
@NoArgsConstructor
public class DatabaseEr2TableContext {
    private TableMeta meta;
    private String id;
    private String value;
    private int x;
    private int y;
    private int width;
    private int height;
}
