package i2f.spring.ai.tool;

import i2f.ai.std.tool.schema.JsonSchemaAnnotationResolver;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author Ice2Faith
 * @date 2026/3/28 18:35
 * @desc
 */
public class SpringAiJsonSchemaAnnotationResolver extends JsonSchemaAnnotationResolver {
    public static final SpringAiJsonSchemaAnnotationResolver INSTANCE = new SpringAiJsonSchemaAnnotationResolver();

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
                if (ann.description() != null && !ann.description().isEmpty()) {
                    return ann.description();
                }
            }
        }
        return null;
    }


    @Override
    public String getToolParameterDescription(Parameter parameter) {
        String ret = super.getToolParameterDescription(parameter);
        if (ret == null || ret.isEmpty()) {
            ToolParam ann = parameter.getAnnotation(ToolParam.class);
            if (ann != null) {
                if (ann.description() != null && !ann.description().isEmpty()) {
                    return ann.description();
                }
            }
        }
        return null;
    }
}
