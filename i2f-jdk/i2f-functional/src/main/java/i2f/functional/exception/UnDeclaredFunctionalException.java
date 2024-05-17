package i2f.functional.exception;

/**
 * @author Ice2Faith
 * @date 2024/5/17 20:47
 * @desc
 */
public class UnDeclaredFunctionalException extends RuntimeException {
    public UnDeclaredFunctionalException() {
    }

    public UnDeclaredFunctionalException(String message) {
        super(message);
    }

    public UnDeclaredFunctionalException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnDeclaredFunctionalException(Throwable cause) {
        super(cause);
    }
}
