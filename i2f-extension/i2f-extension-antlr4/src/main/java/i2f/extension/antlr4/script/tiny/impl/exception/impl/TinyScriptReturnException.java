package i2f.extension.antlr4.script.tiny.impl.exception.impl;

import i2f.extension.antlr4.script.tiny.impl.exception.TinyScriptControlException;

/**
 * @author Ice2Faith
 * @date 2025/2/25 9:01
 */
public class TinyScriptReturnException extends TinyScriptControlException {
    protected boolean hasRetValue=false;
    protected Object retValue;
    public TinyScriptReturnException() {
    }

    public TinyScriptReturnException(Object retValue) {
        this.hasRetValue=true;
        this.retValue = retValue;
    }

    public TinyScriptReturnException(String message) {
        super(message);
    }

    public TinyScriptReturnException(String message, Throwable cause) {
        super(message, cause);
    }

    public TinyScriptReturnException(Throwable cause) {
        super(cause);
    }

    public TinyScriptReturnException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public boolean isHasRetValue() {
        return hasRetValue;
    }

    public Object getRetValue(){
        return this.retValue;
    }
}
