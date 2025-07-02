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
    /**
     * ResultSet -> columnLabel -> 列名
     */
    protected String name;
    /**
     * Mybatis 映射之后的字段名，Pojo 对应的就是 Field 名，Map 对应的就是 Key 名
     */
    protected String prop;
    /**
     * Mybatis 映射的结果元素类型，也就是 List<T> 的 T 的类型
     */
    protected Class<?> elemType;
    /**
     * ResultSet -> columnName -> 表字段名称，如果使用了 as 起别名的情况下，这里是原始列的名称，而不是 as 的别名
     */
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
