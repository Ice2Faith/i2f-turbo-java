package i2f.extension.jce.bc.supports;

import i2f.extension.jce.bc.BcProvider;

/**
 * @author Ice2Faith
 * @date 2024/3/27 14:31
 * @desc https://www.bouncycastle.org/documentation/specification_interoperability/#algorithms-and-key-types
 */
public enum MessageDigestAlgorithm {


    MD5("MD5"),

    SHA_1("SHA-1"),
    SHA224("SHA224"),
    SHA256("SHA256"),
    SHA384("SHA384"),
    SHA512("SHA512"),

    SHA512_224("SHA512/224"),
    SHA512_256("SHA512/256"),

    SHAKE_128("SHAKE-128"),
    SHAKE_256("SHAKE-256"),

    SHA3_224("SHA3-224"),
    SHA3_256("SHA3-256"),
    SHA3_384("SHA3-384"),
    SHA3_512("SHA3-512"),

    SM3("SM3"),
    Tiger("Tiger"),
    Whirlpool("Whirlpool"),
    ;

    static {
        BcProvider.registryProvider();
    }

    private String text;

    MessageDigestAlgorithm(String text) {
        this.text = text;
    }

    public String text() {
        return this.text;
    }
}
