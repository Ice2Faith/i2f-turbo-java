package i2f.serialize.str.text.exception;


import i2f.serialize.exception.SerializeException;

/**
 * @author Ice2Faith
 * @date 2022/4/14 10:18
 * @desc
 */
public class FormatTextSerializeException extends SerializeException {
    public FormatTextSerializeException() {
    }

    public FormatTextSerializeException(String message) {
        super(message);
    }

    public FormatTextSerializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public FormatTextSerializeException(Throwable cause) {
        super(cause);
    }
}
