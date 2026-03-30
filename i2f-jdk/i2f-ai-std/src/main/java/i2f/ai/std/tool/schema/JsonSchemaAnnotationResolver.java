package i2f.ai.std.tool.schema;

import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/3/28 17:43
 * @desc
 */
public class JsonSchemaAnnotationResolver {
    public static final JsonSchemaAnnotationResolver INSTANCE = new JsonSchemaAnnotationResolver();

    public boolean isToolsObject(Object obj) {
        if (obj == null) {
            return false;
        }
        Class<?> clazz = obj.getClass();
        return isToolsClass(clazz);
    }

    public boolean isToolsClass(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        Tools ann = clazz.getAnnotation(Tools.class);
        if (ann == null) {
            return false;
        }
        return true;
    }

    public boolean isToolMethod(Method method) {
        if (method == null) {
            return false;
        }
        Tool ann = method.getAnnotation(Tool.class);
        if (ann == null) {
            return false;
        }
        return true;
    }

    public String getToolMethodName(Method method) {
        if (!isToolMethod(method)) {
            return null;
        }
        Tool ann = method.getAnnotation(Tool.class);
        if (ann != null) {
            if (ann.value() != null && !ann.value().isEmpty()) {
                return ann.value();
            }
        }
        return method.getName();
    }

    public String getToolMethodDescription(Method method) {
        if (!isToolMethod(method)) {
            return null;
        }
        Tool ann = method.getAnnotation(Tool.class);
        if (ann != null) {
            if (ann.description() != null && !ann.description().isEmpty()) {
                return ann.description();
            }
        }
        return null;
    }

    public List<String> getToolMethodTags(Method method) {
        if (!isToolMethod(method)) {
            return new ArrayList<>();
        }
        List<String> ret = new ArrayList<>();
        Tool ann = method.getAnnotation(Tool.class);
        if (ann != null) {
            if (ann.tags() != null && ann.tags().length != 0) {
                ret.addAll(Arrays.asList(ann.tags()));
            }
        }
        Class<?> declaringClass = method.getDeclaringClass();
        Tools classAnn = declaringClass.getAnnotation(Tools.class);
        if (classAnn != null) {
            if (classAnn.tags() != null && classAnn.tags().length != 0) {
                ret.addAll(Arrays.asList(classAnn.tags()));
            }
        }
        return ret;
    }

    public String getToolParameterName(Parameter parameter) {
        ToolParam ann = parameter.getAnnotation(ToolParam.class);
        if (ann != null) {
            if (ann.value() != null && !ann.value().isEmpty()) {
                return ann.value();
            }
        }
        return parameter.getName();
    }

    public String getToolParameterDescription(Parameter parameter) {
        ToolParam ann = parameter.getAnnotation(ToolParam.class);
        if (ann != null) {
            if (ann.description() != null && !ann.description().isEmpty()) {
                return ann.description();
            }
        }
        return null;
    }

    public String getToolFieldDescription(Field field) {
        ToolParam ann = field.getAnnotation(ToolParam.class);
        if (ann != null) {
            if (ann.description() != null && !ann.description().isEmpty()) {
                return ann.description();
            }
        }
        return null;
    }
}
