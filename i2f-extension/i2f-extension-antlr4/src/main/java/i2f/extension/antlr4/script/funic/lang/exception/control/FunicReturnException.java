package i2f.extension.antlr4.script.funic.lang.exception.control;

import i2f.extension.antlr4.script.funic.lang.exception.FunicControlException;

/**
 * @author Ice2Faith
 * @date 2025/2/25 9:01
 */
public class FunicReturnException extends FunicControlException {
    protected boolean hasRetValue = false;
    protected Object retValue;

    public FunicReturnException() {
    }

    public FunicReturnException(String message, Object retValue) {
        super(message);
        this.hasRetValue = true;
        this.retValue = retValue;
    }

    public FunicReturnException(String message) {
        super(message);
    }

    public FunicReturnException(String message, Throwable cause) {
        super(message, cause);
    }

    public FunicReturnException(Throwable cause) {
        super(cause);
    }

    public FunicReturnException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public boolean isHasRetValue() {
        return hasRetValue;
    }

    public Object getRetValue() {
        return this.retValue;
    }
}
