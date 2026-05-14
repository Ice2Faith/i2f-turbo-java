package i2f.mixin.impl;


import i2f.uid.SnowflakeLongUid;

import java.util.UUID;

/**
 * @author Ice2Faith
 * @date 2026/5/14 9:32
 * @desc
 */
public interface UuidMixins {

    default String uuid() {
        return UUID.randomUUID().toString();
    }

    default long snowflake_id() {
        return SnowflakeLongUid.getId();
    }
}
