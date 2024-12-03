package i2f.translate.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/12/3 22:11
 * @desc
 */
public class ZhToneProvider {
    public static final String TONE = "āáǎàōóǒòēéěèīíǐìūúǔù";
    public static final String ASCII = "aaaaooooeeeeiiiiuuuu";
    public static final Map<String, String> tone2asciiMap = Collections.unmodifiableMap(((Supplier<Map<String, String>>) () -> {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < TONE.length(); i++) {
            char tone = TONE.charAt(i);
            char ascii = ASCII.charAt(i);
            map.put(tone + "", ascii + "");
        }
        return map;
    }).get());
}
