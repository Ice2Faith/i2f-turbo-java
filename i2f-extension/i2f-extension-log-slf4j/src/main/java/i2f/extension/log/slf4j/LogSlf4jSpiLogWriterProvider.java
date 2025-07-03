package i2f.extension.log.slf4j;

import i2f.log.provider.LogWriterProvider;
import i2f.log.writer.DefaultBroadcastLogWriter;
import i2f.log.writer.ILogWriter;
import i2f.log.writer.impl.StdoutPlanTextLogWriter;


/**
 * @author Ice2Faith
 * @date 2024/7/1 14:26
 * @desc
 */
public class LogSlf4jSpiLogWriterProvider implements LogWriterProvider {
    public static final String CLASS_NAME = "org.slf4j.Logger";

    @Override
    public String getName() {
        return LogSlf4jLogWriter.WRITER_NAME;
    }

    @Override
    public boolean test() {
        try {
            Class<?> clazz = Class.forName(CLASS_NAME);
            if (clazz != null) {
                return true;
            }
        } catch (Throwable e) {

        }
        try {
            Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(CLASS_NAME);
            if (clazz != null) {
                return true;
            }
        } catch (Throwable e) {

        }
        return false;
    }

    @Override
    public ILogWriter getWriter() {
        return new LogSlf4jLogWriter();
    }

    @Override
    public void loaded(DefaultBroadcastLogWriter writer) {
        writer.remove(StdoutPlanTextLogWriter.WRITER_NAME);
    }
}
