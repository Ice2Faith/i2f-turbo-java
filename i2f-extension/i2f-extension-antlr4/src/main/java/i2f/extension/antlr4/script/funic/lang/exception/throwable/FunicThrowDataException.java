package i2f.extension.antlr4.script.funic.lang.exception.throwable;

import i2f.extension.antlr4.script.funic.lang.exception.FunicThrowException;

/**
 * @author Ice2Faith
 * @date 2026/4/22 16:54
 * @desc
 */
public class FunicThrowDataException extends FunicThrowException {
    protected Object data;

    public FunicThrowDataException(Object data) {
        this.data = data;
    }

    public FunicThrowDataException(Object data, String message) {
        super(message);
        this.data = data;
    }

    public FunicThrowDataException(Object data, String message, Throwable cause) {
        super(message, cause);
        this.data = data;
    }

    public FunicThrowDataException(Object data, Throwable cause) {
        super(cause);
        this.data = data;
    }

    public Object getData() {
        return this.data;
    }
}
