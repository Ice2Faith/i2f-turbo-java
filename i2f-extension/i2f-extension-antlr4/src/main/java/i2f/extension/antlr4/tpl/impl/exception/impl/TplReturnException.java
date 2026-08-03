package i2f.extension.antlr4.tpl.impl.exception.impl;

import i2f.extension.antlr4.tpl.impl.exception.TplControlException;

/**
 * @author Ice2Faith
 * @date 2025/2/25 9:01
 */
public class TplReturnException extends TplControlException {
    protected boolean hasRetValue = false;
    protected Object retValue;

    public TplReturnException() {
    }

    public TplReturnException(Object retValue) {
        this.hasRetValue = true;
        this.retValue = retValue;
    }

    public TplReturnException(String message) {
        super(message);
    }

    public TplReturnException(String message, Throwable cause) {
        super(message, cause);
    }

    public TplReturnException(Throwable cause) {
        super(cause);
    }

    public TplReturnException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public boolean isHasRetValue() {
        return hasRetValue;
    }

    public Object getRetValue() {
        return this.retValue;
    }
}
