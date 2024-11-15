package i2f.data.processor.impl;

import i2f.data.processor.IDataWriter;
import i2f.lifecycle.exception.LifeCycleException;

import java.io.*;

/**
 * @author Ice2Faith
 * @date 2024/11/15 14:33
 */
public class StreamWriterDataWriter implements IDataWriter<String> {
    protected File file;
    protected OutputStream outputStream;
    protected OutputStreamWriter streamWriter;
    protected BufferedWriter writer;
    protected boolean deleteOnDestroy = false;

    public StreamWriterDataWriter(File file) {
        this.file = file;
    }

    public StreamWriterDataWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public StreamWriterDataWriter(OutputStreamWriter streamWriter) {
        this.streamWriter = streamWriter;
    }

    public StreamWriterDataWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    public boolean isDeleteOnDestroy() {
        return deleteOnDestroy;
    }

    public StreamWriterDataWriter setDeleteOnDestroy(boolean deleteOnDestroy) {
        this.deleteOnDestroy = deleteOnDestroy;
        return this;
    }

    @Override
    public void write(String obj) throws IOException {
        this.writer.write(obj);
        this.writer.newLine();
        this.writer.flush();
    }

    @Override
    public void create() {
        try {
            if (writer == null) {
                if (streamWriter == null) {
                    if (outputStream == null) {
                        outputStream = new FileOutputStream(file);
                    }
                    streamWriter = new OutputStreamWriter(outputStream);
                }
                writer = new BufferedWriter(streamWriter);
            }
        } catch (IOException e) {
            throw new LifeCycleException(e.getMessage(), e);
        }
    }

    @Override
    public void destroy() {
        try {
            if (writer != null) {
                writer.close();
            }
            if (deleteOnDestroy) {
                if (file != null) {
                    file.delete();
                }
            }
        } catch (IOException e) {
            throw new LifeCycleException(e.getMessage(), e);
        }
    }
}
