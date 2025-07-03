package i2f.log.std.provider;

import i2f.log.std.ILogger;

/**
 * @author Ice2Faith
 * @date 2024/7/1 14:12
 * @desc
 */
public interface LoggerProvider {
    String getName();

    ILogger getLogger(String location);
}
