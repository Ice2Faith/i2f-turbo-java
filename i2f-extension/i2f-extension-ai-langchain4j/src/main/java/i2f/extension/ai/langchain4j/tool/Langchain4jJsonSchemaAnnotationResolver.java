package i2f.extension.ai.langchain4j.tool;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import i2f.ai.std.tool.schema.JsonSchemaAnnotationResolver;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author Ice2Faith
 * @date 2026/3/28 18:35
 * @desc
 */
public class Langchain4jJsonSchemaAnnotationResolver extends JsonSchemaAnnotationResolver {
    public static final Langchain4jJsonSchemaAnnotationResolver INSTANCE = new Langchain4jJsonSchemaAnnotationResolver();

    @Override
    public boolean isToolMethod(Method method) {
        boolean ret = super.isToolMethod(method);
        if (!ret) {
            Tool ann = method.getAnnotation(Tool.class);
            if (ann != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getToolMethodName(Method method) {
        String ret = super.getToolMethodName(method);
        if (ret == null || ret.isEmpty()) {
            Tool ann = method.getAnnotation(Tool.class);
            if (ann != null) {
                if (ann.name() != null && !ann.name().isEmpty()) {
                    return ann.name();
                }
            }
        }
        return null;
    }

    @Override
    public String getToolMethodDescription(Method method) {
        String ret = super.getToolMethodDescription(method);
        if (ret == null || ret.isEmpty()) {
            Tool ann = method.getAnnotation(Tool.class);
            if (ann != null) {
                if (ann.value() != null && ann.value().length > 0) {
                    return String.join("\n", ann.value());
                }
            }
        }
        return null;
    }


    @Override
    public String getToolParameterDescription(Parameter parameter) {
        String ret = super.getToolParameterDescription(parameter);
        if (ret == null || ret.isEmpty()) {
            P ann = parameter.getAnnotation(P.class);
            if (ann != null) {
                if (ann.value() != null && !ann.value().isEmpty()) {
                    return ann.value();
                }
            }
        }
        return null;
    }
}
