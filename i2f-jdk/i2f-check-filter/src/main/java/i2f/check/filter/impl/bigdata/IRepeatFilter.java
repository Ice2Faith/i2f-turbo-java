package i2f.check.filter.impl.bigdata;


import i2f.data.processor.IDataReader;
import i2f.data.processor.IDataWriter;

import java.io.IOException;
import java.util.List;

/**
 * @author ltb
 * @date 2022/4/27 14:15
 * @desc
 */
public interface IRepeatFilter<T> {
    void filter(IDataWriter<T> writer, List<IDataReader<T>> readers) throws IOException;
}
