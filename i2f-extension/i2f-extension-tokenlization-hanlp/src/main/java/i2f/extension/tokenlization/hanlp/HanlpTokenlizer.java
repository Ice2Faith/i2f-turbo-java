package i2f.extension.tokenlization.hanlp;

import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2025/3/26 22:00
 * @desc
 */
public class HanlpTokenlizer {
    public static enum Mode {
        STANDARD,
        NLP,
        SPEED,
        NOTIONAL,
        INDEX,
        TRA_CN
    }

    public static List<String> tokenlizeSplit(String text) throws Exception {
        return tokenlizeSplit(Mode.STANDARD, text);
    }

    public static List<String> tokenlizeSplit(Mode mode, String text) throws Exception {
        return tokenlize(mode, text).stream().map(e -> e.word).collect(Collectors.toList());
    }

    public static List<Term> tokenlize(String text) throws Exception {
        return tokenlize(Mode.STANDARD, text);
    }

    public static List<Term> tokenlize(Mode mode, String text) throws Exception {
        if (Mode.STANDARD == mode) {
            List<Term> terms = StandardTokenizer.segment(text);
            return terms;
        } else if (Mode.NLP == mode) {
            List<Term> terms = NLPTokenizer.segment(text);
            return terms;
        } else if (Mode.SPEED == mode) {
            List<Term> terms = SpeedTokenizer.segment(text);
            return terms;
        } else if (Mode.NOTIONAL == mode) {
            List<Term> terms = NotionalTokenizer.segment(text);
            return terms;
        } else if (Mode.INDEX == mode) {
            List<Term> terms = IndexTokenizer.segment(text);
            return terms;
        } else if (Mode.TRA_CN == mode) {
            List<Term> terms = TraditionalChineseTokenizer.segment(text);
            return terms;
        }
        List<Term> terms = StandardTokenizer.segment(text);
        return terms;
    }
}
