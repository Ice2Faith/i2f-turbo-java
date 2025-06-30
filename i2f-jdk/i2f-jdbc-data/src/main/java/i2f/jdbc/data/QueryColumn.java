package i2f.jdbc.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.JDBCType;

/**
 * @author Ice2Faith
 * @date 2024/3/14 11:05
 * @desc
 */
@Data
@NoArgsConstructor
public class QueryColumn {
    protected int index;
    protected String name;
    protected String originName;
    protected String catalog;
    protected String clazzName;
    protected Class<?> clazz;
    protected int displaySize;
    protected String label;
    protected int type;
    protected JDBCType jdbcType;
    protected String typeName;
    protected int precision;
    protected int scale;
    protected String schema;
    protected String table;
    protected boolean nullable;
    protected boolean autoIncrement;
    protected boolean readonly;
    protected boolean writable;
    protected boolean caseSensitive;
    protected boolean currency;
    protected boolean definitelyWritable;
    protected boolean searchable;
    protected boolean signed;

}
