package i2f.log.format.impl;

import i2f.log.format.ILogMsgFormatter;
import i2f.match.regex.RegexUtil;

/**
 * @author Ice2Faith
 * @date 2024/7/3 9:20
 * @desc
 */
public class IndexedPattenLogMsgFormatter implements ILogMsgFormatter {
    @Override
    public String format(String format, Object... args) {
        if (format == null) {
            return null;
        }
        if (args == null || args.length == 0) {
            return format;
        }
        return RegexUtil.format(format, args);
    }
}
