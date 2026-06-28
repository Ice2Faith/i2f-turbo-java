package i2f.mixin.impl;

import i2f.jvm.JvmUtil;

/**
 * @author Ice2Faith
 * @date 2026/5/14 19:53
 * @desc
 */
public interface JvmMixins {
    default String jvm_pid() {
        return JvmUtil.getPid();
    }

    default String jvm_user() {
        return JvmUtil.getStartUser();
    }

    default boolean jvm_debug() {
        return JvmUtil.isDebug();
    }

    default boolean jvm_agent() {
        return JvmUtil.isAgent();
    }
}
