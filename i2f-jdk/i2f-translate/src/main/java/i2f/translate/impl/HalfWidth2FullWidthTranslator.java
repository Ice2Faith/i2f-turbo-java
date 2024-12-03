package i2f.translate.impl;

import i2f.translate.ITranslator;

/**
 * @author Ice2Faith
 * @date 2024/12/3 20:37
 * @desc 半角 转 全角
 */
public class HalfWidth2FullWidthTranslator implements ITranslator {
    public static final HalfWidth2FullWidthTranslator INSTANCE = new HalfWidth2FullWidthTranslator();

    @Override
    public String translate(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        StringBuilder builder = new StringBuilder();
        char[] arr = str.toCharArray();
        for (char ch : arr) {
            if (ch >= 0 && ch <= 127) {
                String s = FullAndHalfProvider.half2fullMap.get(ch + "");
                if (s != null) {
                    builder.append(s);
                } else {
                    builder.append(ch);
                }
            } else {
                builder.append(ch);
            }

        }
        return builder.toString();
    }
}
