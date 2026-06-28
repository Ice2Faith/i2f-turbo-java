package i2f.extension.antlr4.script.funic.lang.exception.throwable;

import i2f.extension.antlr4.script.funic.lang.exception.FunicThrowException;

/**
 * @author Ice2Faith
 * @date 2026/4/23 19:59
 * @desc
 */
public class FunicRejectException extends FunicThrowException {
    public FunicRejectException() {
    }

    public FunicRejectException(String message) {
        super(message);
    }

    public FunicRejectException(String message, Throwable cause) {
        super(message, cause);
    }

    public FunicRejectException(Throwable cause) {
        super(cause);
    }
}
