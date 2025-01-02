package i2f.serialize.std.exception;

import i2f.codec.std.exception.CodecException;

public class SerializeException extends CodecException {
    public SerializeException() {
    }

    public SerializeException(String message) {
        super(message);
    }

    public SerializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializeException(Throwable cause) {
        super(cause);
    }

}
