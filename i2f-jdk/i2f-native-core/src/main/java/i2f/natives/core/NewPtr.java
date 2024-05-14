package i2f.natives.core;

import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/5/9 13:49
 * @desc
 */
public class NewPtr extends Ptr {
    public NewPtr(long ptr) {
        super(ptr);
    }

    public NewPtr(Ptr ptr) {
        super(ptr);
    }
}
