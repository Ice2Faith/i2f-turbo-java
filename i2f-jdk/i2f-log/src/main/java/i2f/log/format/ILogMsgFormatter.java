package i2f.log.format;

/**
 * @author Ice2Faith
 * @date 2024/7/3 9:18
 * @desc
 */
public interface ILogMsgFormatter {
    String format(String format, Object... args);
}
