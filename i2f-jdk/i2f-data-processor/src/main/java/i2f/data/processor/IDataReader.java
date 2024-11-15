package i2f.data.processor;

import i2f.lifecycle.ILifeCycle;

import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2024/11/15 14:32
 */
public interface IDataReader<T> extends ILifeCycle {
    T read() throws IOException;

    boolean hasMore() throws IOException;
}

