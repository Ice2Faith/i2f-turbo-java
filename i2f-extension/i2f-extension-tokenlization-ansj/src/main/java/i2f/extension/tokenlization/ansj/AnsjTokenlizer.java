package i2f.extension.tokenlization.ansj;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2025/3/26 21:40
 * @desc
 */
public class AnsjTokenlizer {
    public static List<String> tokenlizeSplit(String text) throws Exception {
        return tokenlize(text).stream().map(e -> e.getName()).collect(Collectors.toList());
    }

    public static List<Term> tokenlize(String text) throws Exception {
        List<Term> terms = ToAnalysis.parse(text).getTerms();
        return terms;
    }

}
