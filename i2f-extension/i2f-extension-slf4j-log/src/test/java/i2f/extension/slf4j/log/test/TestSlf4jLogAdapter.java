package i2f.extension.slf4j.log.test;

import i2f.log.decide.impl.DefaultClassNamePattenLogDecider;
import i2f.log.holder.LogHolder;
import i2f.log.std.enums.LogLevel;
import i2f.log.std.mdc.LogMdcHolder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ice2Faith
 * @date 2024/7/23 19:53
 * @desc
 */
@Slf4j
public class TestSlf4jLogAdapter {
    public static void main(String[] args) throws Exception {
        DefaultClassNamePattenLogDecider defaultDecider = (DefaultClassNamePattenLogDecider) LogHolder.DEFAULT_DECIDER;
        defaultDecider.setRootLevel(LogLevel.ALL);

        LogMdcHolder.getOrNewTraceId();

        Logger logger = LoggerFactory.getLogger(TestSlf4jLogAdapter.class);
        logger.info("adapter");

        LogMdcHolder.removeTraceId();

        log.error("lombok");

        log.warn("warn");

        log.debug("debug");

        log.trace("trace");

        Thread.sleep(2000);
    }
}
