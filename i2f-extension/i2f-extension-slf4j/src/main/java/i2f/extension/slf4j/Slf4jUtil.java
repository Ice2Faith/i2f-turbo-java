package i2f.extension.slf4j;

import i2f.trace.ThreadTrace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ice2Faith
 * @date 2022/3/26 15:50
 * @desc
 */
public class Slf4jUtil {
    public static Logger getLogger() {
        String className = ThreadTrace.currentClassName();
        Logger logger = LoggerFactory.getLogger(className);
        return logger;
    }

    public static Logger getMethodLogger() {
        StackTraceElement elem = ThreadTrace.current();
        String className = elem.getClassName();
        String method = elem.getMethodName();
        Logger logger = LoggerFactory.getLogger(className + "->" + method);
        return logger;
    }

    public static Logger getDebugLogger() {
        StackTraceElement elem = ThreadTrace.current();
        String tag = elem.getClassName() + "->" + elem.getMethodName() + " in file:" + elem.getFileName() + " of line:" + elem.getLineNumber();
        Logger logger = LoggerFactory.getLogger(tag);
        return logger;
    }
}
