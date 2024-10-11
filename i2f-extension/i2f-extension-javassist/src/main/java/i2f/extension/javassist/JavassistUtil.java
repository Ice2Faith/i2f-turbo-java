package i2f.extension.javassist;

import javassist.CtClass;
import javassist.CtMethod;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/8/2 8:40
 * @desc
 */
public class JavassistUtil {
    public static boolean isAssignableFrom(CtClass clazz, String fullClassName) {
        if (clazz == null) {
            return false;
        }
        String name = clazz.getName();
        if (name == null) {
            return false;
        }
        if (fullClassName.equals(name)) {
            return true;
        }
        try {
            CtClass superclass = clazz.getSuperclass();
            if (isAssignableFrom(superclass, fullClassName)) {
                return true;
            }
        } catch (Exception e) {

        }
        try {
            CtClass[] interfaces = clazz.getInterfaces();
            if (interfaces != null) {
                for (CtClass anInterface : interfaces) {
                    if (isAssignableFrom(anInterface, fullClassName)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {

        }
        return false;
    }

    public static Map<CtMethod, CtClass> getAllMethods(CtClass clazz) {
        Map<CtMethod, CtClass> ret = new LinkedHashMap<>();
        if (clazz == null) {
            return ret;
        }
        CtMethod[] methods = clazz.getMethods();
        if (methods != null) {
            for (CtMethod method : methods) {
                ret.put(method, clazz);
            }
        }
        CtMethod[] declaredMethods = clazz.getDeclaredMethods();
        if (declaredMethods != null) {
            for (CtMethod method : declaredMethods) {
                ret.put(method, clazz);
            }
        }
        try {
            CtClass superclass = clazz.getSuperclass();
            Map<CtMethod, CtClass> next = getAllMethods(superclass);
            ret.putAll(next);
        } catch (Exception e) {

        }
        try {
            CtClass[] interfaces = clazz.getInterfaces();
            if (interfaces != null) {
                for (CtClass anInterface : interfaces) {
                    Map<CtMethod, CtClass> next = getAllMethods(anInterface);
                    ret.putAll(next);
                }
            }
        } catch (Exception e) {

        }
        return ret;
    }
}
