package i2f.crypto.impl.jdk.supports;

/**
 * @author Ice2Faith
 * @date 2024/3/27 14:31
 * @desc https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Cipher
 */
public enum KeyPairGeneratorAlgorithm {

    DiffieHellman("DiffieHellman"),
    DSA("DSA"),
    RSA("RSA"),

    RSASSA_PSS("RSASSA-PSS"),
    EC("EC"),
    ;

    private String text;

    KeyPairGeneratorAlgorithm(String text) {
        this.text = text;
    }

    public String text() {
        return this.text;
    }
}
