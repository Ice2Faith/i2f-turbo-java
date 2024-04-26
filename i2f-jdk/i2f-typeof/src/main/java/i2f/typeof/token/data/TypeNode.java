package i2f.typeof.token.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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
        return nameString(c -> {
            String name = c.getName();
            if (c.isMemberClass()) {
                String[] arr = name.split("\\.");
                if (arr.length > 1) {
                    return arr[arr.length - 1].replace("$", ".");
                }
            }
            return c.getSimpleName();
        });
    }

    public String importName() {
        return nameString(c -> {
            String name = c.getName();
            String simpleName = c.getSimpleName();
            if (name.equals("java.lang." + simpleName)) {
                return simpleName;
            }
            return name;
        });
    }

    public String fullName() {
        return nameString(Class::getName);
    }

    public String nameString(Function<Class<?>, String> typeNameMapper) {
        StringBuilder builder = new StringBuilder();
        builder.append(typeNameMapper.apply(type));
        if (!args.isEmpty()) {
            builder.append("<");
            boolean isFirst = true;
            for (TypeNode item : args) {
                if (!isFirst) {
                    builder.append(", ");
                }
                builder.append(item.nameString(typeNameMapper));
                isFirst = false;
            }
            builder.append(">");
        }
        return builder.toString();
    }
}
