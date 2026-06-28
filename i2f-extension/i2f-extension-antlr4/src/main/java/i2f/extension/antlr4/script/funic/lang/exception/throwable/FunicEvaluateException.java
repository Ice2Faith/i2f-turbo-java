package i2f.extension.antlr4.script.funic.lang.exception.throwable;

import i2f.extension.antlr4.script.funic.lang.exception.FunicThrowException;

/**
 * @author Ice2Faith
 * @date 2025/2/25 9:00
 */
public class FunicEvaluateException extends FunicThrowException {
    public FunicEvaluateException() {
    }

    public FunicEvaluateException(String message) {
        super(message);
    }

    public FunicEvaluateException(String message, Throwable cause) {
        super(message, cause);
    }

    public FunicEvaluateException(Throwable cause) {
        super(cause);
    }

    public FunicEvaluateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
