package i2f.net.http.data;


import i2f.builder.BaseBuilder;
import i2f.io.stream.StreamUtil;
import i2f.serialize.std.str.IStringObjectSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author Ice2Faith
 * @date 2022/3/24 8:41
 * @desc
 */
@Data
@NoArgsConstructor
public class HttpResponse implements Closeable, BaseBuilder<HttpResponse> {
    private int statusCode;
    private String statusMessage;
    private long contentLength;
    private HttpHeaders header;

    private InputStream inputStream;

    private InputStream errorStream;

    /**
     * 用于释放底层的资源使用
     */
    private Closeable closer;

    @Override
    public void close() throws IOException {
        if (inputStream != null) {
            inputStream.close();
            inputStream = null;
        }
        if (errorStream != null) {
            errorStream.close();
            errorStream = null;
        }
        if (closer != null) {
            closer.close();
            closer = null;
        }
    }

    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 300;
    }

    public byte[] getContentAsBytes() throws IOException {
        if (inputStream == null) {
            throw new IOException("body has been read or not body!");
        }
        byte[] ret = StreamUtil.readBytes(inputStream, true);
        close();
        return ret;
    }

    public String getContentAsString() throws IOException {
        return getContentAsString(StandardCharsets.UTF_8.name());
    }

    public String getContentAsString(String charset) throws IOException {
        if (inputStream == null) {
            throw new IOException("body has been read or not body!");
        }
        String ret = StreamUtil.readString(inputStream, charset, true);
        close();
        return ret;
    }

    public <T> T getContentAsObject(IStringObjectSerializer processor, Class<T> clazz) throws IOException {
        String json = getContentAsString();
        return (T) processor.deserialize(json, clazz);
    }

    public <T> T getContentAsObject(IStringObjectSerializer processor, Class<T> clazz, String charset) throws IOException {
        String json = getContentAsString(charset);
        return (T) processor.deserialize(json, clazz);
    }

    public <T> T getContentAsRef(IStringObjectSerializer processor, Object refToken) throws IOException {
        String json = getContentAsString();
        return (T) processor.deserialize(json, refToken);
    }

    public <T> T getContentAsRef(IStringObjectSerializer processor, Object refToken, String charset) throws IOException {
        String json = getContentAsString(charset);
        return (T) processor.deserialize(json, refToken);
    }

    public File saveAsFile(File file) throws IOException {
        if (inputStream == null) {
            throw new IOException("body has been read or not body!");
        }
        FileOutputStream fos = new FileOutputStream(file);
        StreamUtil.streamCopy(inputStream, fos, true);
        close();
        return file;
    }

    public OutputStream transferTo(OutputStream os) throws IOException {
        if (inputStream == null) {
            throw new IOException("body has been read or not body!");
        }
        StreamUtil.streamCopy(inputStream, os, false, true);
        os.flush();
        close();
        return os;
    }

    public byte[] getErrorAsBytes() throws IOException {
        if (errorStream == null) {
            throw new IOException("body has been read or not body!");
        }
        byte[] ret = StreamUtil.readBytes(errorStream, true);
        close();
        return ret;
    }

    public String getErrorAsString() throws IOException {
        return getErrorAsString(StandardCharsets.UTF_8.name());
    }

    public String getErrorAsString(String charset) throws IOException {
        if (errorStream == null) {
            throw new IOException("body has been read or not body!");
        }
        String ret = StreamUtil.readString(errorStream, charset, true);
        close();
        return ret;
    }
}
