package i2f.extension.antlr4.tpl.impl.exception.impl;

import i2f.extension.antlr4.tpl.impl.exception.TplThrowException;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Ice2Faith
 * @date 2025/2/25 9:00
 */
@Getter
@Setter
public class TplParseException extends TplThrowException {
    protected int line;
    protected int column;

    public TplParseException() {
    }

    public TplParseException(String message) {
        super(message);
    }

    public TplParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public TplParseException(Throwable cause) {
        super(cause);
    }

    public TplParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
