package i2f.extension.antlr4.funvi.lang.exception.impl;

import i2f.extension.antlr4.funvi.lang.exception.FunviControlException;

/**
 * @author Ice2Faith
 * @date 2025/2/25 9:01
 */
public class FunviReturnException extends FunviControlException {
    protected boolean hasRetValue = false;
    protected Object retValue;

    public FunviReturnException() {
    }

    public FunviReturnException(Object retValue) {
        this.hasRetValue = true;
        this.retValue = retValue;
    }

    public FunviReturnException(String message) {
        super(message);
    }

    public FunviReturnException(String message, Throwable cause) {
        super(message, cause);
    }

    public FunviReturnException(Throwable cause) {
        super(cause);
    }

    public FunviReturnException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public boolean isHasRetValue() {
        return hasRetValue;
    }

    public Object getRetValue() {
        return this.retValue;
    }
}
