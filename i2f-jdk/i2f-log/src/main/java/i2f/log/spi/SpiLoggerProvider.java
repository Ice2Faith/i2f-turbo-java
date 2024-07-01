package i2f.log.spi;

import i2f.log.ILogger;

/**
 * @author Ice2Faith
 * @date 2024/7/1 14:12
 * @desc
 */
public interface SpiLoggerProvider {
    String getName();

    boolean test();

    ILogger getLogger(String location);
}
