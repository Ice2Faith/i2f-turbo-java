package i2f.translate.impl;

import i2f.translate.ITranslator;

/**
 * @author Ice2Faith
 * @date 2024/12/3 22:19
 * @desc
 */
public class ZhTone2AsciiTranslator implements ITranslator {
    public static final ZhTone2AsciiTranslator INSTANCE = new ZhTone2AsciiTranslator();

    @Override
    public String translate(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        StringBuilder builder = new StringBuilder();
        char[] arr = str.toCharArray();
        for (char ch : arr) {
            String s = ZhToneProvider.tone2asciiMap.get(ch + "");
            if (s != null) {
                builder.append(s);
            } else {
                builder.append(ch);
            }
        }
        return builder.toString();
    }
}
