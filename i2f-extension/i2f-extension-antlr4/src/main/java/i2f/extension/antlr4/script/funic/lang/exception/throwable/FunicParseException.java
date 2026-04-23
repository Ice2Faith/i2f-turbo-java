package i2f.extension.antlr4.script.funic.lang.exception.throwable;

import i2f.extension.antlr4.script.funic.lang.exception.FunicThrowException;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Ice2Faith
 * @date 2025/2/25 9:00
 */
@Getter
@Setter
public class FunicParseException extends FunicThrowException {
    protected int line;
    protected int column;

    public FunicParseException() {
    }

    public FunicParseException(String message) {
        super(message);
    }

    public FunicParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public FunicParseException(Throwable cause) {
        super(cause);
    }

    public FunicParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
