package i2f.extension.antlr4.script.funic.lang.exception.control;

import i2f.extension.antlr4.script.funic.lang.exception.FunicControlException;

/**
 * @author Ice2Faith
 * @date 2025/2/25 9:01
 */
public class FunicContinueException extends FunicControlException {
    public FunicContinueException() {
    }

    public FunicContinueException(String message) {
        super(message);
    }

    public FunicContinueException(String message, Throwable cause) {
        super(message, cause);
    }

    public FunicContinueException(Throwable cause) {
        super(cause);
    }

    public FunicContinueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
