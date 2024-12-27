package i2f.uid;

import java.util.UUID;

/**
 * @author Ice2Faith
 * @date 2024/12/27 23:29
 * @desc
 */
public class UuidRandomStringUid implements IStringUid {
    public static final UuidRandomStringUid INSTANCE = new UuidRandomStringUid();

    @Override
    public String nextStringId() {
        return UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
    }
}
