package i2f.codec.compress.deflate;

import i2f.codec.compress.IByteByteCodec;
import i2f.codec.exception.CodecException;
import i2f.codec.stream.compress.deflate.DeflateStreamCodecEx;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author Ice2Faith
 * @date 2023/6/27 9:41
 * @desc
 */
public class DeflateByteByteCodec implements IByteByteCodec {
    public static DeflateByteByteCodec INSTANCE = new DeflateByteByteCodec();

    @Override
    public byte[] encode(byte[] data) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DeflateStreamCodecEx.INSTANCE.encode(bis, bos);
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
            DeflateStreamCodecEx.INSTANCE.decode(bos, bis);
            return bos.toByteArray();
        } catch (Exception e) {
            throw new CodecException(e.getMessage(), e);
        }
    }

}
