package i2f.jdbc.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2024/6/6 13:49
 * @desc
 */
@Data
@NoArgsConstructor
public class TypedArgument {
    private Class<?> type;
    private Object value;

    public TypedArgument(Class<?> type) {
        this.type = type;
    }

    public TypedArgument(Class<?> type, Object value) {
        this.type = type;
        this.value = value;
    }

    public static TypedArgument of(Class<?> type) {
        return new TypedArgument(type);
    }

    public static TypedArgument of(Class<?> type, Object value) {
        return new TypedArgument(type, value);
    }
}
