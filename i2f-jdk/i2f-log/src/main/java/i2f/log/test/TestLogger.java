package i2f.log.test;

import i2f.clock.SystemClock;
import i2f.log.ILogger;
import i2f.log.LoggerFactory;
import i2f.log.config.LogConfiguration;
import i2f.log.decide.ILogDecider;
import i2f.log.decide.impl.DefaultClassNamePattenLogDecider;
import i2f.log.enums.LogLevel;
import i2f.log.format.impl.StringFormatLogMsgFormatter;
import i2f.log.holder.LogHolder;
import i2f.log.stdout.StdoutRedirectPrintStream;
import i2f.log.writer.DefaultBroadcastLogWriter;
import i2f.log.writer.ILogWriter;
import i2f.log.writer.impl.LocalFilePlanTextLogWriter;

import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2024/7/1 11:16
 * @desc
 */
public class TestLogger {
    public static ILogger staticLogger = LoggerFactory.getLogger();

    public static void main(String[] args) {
        LogConfiguration.config();
        StdoutRedirectPrintStream.redirectSysoutSyserr();
        ILogWriter writer = LogHolder.GLOBAL_WRITER;
        if (writer instanceof DefaultBroadcastLogWriter) {
            DefaultBroadcastLogWriter broadcastLogWriter = (DefaultBroadcastLogWriter) writer;
            broadcastLogWriter.getAsync().set(false);
            LocalFilePlanTextLogWriter fileWriter = new LocalFilePlanTextLogWriter();
            fileWriter.setFileLimitSize(3 * 1024 * 1024);
            fileWriter.setFileLimitTotalSize(9 * 1024 * 1024);
            LogHolder.registryWriter("FILE", fileWriter);
        }

        ILogDecider decider = LogHolder.GLOBAL_DECIDER;
        if (decider instanceof DefaultClassNamePattenLogDecider) {
            DefaultClassNamePattenLogDecider pattenLogDecider = (DefaultClassNamePattenLogDecider) decider;
            pattenLogDecider.setRootLevel(LogLevel.ALL);
//            LogHolder.registryDecideLevel("i2f.log.**", LogLevel.WARN);
        }

        // 设置为使用String.format
        LogHolder.GLOBAL_MSG_FORMATTER = new StringFormatLogMsgFormatter();
        staticLogger.info("this is main args: %s", Arrays.toString(args));

        System.out.println("sysout");

        // 设置为使用RegexUtil.format
        LogHolder.GLOBAL_MSG_FORMATTER = LogHolder.DEFAULT_MSG_FORMATTER;
        long bts = SystemClock.currentTimeMillis();
        for (int i = 0; i < 1 * 10000; i++) {
            ILogger logger = LoggerFactory.getLogger();
            logger.info("main scope logger: {}", SystemClock.currentTimeMillis());

            logger.debug("debug level,{}", SystemClock.currentTimeMillis());

            try {
                int a = 1 / 0;
            } catch (Exception e) {
                logger.error(e, "calculate error: { e:}", e.getMessage());
            }
        }
        long ets = SystemClock.currentTimeMillis();
        System.out.println("useTs:" + (ets - bts));
    }
}
