package i2f.jdbc.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.JDBCType;
import java.sql.SQLType;

/**
 * @author Ice2Faith
 * @date 2024/6/6 13:49
 * @desc
 */
@Data
@NoArgsConstructor
public class TypedArgument {
    private Class<?> javaType;
    /**
     * argument @reference(JDBCType)
     */
    private SQLType jdbcType;
    private ArgumentTypeHandler handler;
    private Object value;

    public TypedArgument(Class<?> javaType) {
        this.javaType = javaType;
    }

    public TypedArgument(Class<?> javaType, Object value) {
        this.javaType = javaType;
        this.value = value;
    }

    public TypedArgument(Object value, ArgumentTypeHandler handler) {
        this.value = value;
        this.handler = handler;
    }

    public TypedArgument(SQLType jdbcType, Object value) {
        this.jdbcType = jdbcType;
        this.value = value;
    }

    public TypedArgument(JDBCType jdbcType, Object value) {
        this.jdbcType = jdbcType;
        this.value = value;
    }

    public static TypedArgument of(Class<?> javaType) {
        return new TypedArgument(javaType);
    }

    public static TypedArgument of(Class<?> javaType, Object value) {
        return new TypedArgument(javaType, value);
    }

    public static TypedArgument of(Object value, ArgumentTypeHandler handler) {
        return new TypedArgument(value, handler);
    }

    public static TypedArgument of(SQLType jdbcType, Object value) {
        return new TypedArgument(jdbcType, value);
    }

    public static TypedArgument of(JDBCType jdbcType, Object value) {
        return new TypedArgument(jdbcType, value);
    }

}
