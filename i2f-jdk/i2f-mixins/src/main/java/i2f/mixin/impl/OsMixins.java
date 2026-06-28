package i2f.mixin.impl;

import i2f.os.OsUtil;

/**
 * @author Ice2Faith
 * @date 2026/5/14 20:00
 * @desc
 */
public interface OsMixins {
    default boolean os_windows() {
        return OsUtil.isWindows();
    }

    default boolean os_linux() {
        return OsUtil.isLinux();
    }

    default boolean os_64bit() {
        return OsUtil.is64bit();
    }
}
