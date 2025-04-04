package i2f.extension.jce.bc.signature;

import i2f.crypto.impl.jdk.signature.SignatureType;
import i2f.extension.jce.bc.BcProvider;

/**
 * @author Ice2Faith
 * @date 2024/3/27 17:33
 * @desc
 */
public enum RsaType implements SignatureType {
    /**
     * 默认
     */
    SHA224withRSA("SHA224withRSA"),
    SHA256withRSA("SHA256withRSA"),
    SHA384withRSA("SHA384withRSA"),
    SHA512withRSA("SHA512withRSA"),
    RIPEMD128withRSA("RIPEMD128withRSA"),
    RIPEMD160withRSA("RIPEMD160withRSA"),
    ;

    private String type;

    private RsaType(String type) {
        this.type = type;
    }


    public static final int[] SECRET_BYTES_LEN = {1024, 2048};


    @Override
    public String provider() {
        return BcProvider.PROVIDER_NAME;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public String algorithmName() {
        return "RSA";
    }

    @Override
    public int[] secretBytesLen() {
        return SECRET_BYTES_LEN;
    }

}
