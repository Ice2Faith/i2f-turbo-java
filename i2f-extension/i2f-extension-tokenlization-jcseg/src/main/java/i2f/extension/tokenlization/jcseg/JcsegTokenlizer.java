package i2f.extension.tokenlization.jcseg;

import org.lionsoul.jcseg.tokenizer.core.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2025/3/26 21:17
 * @desc
 */
public class JcsegTokenlizer {
    public static JcsegTaskConfig config = new JcsegTaskConfig();
    public static ADictionary dic = DictionaryFactory
            .createDefaultDictionary(config);

    public static List<String> tokenlizeSplit(String text) throws Exception {
        return tokenlizeSplit(JcsegTaskConfig.COMPLEX_MODE, text);
    }

    public static List<IWord> tokenlize(String text) throws Exception {
        return tokenlize(JcsegTaskConfig.COMPLEX_MODE, text);
    }

    public static List<String> tokenlizeSplit(int mode, String text) throws Exception {
        return tokenlize(mode, text).stream().map(e -> e.getValue()).collect(Collectors.toList());
    }

    public static List<IWord> tokenlize(int mode, String text) throws Exception {
        List<IWord> ret = new ArrayList<>(text.length());
        // 创建分词器实例
        ISegment segment = getSegment(mode);
        segment.reset(new StringReader(text));
        IWord word = null;
        while ((word = segment.next()) != null) {
            ret.add(word);
        }
        return ret;
    }

    public static ISegment getSegment(int mode) throws JcsegException {
        return SegmentFactory.createJcseg(mode, new Object[]{config, dic});
    }
}
