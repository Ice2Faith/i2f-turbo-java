package i2f.translate.zh2pingyin.impl;

import i2f.translate.ITranslator;
import i2f.translate.impl.FullWidth2HalfWidthTranslator;
import i2f.translate.impl.ZhTone2AsciiTranslator;
import i2f.translate.zh2pingyin.data.Zh2PinyinVo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2024/12/1 10:10
 */
@Data
@NoArgsConstructor
public class Zh2PinyinTranslator implements ITranslator {
    protected boolean keepTone = true;

    public Zh2PinyinTranslator(boolean keepTone) {
        this.keepTone = keepTone;
    }

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
                    String pinYin = info.getPinYin();
                    if (pinYin == null) {
                        builder.append(ch);
                    } else {
                        pinYin = FullWidth2HalfWidthTranslator.INSTANCE.translate(pinYin);
                        if (!keepTone) {
                            pinYin = ZhTone2AsciiTranslator.INSTANCE.translate(pinYin);
                        }
                        builder.append(pinYin);
                    }
                }
            }
        }
        return builder.toString();
    }

}
