package i2f.relation.javacode.data;

/**
 * @author Ice2Faith
 * @date 2024/11/27 19:16
 * @desc
 */
public enum JavaRelationType {
    REAL(0, "real"),
    FIELD(1, "field"),
    METHOD(2, "method"),
    CONSTRUCTOR(3, "constructor"),
    SUPER(4, "super"),
    INTERFACE(5, "interface"),
    ANNOTATION(6, "annotation"),
    PARAMETER(7, "parameter"),
    RETURN(8, "return"),
    EXCEPTION(9, "exception"),
    ;

    private int code;
    private String text;

    private JavaRelationType(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int code() {
        return code;
    }

    public String text() {
        return text;
    }
}
