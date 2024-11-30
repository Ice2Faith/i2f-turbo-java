package i2f.extension.reverse.engineer.generator.database;

import i2f.database.metadata.data.TableMeta;
import i2f.extension.velocity.GeneratorTool;
import lombok.Data;

import java.util.List;

/**
 * @author ltb
 * @date 2022/6/15 16:32
 * @desc
 */
@Data
public class TableContext {
    private String name;
    private String comment;

    private String pascalName;
    private String camelName;

    private List<ColumnContext> columns;

    private TableMeta meta;

    public static TableContext parse(TableMeta meta) {
        TableContext ret = new TableContext();
        ret.name = meta.getName();
        ret.comment = meta.getComment();

        ret.camelName = GeneratorTool.toCamel(GeneratorTool.lower(ret.name));
        ret.pascalName = GeneratorTool.toPascal(GeneratorTool.lower(ret.name));

        ret.columns = ColumnContext.parse(meta.getColumns());

        ret.meta = meta;
        return ret;
    }
}
