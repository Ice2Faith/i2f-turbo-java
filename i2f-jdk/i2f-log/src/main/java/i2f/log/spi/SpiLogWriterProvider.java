package i2f.log.spi;

import i2f.log.writer.DefaultBroadcastLogWriter;
import i2f.log.writer.ILogWriter;

/**
 * @author Ice2Faith
 * @date 2024/7/1 14:18
 * @desc
 */
public interface SpiLogWriterProvider {
    String getName();

    boolean test();

    ILogWriter getWriter();

    default void loaded(DefaultBroadcastLogWriter logWriter) {

    }
}
