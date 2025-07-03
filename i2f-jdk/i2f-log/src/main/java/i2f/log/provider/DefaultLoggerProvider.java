package i2f.log.provider;

import i2f.log.holder.LogHolder;
import i2f.log.logger.DefaultLogger;
import i2f.log.std.ILogger;
import i2f.log.std.provider.LoggerProvider;

/**
 * @author Ice2Faith
 * @date 2025/7/3 17:10
 */
public class DefaultLoggerProvider implements LoggerProvider {

    public static final String NAME = "Default";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public ILogger getLogger(String location) {
        return new DefaultLogger(location, LogHolder.getDecider(), LogHolder.getWriter());
    }
}
