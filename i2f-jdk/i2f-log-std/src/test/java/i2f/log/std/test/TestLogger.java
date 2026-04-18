package i2f.log.std.test;

import i2f.log.std.ILogger;
import i2f.log.std.LoggerFactory;
import i2f.trace.mdc.MdcHolder;
import i2f.trace.mdc.MdcTraces;

/**
 * @author Ice2Faith
 * @date 2025/7/3 20:01
 * @desc
 */
public class TestLogger {
    private static ILogger logger = LoggerFactory.getLogger(TestLogger.class);

    public static void main(String[] args) {
        logger.info("Hello World");
        MdcHolder.put(MdcTraces.TRACE_ID, MdcTraces.genTraceId());
        for (int i = 0; i < 10; i++) {
            logger.infoArgs("number:", i);
        }
    }
}
