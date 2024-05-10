package i2f.natives.core;

import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/5/9 13:49
 * @desc
 */
public class Ptr {
    protected final long ptr;

    public Ptr(long ptr) {
        this.ptr = ptr;
    }

    public Ptr(Ptr ptr) {
        this.ptr = ptr.ptr;
    }

    public long value() {
        return ptr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ptr ptr1 = (Ptr) o;
        return ptr == ptr1.ptr;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ptr);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "ptr=" + ptr +
                '}';
    }
}
