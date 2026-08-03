package i2f.extension.antlr4.funvi.impl.exception.impl;

import i2f.extension.antlr4.funvi.impl.exception.FunviThrowException;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Ice2Faith
 * @date 2025/2/25 9:00
 */
@Getter
@Setter
public class FunviParseException extends FunviThrowException {
    protected int line;
    protected int column;

    public FunviParseException() {
    }

    public FunviParseException(String message) {
        super(message);
    }

    public FunviParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public FunviParseException(Throwable cause) {
        super(cause);
    }

    public FunviParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
