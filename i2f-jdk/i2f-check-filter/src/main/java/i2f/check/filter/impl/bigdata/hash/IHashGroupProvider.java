package i2f.check.filter.impl.bigdata.hash;


import i2f.data.processor.IDataReader;
import i2f.data.processor.IDataWriter;
import i2f.lifecycle.ILifeCycle;

/**
 * @author Ice2Faith
 * @date 2022/4/27 10:11
 * @desc
 */
public interface IHashGroupProvider<T> extends ILifeCycle {
    IDataWriter<T> getWriter(String hash);

    IDataReader<T> getReader(String hash);
}
