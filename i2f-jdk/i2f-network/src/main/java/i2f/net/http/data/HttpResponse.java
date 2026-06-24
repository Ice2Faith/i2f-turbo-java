package i2f.net.http.data;


import i2f.io.stream.StreamUtil;
import i2f.serialize.std.str.IStringObjectSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;

/**
 * @author Ice2Faith
 * @date 2022/3/24 8:41
 * @desc
 */
@Data
@NoArgsConstructor
public class HttpResponse implements Closeable {
    private int responseCode;
    private String responseMessage;
    private long contentLength;
    private HttpHeaders header;

    private boolean parsedContentBytes;
    private InputStream inputStream;
    private byte[] contentBytes;

    private byte[] errorBytes;
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

    public String getContentAsString(String charset) throws UnsupportedEncodingException {
        if (parsedContentBytes) {
            return new String(contentBytes, charset);
        } else {
            throw new NegativeArraySizeException("content out of size for type String.");
        }
    }

    public <T> T getContentAsObject(IStringObjectSerializer processor, Class<T> clazz, String charset) throws IOException {
        String json = getContentAsString(charset);
        return (T) processor.deserialize(json, clazz);
    }

    public <T> T getContentAsRef(IStringObjectSerializer processor, Object refToken, String charset) throws IOException {
        String json = getContentAsString(charset);
        return (T) processor.deserialize(json, refToken);
    }

    public File saveAsFile(File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        StreamUtil.streamCopy(inputStream, fos, true);
        return file;
    }
}
