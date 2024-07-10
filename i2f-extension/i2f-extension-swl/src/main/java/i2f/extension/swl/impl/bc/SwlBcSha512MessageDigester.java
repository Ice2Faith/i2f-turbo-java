package i2f.extension.swl.impl.bc;

import i2f.codec.bytes.charset.CharsetStringByteCodec;
import i2f.codec.bytes.raw.HexStringByteCodec;
import i2f.extension.jce.bc.digest.md.BcMessageDigester;
import i2f.jce.std.digest.IMessageDigester;
import i2f.swl.consts.SwlCode;
import i2f.swl.exception.SwlException;
import i2f.swl.std.ISwlMessageDigester;

/**
 * @author Ice2Faith
 * @date 2024/7/10 19:40
 * @desc
 */
public class SwlBcSha512MessageDigester implements ISwlMessageDigester {
    private IMessageDigester digester= BcMessageDigester.SHA512;
    @Override
    public String digest(String data) {
        try {
            byte[] bytes = CharsetStringByteCodec.UTF8.decode(data);
            byte[] digest = digester.digest(bytes);
            return HexStringByteCodec.INSTANCE.encode(digest);
        } catch (Exception e) {
            throw new SwlException(SwlCode.DIGEST_SIGN_EXCEPTION.code(), e.getMessage(),e);
        }
    }

    @Override
    public boolean verify(String digest, String data) {
        try {
            byte[] digestBytes = HexStringByteCodec.INSTANCE.decode(digest);
            byte[] dataBytes = CharsetStringByteCodec.UTF8.decode(data);
            return digester.verify(digestBytes,dataBytes);
        } catch (Exception e) {
            throw new SwlException(SwlCode.DIGEST_VERIFY_EXCEPTION.code(), e.getMessage(),e);
        }
    }
}
