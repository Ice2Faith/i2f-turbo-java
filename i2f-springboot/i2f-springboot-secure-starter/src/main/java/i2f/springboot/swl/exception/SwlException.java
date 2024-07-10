package i2f.springboot.swl.exception;

/**
 * @author Ice2Faith
 * @date 2024/7/10 9:47
 * @desc
 */
public class SwlException extends RuntimeException {
    private int code;

    public SwlException(int code) {
        this.code = code;
    }

    public SwlException(int code, String message) {
        super(message);
        this.code = code;
    }

    public SwlException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public SwlException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public int code() {
        return this.code;
    }
}
