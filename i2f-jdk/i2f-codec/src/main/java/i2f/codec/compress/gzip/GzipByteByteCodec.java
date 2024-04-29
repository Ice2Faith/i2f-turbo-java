package i2f.codec.compress.gzip;

import i2f.codec.compress.IByteByteCodec;
import i2f.codec.exception.CodecException;
import i2f.codec.stream.compress.gzip.GzipStreamCodecEx;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author Ice2Faith
 * @date 2023/6/27 9:41
 * @desc
 */
public class GzipByteByteCodec implements IByteByteCodec {
    public static GzipByteByteCodec INSTANCE = new GzipByteByteCodec();

    @Override
    public byte[] encode(byte[] data) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            GzipStreamCodecEx.INSTANCE.encode(bis, bos);
            return bos.toByteArray();
        } catch (Exception e) {
            throw new CodecException(e.getMessage(), e);
        }
    }

    @Override
    public byte[] decode(byte[] enc) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(enc);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            GzipStreamCodecEx.INSTANCE.decode(bos, bis);
            return bos.toByteArray();
        } catch (Exception e) {
            throw new CodecException(e.getMessage(), e);
        }
    }
}
