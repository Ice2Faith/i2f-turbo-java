package i2f.extension.jce.bc.supports;

/**
 * @author Ice2Faith
 * @date 2024/3/27 14:31
 * @desc https://www.bouncycastle.org/documentation/specification_interoperability/#algorithms-and-key-types
 */
public enum MacAlgorithm {

    HmacMD5("HmacMD5"),

    HmacSHA1("HmacSHA1"),
    HmacSHA224("HmacSHA224"),
    HmacSHA256("HmacSHA256"),
    HmacSHA384("HmacSHA384"),
    HmacSHA512("HmacSHA512"),

    HmacRIPEMD128("HmacRIPEMD128"),
    HmacRIPEMD160("HmacRIPEMD160"),
    HmacRIPEMD256("HmacRIPEMD256"),
    HmacRIPEMD320("HmacRIPEMD320"),

    HmacSHA512_224("HmacSHA512/224"),
    HmacSHA512_256("HmacSHA512/256"),

    HmacSM3("HmacSM3"),

    HmacTiger("HmacTiger"),
    HmacWhirlpool("HmacWhirlpool"),

    ;

    private String text;

    MacAlgorithm(String text) {
        this.text = text;
    }

    public String text() {
        return this.text;
    }
}
