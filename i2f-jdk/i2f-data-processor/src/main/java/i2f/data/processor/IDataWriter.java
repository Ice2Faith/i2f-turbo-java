package i2f.data.processor;

import i2f.lifecycle.ILifeCycle;

import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2024/11/15 14:31
 */
public interface IDataWriter<T> extends ILifeCycle {
    void write(T obj) throws IOException;
}
