package i2f.translate.zh2pingyin.impl;

import i2f.translate.ITranslator;
import i2f.translate.zh2pingyin.data.Zh2PinyinVo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2024/12/1 10:10
 */
@Data
@NoArgsConstructor
public class Zh2TraTranslator implements ITranslator {

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
                Zh2PinyinVo info = PinyinProvider.PROVIDER.getWordInfo(ch + "");
                if (info == null) {
                    builder.append(ch);
                } else {
                    String word = info.getOldWord();
                    if (word == null) {
                        builder.append(ch);
                    } else {
                        builder.append(word);
                    }
                }
            }
        }
        return builder.toString();
    }

}
