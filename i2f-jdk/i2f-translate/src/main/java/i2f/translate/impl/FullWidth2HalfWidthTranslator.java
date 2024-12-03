package i2f.translate.impl;

import i2f.translate.ITranslator;

/**
 * @author Ice2Faith
 * @date 2024/12/3 20:37
 * @desc 全角 转 半角
 */
public class FullWidth2HalfWidthTranslator implements ITranslator {
    public static final FullWidth2HalfWidthTranslator INSTANCE = new FullWidth2HalfWidthTranslator();

    @Override
    public String translate(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        StringBuilder builder = new StringBuilder();
        char[] arr = str.toCharArray();
        for (char ch : arr) {
            if (ch >= 0 && ch <= 127) {
                builder.append(ch);
            } else {
                String s = FullAndHalfProvider.full2halfMap.get(ch + "");
                if (s != null) {
                    builder.append(s);
                } else {
                    builder.append(ch);
                }
            }

        }
        return builder.toString();
    }
}
