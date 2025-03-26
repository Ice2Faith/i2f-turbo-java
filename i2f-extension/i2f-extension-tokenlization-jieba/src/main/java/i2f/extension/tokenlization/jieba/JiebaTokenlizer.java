package i2f.extension.tokenlization.jieba;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/3/26 21:51
 * @desc
 */
public class JiebaTokenlizer {
    public static List<String> tokenlizeSplit(String text) throws Exception {
        JiebaSegmenter segmenter = getSegmenter();
        return segmenter.sentenceProcess(text);
    }

    public static List<SegToken> tokenlize(String text) throws Exception {
        return tokenlize(JiebaSegmenter.SegMode.SEARCH, text);
    }

    public static List<SegToken> tokenlize(JiebaSegmenter.SegMode mode, String text) throws Exception {
        // 创建分词器对象
        JiebaSegmenter segmenter = getSegmenter();
        List<SegToken> list = segmenter.process(text, mode);
        return list;
    }

    public static JiebaSegmenter getSegmenter() {
        return new JiebaSegmenter();
    }

}
