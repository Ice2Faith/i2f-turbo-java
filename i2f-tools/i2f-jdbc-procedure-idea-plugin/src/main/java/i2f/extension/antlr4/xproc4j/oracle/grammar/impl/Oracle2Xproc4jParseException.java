package i2f.extension.antlr4.xproc4j.oracle.grammar.impl;


/**
 * @author Ice2Faith
 * @date 2025/2/25 9:00
 */
public class Oracle2Xproc4jParseException extends RuntimeException {
    protected int line;
    protected int column;

    public Oracle2Xproc4jParseException() {
    }

    public Oracle2Xproc4jParseException(String message) {
        super(message);
    }

    public Oracle2Xproc4jParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public Oracle2Xproc4jParseException(Throwable cause) {
        super(cause);
    }

    public Oracle2Xproc4jParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public int getLine() {
        return line;
    }

    public Oracle2Xproc4jParseException line(int line) {
        this.line = line;
        return this;
    }

    public int getColumn() {
        return column;
    }

    public Oracle2Xproc4jParseException column(int column) {
        this.column = column;
        return this;
    }
}
