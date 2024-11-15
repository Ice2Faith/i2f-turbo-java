package i2f.check.filter.impl.bigdata.hash.impl;


import i2f.check.filter.impl.bigdata.hash.IHashGroupProvider;
import i2f.data.processor.IDataReader;
import i2f.data.processor.IDataWriter;
import i2f.data.processor.impl.StreamReaderDataReader;
import i2f.data.processor.impl.StreamWriterDataWriter;

import java.io.File;

/**
 * @author ltb
 * @date 2022/4/27 11:27
 * @desc
 */
public class StreamHashGroupProvider implements IHashGroupProvider<String> {
    protected File basePath;
    protected boolean deleteOnDestroy = true;

    public StreamHashGroupProvider(File basePath) {
        this.basePath = basePath;
    }

    public boolean isDeleteOnDestroy() {
        return deleteOnDestroy;
    }

    public StreamHashGroupProvider setDeleteOnDestroy(boolean deleteOnDestroy) {
        this.deleteOnDestroy = deleteOnDestroy;
        return this;
    }

    @Override
    public IDataWriter<String> getWriter(String hash) {
        return new StreamWriterDataWriter(new File(basePath, hash + ".data")).setDeleteOnDestroy(false);
    }

    @Override
    public IDataReader<String> getReader(String hash) {
        return new StreamReaderDataReader(new File(basePath, hash + ".data")).setDeleteOnDestroy(deleteOnDestroy);
    }

    @Override
    public void create() {

    }

    @Override
    public void destroy() {

    }
}
