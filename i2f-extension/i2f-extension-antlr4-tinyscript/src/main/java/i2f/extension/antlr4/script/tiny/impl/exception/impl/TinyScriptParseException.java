package i2f.extension.antlr4.script.tiny.impl.exception.impl;

import i2f.extension.antlr4.script.tiny.impl.exception.TinyScriptThrowException;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Ice2Faith
 * @date 2025/2/25 9:00
 */
@Getter
@Setter
public class TinyScriptParseException extends TinyScriptThrowException {
    protected int line;
    protected int column;

    public TinyScriptParseException() {
    }

    public TinyScriptParseException(String message) {
        super(message);
    }

    public TinyScriptParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public TinyScriptParseException(Throwable cause) {
        super(cause);
    }

    public TinyScriptParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
