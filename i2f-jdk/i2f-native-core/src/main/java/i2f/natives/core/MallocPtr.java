package i2f.natives.core;

import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/5/9 13:49
 * @desc
 */
public class MallocPtr extends Ptr {
    public MallocPtr(long ptr) {
        super(ptr);
    }

    public MallocPtr(Ptr ptr) {
        super(ptr);
    }
}

