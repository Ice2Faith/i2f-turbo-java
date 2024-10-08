package i2f.spring.tx;

public class TxUnhandledException extends RuntimeException {
    public TxUnhandledException() {
    }

    public TxUnhandledException(String message) {
        super(message);
    }

    public TxUnhandledException(String message, Throwable cause) {
        super(message, cause);
    }

    public TxUnhandledException(Throwable cause) {
        super(cause);
    }
}
