package i2f.translate.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/12/3 21:06
 * @desc 全角-半角 转换提供器
 */
public class FullAndHalfProvider {
    public static final String EN_HALF = "qwertyuiopasdfghjklzxcvbnm1234567890QWERTYUIOPASDFGHJKLZXCVBNMg`~!@#$%^&*()_+-=[]\\{}|;':\",./<>?";
    public static final String EN_FULL = "ｑｗｅｒｔｙｕｉｏｐａｓｄｆｇｈｊｋｌｚｘｃｖｂｎｍ１２３４５６７８９０ＱＷＥＲＴＹＵＩＯＰＡＳＤＦＧＨＪＫＬＺＸＣＶＢＮＭɡ｀～！＠＃＄％＾＆＊（）＿＋－＝［］＼｛｝｜；＇：＂，．／＜＞？";
    public static final String ZH_HALF = " `~!@#$%^&*()_+-=[]\\{}|;'':\"\",./<>?";
    public static final String ZH_FULL = "　·～！＠＃￥％…＆×（）—＋－＝【】＼｛｝｜；‘’：“”，。、《》？";
    public static final Map<String, String> full2halfMap = Collections.unmodifiableMap(((Supplier<Map<String, String>>) () -> {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < EN_HALF.length(); i++) {
            char half = EN_HALF.charAt(i);
            char full = EN_FULL.charAt(i);
            map.put(full + "", half + "");
        }
        for (int i = 0; i < ZH_HALF.length(); i++) {
            char half = ZH_HALF.charAt(i);
            char full = ZH_FULL.charAt(i);
            map.put(full + "", half + "");
        }
        return map;
    }).get());
    public static final Map<String, String> half2fullMap = Collections.unmodifiableMap(((Supplier<Map<String, String>>) () -> {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, String> entry : full2halfMap.entrySet()) {
            map.put(entry.getValue(), entry.getKey());
        }
        return map;
    }).get());
}
