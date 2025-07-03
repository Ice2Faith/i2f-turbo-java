package i2f.extension.tokenlization.jcseg.test;

import i2f.extension.tokenlization.jcseg.JcsegTokenlizer;
import org.lionsoul.jcseg.tokenizer.core.IWord;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/3/26 21:36
 * @desc
 */
public class TestJcsegTokenlizer {
    public static void main(String[] args) throws Exception {
        List<IWord> list = JcsegTokenlizer.tokenlize("福建省福州市马尾区马尾镇江滨东路鸿山公寓104栋101号");
        list.forEach(System.out::println);

        System.out.println("=================================");
        list = JcsegTokenlizer.tokenlize("福建福州市马尾马尾镇江滨东路鸿山公寓104栋101号");
        list.forEach(System.out::println);

        System.out.println("=================================");
        list = JcsegTokenlizer.tokenlize("福建厦门翔安区");
        list.forEach(System.out::println);

        System.out.println("=================================");
        List<String> words = JcsegTokenlizer.tokenlizeSplit("福建省福州市马尾区马尾镇江滨东路鸿山公寓104栋101号");
        words.forEach(System.out::println);
    }
}
