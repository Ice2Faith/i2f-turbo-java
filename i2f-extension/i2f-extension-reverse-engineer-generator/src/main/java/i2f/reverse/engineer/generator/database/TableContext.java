package i2f.reverse.engineer.generator.database;

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

    public static TableContext parse(TableMeta meta) {
        TableContext ret = new TableContext();
        ret.name = meta.getTable();
        ret.comment = meta.getRemark();

        ret.camelName = GeneratorTool.toCamel(GeneratorTool.lower(ret.name));
        ret.pascalName = GeneratorTool.toPascal(GeneratorTool.lower(ret.name));

        ret.columns = ColumnContext.parse(meta.getColumns());
        return ret;
    }
}
