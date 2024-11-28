package i2f.database.metadata.data;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/3/14 10:29
 * @desc
 */
@Data
public class ColumnMeta {
    protected int index;
    protected String name;
    protected String type;
    protected String comment;

    protected int precision;
    protected int scale;

    protected boolean isNullable;
    protected boolean isAutoIncrement;
    protected boolean isGenerated;

    protected String defaultValue;

    protected String columnType;
    protected String javaType;
    protected String jdbcType;
    protected String stdType;
    protected String looseJavaType;
    protected String looseJdbcType;

    // 以下字段，需要使用 TableMeta.fillColumnIndexMeta 后才有值，才能正确使用
    protected boolean isPrimaryKey;
    protected String primaryKeyName;
    protected int primaryKeyOrder;

    protected boolean isUniqueKey;
    protected List<Map.Entry<String, Integer>> uniqueKeyList;

    protected boolean isIndexKey;
    protected List<Map.Entry<String, Integer>> indexKeyList;

    public static int compare(ColumnMeta o1, ColumnMeta o2) {
        return Integer.compare(o1.getIndex(), o2.getIndex());
    }
}
