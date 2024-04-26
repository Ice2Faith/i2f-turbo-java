package i2f.typeof.token.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/4/26 14:19
 * @desc
 */
@Data
@NoArgsConstructor
public class TypeNode {
    private Class<?> type;
    private List<TypeNode> args = new ArrayList<>();

    public String simpleName() {
        StringBuilder builder = new StringBuilder();
        builder.append(type.getSimpleName());
        if (!args.isEmpty()) {
            builder.append("<");
            boolean isFirst = true;
            for (TypeNode item : args) {
                if (!isFirst) {
                    builder.append(", ");
                }
                builder.append(item.simpleName());
                isFirst = false;
            }
            builder.append(">");
        }
        return builder.toString();
    }
}
