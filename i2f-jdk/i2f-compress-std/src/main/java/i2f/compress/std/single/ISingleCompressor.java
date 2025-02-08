package i2f.compress.std.single;

import java.io.*;

/**
 * @author Ice2Faith
 * @date 2024/6/29 15:39
 * @desc
 */
public interface ISingleCompressor {
    void compress(InputStream is, OutputStream os) throws IOException;

    default byte[] compressAsBytes(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        compress(is, bos);
        bos.close();
        return bos.toByteArray();
    }

    default byte[] compressAsBytes(byte[] data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        compress(bis, bos);
        bos.close();
        bis.close();
        return bos.toByteArray();
    }

    void release(InputStream is, OutputStream os) throws IOException;

    default byte[] releaseAsBytes(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        release(is, bos);
        bos.close();
        return bos.toByteArray();
    }

    default byte[] releaseAsBytes(byte[] data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        release(bis, bos);
        bos.close();
        bis.close();
        return bos.toByteArray();
    }
}
