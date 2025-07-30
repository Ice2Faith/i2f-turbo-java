package i2f.database.metadata.data;

import i2f.database.metadata.std.StdType;
import lombok.Data;

import java.sql.JDBCType;
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

    protected Class<?> rawJavaType;
    protected JDBCType rawJdbcType;
    protected StdType rawStdType;
    protected Class<?> rawLooseJavaType;
    protected JDBCType rawLooseJdbcType;

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

    public ColumnMeta fillRawTypes() {
        if (rawStdType == null) {
            rawStdType = StdType.detectType(stdType, javaType);
        }
        if (rawStdType == null) {
            return this;
        }
        if (rawJavaType == null) {
            rawJavaType = rawStdType.javaType();
        }
        if (rawJdbcType == null) {
            rawJdbcType = rawStdType.jdbcType();
        }
        if (rawLooseJavaType == null) {
            rawLooseJavaType = rawStdType.looseJavaType();
        }
        if (rawLooseJdbcType == null) {
            rawLooseJdbcType = rawStdType.looseJdbcType();
        }
        return this;
    }
}
