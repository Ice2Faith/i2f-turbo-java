package i2f.proxy.std.impl;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Ice2Faith
 * @date 2026/5/14 11:11
 * @desc
 */

public final class MethodHandlesUtil {

    // 定义允许的所有访问模式（私有、受保护、包私有、公有）
    private static final int ALLOWED_MODES = MethodHandles.Lookup.PRIVATE
            | MethodHandles.Lookup.PROTECTED
            | MethodHandles.Lookup.PACKAGE
            | MethodHandles.Lookup.PUBLIC;

    // 缓存 JDK 8 的 Lookup 私有构造器
    private static final Constructor<MethodHandles.Lookup> LOOKUP_CONSTRUCTOR;

    private static final Method PRIVATE_LOOKUP_IN_METHOD;
    private static final Field IMPL_LOOKUP_FIELD;


    static {
        Constructor<MethodHandles.Lookup> constructor = null;
        Field implLookupField = null;
        Method privateLookupInMethod = null;
        try {
            // jdk8 获取是有构造
            // 获取 MethodHandles.Lookup 的私有构造器：Lookup(Class<?> lookupClass, int allowedModes)
            constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
            // 暴力反射，打破封装
            constructor.setAccessible(true);
        } catch (ReflectiveOperationException e) {
            // JDK 9+ 没有该构造器，尝试获取 IMPL_LOOKUP 字段
            try {
                privateLookupInMethod = MethodHandles.class.getMethod("privateLookupIn", Class.class, MethodHandles.Lookup.class);

                implLookupField = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
                implLookupField.setAccessible(true);
            } catch (ReflectiveOperationException ex) {
                throw new IllegalStateException("Cannot find Lookup constructor or IMPL_LOOKUP field in current JDK version", ex);
            }
            if (implLookupField == null) {
                throw new IllegalStateException("current JDK version un-support got MethodHandles.Lookup private constructor", e);
            }
        }
        LOOKUP_CONSTRUCTOR = constructor;
        PRIVATE_LOOKUP_IN_METHOD = privateLookupInMethod;
        IMPL_LOOKUP_FIELD = implLookupField;
    }

    /**
     * 获取拥有完整权限的 Lookup 对象（兼容 JDK 8）
     */
    public static MethodHandles.Lookup getLookup(Class<?> declaringClass) {
        try {
            // jdk8 使用是有构造
            if (LOOKUP_CONSTRUCTOR != null) {
                // 通过反射创建 Lookup 实例，传入声明该方法的接口类和允许的模式
                return LOOKUP_CONSTRUCTOR.newInstance(declaringClass, ALLOWED_MODES);
            }

            // jdk9+ 使用 privateLookupIn
            if (PRIVATE_LOOKUP_IN_METHOD != null && IMPL_LOOKUP_FIELD != null) {
                MethodHandles.Lookup baseLookup = (MethodHandles.Lookup) IMPL_LOOKUP_FIELD.get(null);
                // 将 IMPL_LOOKUP 的权限降级到目标类，并赋予全部模式
                return (MethodHandles.Lookup) PRIVATE_LOOKUP_IN_METHOD.invoke(null, declaringClass, baseLookup);
            }

            throw new IllegalStateException("No available method to create Lookup instance");
        } catch (Exception e) {
            throw new RuntimeException("got MethodHandles.Lookup failure", e);
        }
    }

    /**
     * 获取接口默认方法的 MethodHandle
     */
    public static MethodHandle getDefaultMethodHandle(Method method) throws Throwable {
        Class<?> declaringClass = method.getDeclaringClass();
        // 1. 获取 Lookup
        MethodHandles.Lookup lookup = getLookup(declaringClass);
        // 2. 构造 MethodType
        MethodType methodType = MethodType.methodType(method.getReturnType(), method.getParameterTypes());
        // 3. 查找并返回特殊方法句柄 (findSpecial 对应 invokespecial 字节码指令，专门用于调用父类或接口的默认方法)
        return lookup.findSpecial(declaringClass, method.getName(), methodType, declaringClass);
    }
}
