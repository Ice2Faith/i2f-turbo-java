package i2f.springboot.dynamic.datasource.exception;

/**
 * @author Ice2Faith
 * @date 2024/5/22 21:14
 * @desc
 */
public class DataSourceNotFoundException extends RuntimeException {
    public DataSourceNotFoundException() {
    }

    public DataSourceNotFoundException(String message) {
        super(message);
    }

    public DataSourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataSourceNotFoundException(Throwable cause) {
        super(cause);
    }
}
