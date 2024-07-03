package i2f.log.format.impl;

import i2f.log.format.ILogMsgFormatter;


/**
 * @author Ice2Faith
 * @date 2024/7/3 9:19
 * @desc
 */
public class StringFormatLogMsgFormatter implements ILogMsgFormatter {
    @Override
    public String format(String format, Object... args) {
        if (format == null) {
            return null;
        }
        if (args == null || args.length == 0) {
            return format;
        }
        return String.format(format, args);
    }
}
