package i2f.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Ice2Faith
 * @date 2022/5/4 17:01
 * @desc
 */
public class UnsafeHacker {
    public static final String NASHORN_OBJECT_SIZE_CALCULATOR_CLASS_NAME = "jdk.nashorn.internal.ir.debug.ObjectSizeCalculator";
    public static final String NASHORN_MAVEN_DEPENDENCY = "" +
            "<dependency>\n" +
            "    <groupId>org.openjdk.nashorn</groupId>\n" +
            "    <artifactId>nashorn-core</artifactId>\n" +
            "    <version>15.4</version>\n" +
            "</dependency>";
    public static Class<?> NASHORN_OBJECT_SIZE_CACULATOR_CLASS = null;
    private static volatile Unsafe unsafe;

    static {
        if (NASHORN_OBJECT_SIZE_CACULATOR_CLASS == null) {
            try {
                NASHORN_OBJECT_SIZE_CACULATOR_CLASS = Class.forName(NASHORN_OBJECT_SIZE_CALCULATOR_CLASS_NAME);
            } catch (Throwable e) {
            }
        }
        if (NASHORN_OBJECT_SIZE_CACULATOR_CLASS == null) {
            try {
                NASHORN_OBJECT_SIZE_CACULATOR_CLASS = Thread.currentThread().getContextClassLoader().loadClass(NASHORN_OBJECT_SIZE_CALCULATOR_CLASS_NAME);
            } catch (Throwable e) {
            }
        }
        if (NASHORN_OBJECT_SIZE_CACULATOR_CLASS == null) {
            System.out.println("nashorn missing, maybe add dependency : \n" + NASHORN_MAVEN_DEPENDENCY);
        }
    }

    public static Unsafe getUnsafe() {
        if (unsafe == null) {
            synchronized (UnsafeHacker.class) {
                if (unsafe == null) {
                    try {
                        Field field = Unsafe.class.getDeclaredField("theUnsafe");
                        field.setAccessible(true);
                        unsafe = (Unsafe) field.get(null);
                    } catch (Exception e) {
                        throw new IllegalStateException(e.getMessage(), e);
                    }
                }
            }
        }
        return unsafe;
    }

    public static long addressOf(Object obj) {
        long ret = 0;
        if (obj == null) {
            return ret;
        }
        Unsafe unsafe = getUnsafe();
        Object[] array = new Object[]{obj};

        long baseOffset = unsafe.arrayBaseOffset(Object[].class);
        int addressSize = unsafe.addressSize();

        switch (addressSize) {
            case 4:
                ret = unsafe.getInt(array, baseOffset);
                break;
            case 8:
                ret = unsafe.getLong(array, baseOffset);
                break;
            default:
                throw new UnsupportedOperationException("unsupported address size: " + addressSize);
        }
        return ret;
    }

    public static String addressHexOf(Object obj) {
        return String.format("0x%08X", addressOf(obj));
    }

    public static long sizeOf(Object obj) {
        if (NASHORN_OBJECT_SIZE_CACULATOR_CLASS == null) {
            return -1;
        }
        try {
            Method method = NASHORN_OBJECT_SIZE_CACULATOR_CLASS.getDeclaredMethod("getObjectSize", Object.class);
            if (method == null) {
                return -1;
            }
            return (Long) method.invoke(null, obj);
        } catch (Throwable e) {

        }
        return -1;
    }

}
