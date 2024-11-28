package i2f.reverse.engineer.generator.database;

import i2f.database.metadata.data.ColumnMeta;
import i2f.extension.velocity.GeneratorTool;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ltb
 * @date 2022/6/15 16:32
 * @desc
 */
@Data
public class ColumnContext {
    private String name;
    private String comment;

    private String pascalName;
    private String camelName;

    private String javaType;
    private String jdbcType;

    private boolean isAutoincrement = false;

    private boolean isPrimaryKey = false;

    public static ColumnContext parse(ColumnMeta meta) {
        ColumnContext ret = new ColumnContext();
        ret.name = meta.getName();
        ret.comment = meta.getComment();

        ret.camelName = GeneratorTool.toCamel(GeneratorTool.lower(ret.name));
        ret.pascalName = GeneratorTool.toPascal(GeneratorTool.lower(ret.name));

        ret.javaType = meta.getJavaType();

        if (ret.javaType != null) {
            if (ret.javaType.startsWith("java.lang.")) {
                ret.javaType = ret.javaType.substring("java.lang.".length());
            }
            if (ret.javaType.equals(Timestamp.class.getName())) {
                ret.javaType = Date.class.getSimpleName();
            }
        }
        ret.jdbcType = meta.getType();

        ret.isAutoincrement = meta.isAutoIncrement();
        ret.isPrimaryKey = meta.isAutoIncrement();
        return ret;
    }

    public static List<ColumnContext> parse(List<ColumnMeta> metas) {
        List<ColumnContext> ret = new ArrayList<>();
        for (ColumnMeta item : metas) {
            ColumnContext ctx = parse(item);
            ret.add(ctx);
        }
        return ret;
    }
}
