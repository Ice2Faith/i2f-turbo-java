package i2f.crypto.impl.jdk.supports;

/**
 * @author Ice2Faith
 * @date 2024/3/27 14:31
 * @desc https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Cipher
 */
public enum CertPathBuilderAlgorithm {

    PKIX("PKIX"),
    ;

    private String text;

    CertPathBuilderAlgorithm(String text) {
        this.text = text;
    }

    public String text() {
        return this.text;
    }
}
