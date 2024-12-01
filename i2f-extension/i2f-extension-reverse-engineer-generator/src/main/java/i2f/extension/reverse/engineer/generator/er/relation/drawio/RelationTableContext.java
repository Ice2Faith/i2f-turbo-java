package i2f.extension.reverse.engineer.generator.er.relation.drawio;

import i2f.database.metadata.data.TableMeta;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/6/15 16:32
 * @desc
 */
@Data
@NoArgsConstructor
public class RelationTableContext {
    private TableMeta meta;
    private String id;
    private String value;
    private int x;
    private int y;
    private int width;
    private int height;
}
