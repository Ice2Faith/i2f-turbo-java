package i2f.extension.antlr4.script.funic.lang.exception.control;

import i2f.extension.antlr4.script.funic.lang.exception.FunicControlException;

/**
 * @author Ice2Faith
 * @date 2025/2/25 9:01
 */
public class FunicBreakException extends FunicControlException {
    public FunicBreakException() {
    }

    public FunicBreakException(String message) {
        super(message);
    }

    public FunicBreakException(String message, Throwable cause) {
        super(message, cause);
    }

    public FunicBreakException(Throwable cause) {
        super(cause);
    }

    public FunicBreakException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
