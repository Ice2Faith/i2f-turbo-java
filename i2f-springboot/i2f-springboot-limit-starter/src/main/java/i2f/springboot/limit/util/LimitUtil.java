package i2f.springboot.limit.util;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author Ice2Faith
 * @date 2025/11/12 10:23
 */
public class LimitUtil {
    public static String getMethodSignature(Method method) {
        StringBuilder builder = new StringBuilder();
        builder.append(method.getReturnType().getSimpleName());
        builder.append(" ");
        builder.append(method.getDeclaringClass().getSimpleName());
        builder.append(".");
        builder.append(method.getName());
        builder.append("(");
        Parameter[] arr = method.getParameters();
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                builder.append(",");
            }
            builder.append(arr[i].getType().getSimpleName());
        }
        builder.append(")");
        return builder.toString();
    }
}
