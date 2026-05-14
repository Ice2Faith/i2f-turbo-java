package i2f.mixin.impl;

/**
 * @author Ice2Faith
 * @date 2026/5/14 9:06
 * @desc
 */
public interface SystemMixins {
    default void print(Object... objs) {
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for (Object obj : objs) {
            if (!isFirst) {
                builder.append(", ");
            }
            builder.append(obj);
            isFirst = false;
        }
        System.out.print(builder.toString());
    }

    default void println(Object... objs) {
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for (Object obj : objs) {
            if (!isFirst) {
                builder.append(", ");
            }
            builder.append(obj);
            isFirst = false;
        }
        System.out.println(builder.toString());
    }

    default String sys_env(String key) {
        return System.getenv(key);
    }

    default String jvm(String key) {
        String prop = System.getProperty(key);
        return prop;
    }

    default void gc() {
        System.gc();
    }

    default void exit(int status) {
        System.exit(status);
    }

    default String sys_property(String key) {
        return System.getProperty(key);
    }

}
