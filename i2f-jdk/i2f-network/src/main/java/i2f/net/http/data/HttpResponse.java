package i2f.net.http.data;


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
public class HttpResponse implements Closeable {
    private int statusCode;
    private String statusMessage;
    private long contentLength;
    private HttpHeaders header;

    private InputStream inputStream;

    private InputStream errorStream;

    @Override
    public void close() throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
        if (errorStream != null) {
            errorStream.close();
        }
    }

    public byte[] getContentAsBytes() throws IOException {
        if (inputStream == null) {
            throw new IOException("body has been read or not body!");
        }
        byte[] ret = StreamUtil.readBytes(inputStream, true);
        inputStream = null;
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
        inputStream = null;
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
        return file;
    }

    public byte[] getErrorAsBytes() throws IOException {
        if (errorStream == null) {
            throw new IOException("body has been read or not body!");
        }
        byte[] ret = StreamUtil.readBytes(errorStream, true);
        errorStream = null;
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
        errorStream = null;
        return ret;
    }
}
