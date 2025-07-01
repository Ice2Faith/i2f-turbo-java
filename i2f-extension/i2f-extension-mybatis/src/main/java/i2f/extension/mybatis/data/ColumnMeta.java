package i2f.extension.mybatis.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.JDBCType;

/**
 * @author Ice2Faith
 * @date 2025/6/30 21:08
 * @desc
 */
@Data
@NoArgsConstructor
public class ColumnMeta {
    protected String name;
    protected String columnName;
    protected String typeName;
    protected int type;
    protected JDBCType jdbcType;
    protected int displaySize;
    protected int precision;
    protected int scale;
    protected String className;
    protected Class<?> clazz;
    protected boolean autoIncrement;
    protected boolean nullable;
}
