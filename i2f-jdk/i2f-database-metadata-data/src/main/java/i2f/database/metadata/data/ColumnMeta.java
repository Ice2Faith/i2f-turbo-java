package i2f.database.metadata.data;

import lombok.Data;

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

    public static int compare(ColumnMeta o1, ColumnMeta o2) {
        return Integer.compare(o1.getIndex(), o2.getIndex());
    }
}
