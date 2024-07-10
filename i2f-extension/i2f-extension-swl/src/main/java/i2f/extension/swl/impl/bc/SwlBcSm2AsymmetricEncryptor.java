package i2f.extension.swl.impl.bc;

import i2f.codec.bytes.charset.CharsetStringByteCodec;
import i2f.codec.bytes.raw.HexStringByteCodec;
import i2f.extension.jce.bc.encrypt.asymmetric.BcSm2Encryptor;
import i2f.jce.std.encrypt.asymmetric.IAsymmetricEncryptor;
import i2f.jce.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.swl.consts.SwlCode;
import i2f.swl.exception.SwlException;
import i2f.swl.std.ISwlAsymmetricEncryptor;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author Ice2Faith
 * @date 2024/7/10 18:59
 * @desc
 */
public class SwlBcSm2AsymmetricEncryptor implements ISwlAsymmetricEncryptor {
    private IAsymmetricEncryptor encryptor=new BcSm2Encryptor();
    @Override
    public AsymKeyPair generateKeyPair() {
        try {
            KeyPair keyPair = BcSm2Encryptor.genKeyPair();
            String publicKey= null;
            PublicKey pubKey = keyPair.getPublic();
            if(pubKey!=null){
                publicKey= HexStringByteCodec.INSTANCE.encode(pubKey.getEncoded());
            }
            String privateKey= null;
            PrivateKey priKey = keyPair.getPrivate();
            if(priKey!=null){
                privateKey=HexStringByteCodec.INSTANCE.encode(priKey.getEncoded());
            }
            return new AsymKeyPair(publicKey,privateKey);
        } catch (Exception e) {
            throw new SwlException(SwlCode.ASYMMETRIC_INVALID_KEY_EXCEPTION.code(),e.getMessage(),e);
        }
    }

    @Override
    public AsymKeyPair getKeyPair() {
        KeyPair keyPair = encryptor.getKeyPair();
        String publicKey= null;
        PublicKey pubKey = keyPair.getPublic();
        if(pubKey!=null){
            publicKey=HexStringByteCodec.INSTANCE.encode(pubKey.getEncoded());
        }
        String privateKey= null;
        PrivateKey priKey = keyPair.getPrivate();
        if(priKey!=null){
            privateKey=HexStringByteCodec.INSTANCE.encode(priKey.getEncoded());
        }
        return new AsymKeyPair(publicKey,privateKey);
    }

    @Override
    public void setKeyPair(AsymKeyPair key) {
        String publicKey = key.getPublicKey();
        if(publicKey!=null){
            byte[] encoded = HexStringByteCodec.INSTANCE.decode(publicKey);
            encryptor.setPublicKeyBytes(encoded);
        }
        String privateKey = key.getPrivateKey();
        if(privateKey!=null){
            byte[] encoded = HexStringByteCodec.INSTANCE.decode(privateKey);
            encryptor.setPrivateKeyBytes(encoded);
        }
    }

    @Override
    public String getPublicKey() {
        byte[] publicKeyBytes = encryptor.getPublicKeyBytes();
        return HexStringByteCodec.INSTANCE.encode(publicKeyBytes);
    }

    @Override
    public void setPublicKey(String publicKey) {
        encryptor.setPublicKeyBytes(HexStringByteCodec.INSTANCE.decode(publicKey));
    }

    @Override
    public String getPrivateKey() {
        byte[] privateKeyBytes = encryptor.getPrivateKeyBytes();
        return HexStringByteCodec.INSTANCE.encode(privateKeyBytes);
    }

    @Override
    public void setPrivateKey(String privateKey) {
        encryptor.setPrivateKeyBytes(HexStringByteCodec.INSTANCE.decode(privateKey));
    }

    @Override
    public String encrypt(String data) {
        try {
            byte[] bytes = CharsetStringByteCodec.UTF8.decode(data);
            byte[] enc = encryptor.encrypt(bytes);
            return HexStringByteCodec.INSTANCE.encode(enc);
        } catch (Exception e) {
            throw new SwlException(SwlCode.ASYMMETRIC_ENCRYPT_EXCEPTION.code(), e.getMessage(), e);
        }
    }

    @Override
    public String decrypt(String data) {
        try {
            byte[] bytes = HexStringByteCodec.INSTANCE.decode(data);
            byte[] enc = encryptor.decrypt(bytes);
            return CharsetStringByteCodec.UTF8.encode(enc);
        } catch (Exception e) {
            throw new SwlException(SwlCode.ASYMMETRIC_DECRYPT_EXCEPTION.code(), e.getMessage(), e);
        }
    }

    @Override
    public String sign(String data) {
        try {
            byte[] bytes = CharsetStringByteCodec.UTF8.decode(data);
            byte[] sign = encryptor.sign(bytes);
            return HexStringByteCodec.INSTANCE.encode(sign);
        } catch (Exception e) {
            throw new SwlException(SwlCode.ASYMMETRIC_SIGN_EXCEPTION.code(), e.getMessage(), e);
        }
    }

    @Override
    public boolean verify(String sign, String data) {
        try {
            byte[] signBytes = HexStringByteCodec.INSTANCE.decode(sign);
            byte[] dataBytes = CharsetStringByteCodec.UTF8.decode(data);
            return encryptor.verify(signBytes, dataBytes);
        } catch (Exception e) {
            throw new SwlException(SwlCode.ASYMMETRIC_VERIFY_EXCEPTION.code(), e.getMessage(), e);
        }
    }
}
