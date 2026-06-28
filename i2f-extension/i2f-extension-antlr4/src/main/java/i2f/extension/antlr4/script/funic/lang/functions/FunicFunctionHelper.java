package i2f.extension.antlr4.script.funic.lang.functions;

import i2f.extension.antlr4.script.funic.lang.annotations.FunicFunction;
import i2f.invokable.method.IMethod;
import i2f.invokable.method.impl.DecorateNameMethod;
import i2f.invokable.method.impl.jdk.JdkInstanceStaticMethod;
import i2f.invokable.method.impl.jdk.JdkMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2026/4/23 19:20
 * @desc
 */
public class FunicFunctionHelper {
    public static List<String> getMethodAlias(Method method) {
        List<String> ret = new ArrayList<>();
        FunicFunction ann = method.getAnnotation(FunicFunction.class);
        if (ann == null) {
            return ret;
        }
        String[] arr = ann.value();
        if (arr != null && arr.length > 0) {
            for (String item : arr) {
                item = item.trim();
                if (item.isEmpty()) {
                    continue;
                }
                ret.add(item);
            }
        }
        return ret;
    }

    public static List<IMethod> ofStaticMethod(Method method) {
        List<IMethod> ret = new ArrayList<>();
        if (!Modifier.isStatic(method.getModifiers())) {
            return ret;
        }
        JdkMethod value = new JdkMethod(method);
        ret.add(value);
        List<String> names = getMethodAlias(method);
        for (String name : names) {
            ret.add(new DecorateNameMethod(value, s -> name));
        }
        return ret;
    }

    public static List<IMethod> ofInstanceAsStaticMethod(Object target, Method method) {
        List<IMethod> ret = new ArrayList<>();
        if (Modifier.isStatic(method.getModifiers())) {
            return ofStaticMethod(method);
        }
        JdkInstanceStaticMethod value = new JdkInstanceStaticMethod(target, method);
        ret.add(value);
        List<String> names = getMethodAlias(method);
        for (String name : names) {
            ret.add(new DecorateNameMethod(value, s -> name));
        }
        return ret;
    }

    public static boolean defaultMethodFilter(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        if (Object.class.equals(declaringClass)) {
            return false;
        }
        return true;
    }

    public static List<IMethod> ofMethods(Class<?> clazz) {
        return ofMethods(clazz, FunicFunctionHelper::defaultMethodFilter);
    }

    public static List<IMethod> ofMethods(Class<?> clazz, Predicate<Method> filter) {
        List<IMethod> ret = new ArrayList<>();
        Method[] arr = clazz.getMethods();
        for (Method item : arr) {
            if (Modifier.isStatic(item.getModifiers())) {
                if (filter == null || filter.test(item)) {
                    ret.addAll(ofStaticMethod(item));
                }
            }
        }
        return ret;
    }

    public static List<IMethod> ofInstanceMethods(Object target) {
        return ofInstanceMethods(target, FunicFunctionHelper::defaultMethodFilter);
    }

    public static List<IMethod> ofInstanceMethods(Object target, Predicate<Method> filter) {
        List<IMethod> ret = new ArrayList<>();
        if (target == null) {
            return ret;
        }
        Class<?> clazz = target.getClass();
        Method[] arr = clazz.getMethods();
        for (Method item : arr) {
            if (Modifier.isStatic(item.getModifiers())) {
                if (filter == null || filter.test(item)) {
                    ret.addAll(ofStaticMethod(item));
                }
            } else {
                if (filter == null || filter.test(item)) {
                    ret.addAll(ofInstanceAsStaticMethod(target, item));
                }
            }
        }
        return ret;
    }


}
