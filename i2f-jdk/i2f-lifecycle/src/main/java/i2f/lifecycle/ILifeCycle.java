package i2f.lifecycle;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author ltb
 * @date 2022/4/14 14:18
 * @desc
 */
public interface ILifeCycle extends Closeable {
    default void create() {

    }

    default void destroy() {

    }

    @Override
    default void close() throws IOException {
        this.destroy();
    }
}
