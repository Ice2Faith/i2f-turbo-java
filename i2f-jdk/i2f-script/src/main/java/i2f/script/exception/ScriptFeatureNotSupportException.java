package i2f.script.exception;

import javax.script.ScriptException;

/**
 * @author Ice2Faith
 * @date 2024/8/4 11:46
 * @desc
 */
public class ScriptFeatureNotSupportException extends ScriptException {
    public ScriptFeatureNotSupportException(String s) {
        super(s);
    }

    public ScriptFeatureNotSupportException(Exception e) {
        super(e);
    }

    public ScriptFeatureNotSupportException(String message, String fileName, int lineNumber) {
        super(message, fileName, lineNumber);
    }

    public ScriptFeatureNotSupportException(String message, String fileName, int lineNumber, int columnNumber) {
        super(message, fileName, lineNumber, columnNumber);
    }
}
