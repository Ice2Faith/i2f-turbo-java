package i2f.serialize.std.str.xml.exception;

import i2f.serialize.std.exception.SerializeException;

/**
 * @author Ice2Faith
 * @date 2022/4/2 14:13
 * @desc
 */
public class XmlSerializeException extends SerializeException {
    public XmlSerializeException() {
    }

    public XmlSerializeException(String message) {
        super(message);
    }

    public XmlSerializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public XmlSerializeException(Throwable cause) {
        super(cause);
    }
}
