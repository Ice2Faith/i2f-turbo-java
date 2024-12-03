package i2f.extension.reverse.engineer.generator.database;

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
    private String dataType;
    private String columnType;
    private boolean isNullable = true;
    private boolean isPrimary = false;
    private boolean isAutoIncrement = false;
    private String javaType;

    private ColumnMeta meta;

    public static ColumnContext parse(ColumnMeta meta) {
        ColumnContext ret = new ColumnContext();
        ret.name = meta.getName();
        ret.comment = meta.getComment();

        ret.javaType = meta.getJavaType();

        if (ret.javaType != null) {
            if (ret.javaType.startsWith("java.lang.")) {
                ret.javaType = ret.javaType.substring("java.lang.".length());
            }
            if (ret.javaType.equals(Timestamp.class.getName())) {
                ret.javaType = Date.class.getSimpleName();
            }
        }
        ret.dataType = meta.getType();
        ret.columnType = meta.getColumnType();
        ret.isNullable = meta.isNullable();
        ret.isPrimary = meta.isPrimaryKey();
        ret.isAutoIncrement = meta.isAutoIncrement();

        ret.meta = meta;
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
