package i2f.extension.antlr4.script.tiny.impl;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.lang.reflect.Method;

/**
 * @author Ice2Faith
 * @date 2025/3/5 10:35
 */
public class DefaultAntlrErrorListener extends BaseErrorListener {

    public static final DefaultAntlrErrorListener INSTANCE = new DefaultAntlrErrorListener();

    protected static volatile Object SLF4J_LOGGER;
    protected static volatile Method SLF4J_ERROR_METHOD;

    static {
        Class<?> clazz = null;
        try {
            clazz = Thread.currentThread().getContextClassLoader().loadClass("org.slf4j.LoggerFactory");
        } catch (Throwable e) {

        }
        if (clazz == null) {
            try {
                clazz = Class.forName("org.slf4j.LoggerFactory");
            } catch (Throwable e) {

            }
        }
        if (clazz == null) {
            SLF4J_LOGGER = null;
        }
        try {
            Method method = clazz.getMethod("getLogger", Class.class);
            if (method != null) {
                SLF4J_LOGGER = method.invoke(null, DefaultAntlrErrorListener.class);
            }
        } catch (Exception e) {
        }
        if (SLF4J_LOGGER != null) {
            try {
                Class<?> cls = SLF4J_LOGGER.getClass();
                SLF4J_ERROR_METHOD = cls.getMethod("error", String.class, Throwable.class);
                SLF4J_ERROR_METHOD.setAccessible(true);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        String errorMsg = "line " + line + ":" + charPositionInLine + " " + msg;
        logError(errorMsg, e);
        throw e;
    }

    public void logError(String msg, Throwable e) {
        if (SLF4J_ERROR_METHOD != null) {
            try {
                SLF4J_ERROR_METHOD.invoke(SLF4J_LOGGER, msg, e);
            } catch (Exception ex) {
            }
            return;
        }
        System.err.println(msg);
        e.printStackTrace();
    }

}
