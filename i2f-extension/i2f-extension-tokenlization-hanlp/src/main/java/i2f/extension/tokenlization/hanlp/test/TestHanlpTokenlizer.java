package i2f.extension.tokenlization.hanlp.test;


import com.hankcs.hanlp.seg.common.Term;
import i2f.extension.tokenlization.hanlp.HanlpTokenlizer;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/3/26 21:42
 * @desc
 */
public class TestHanlpTokenlizer {
    public static void main(String[] args) throws Exception {
        List<Term> list = HanlpTokenlizer.tokenlize("福建省福州市马尾区马尾镇江滨东路鸿山公寓104栋101号");
        list.forEach(System.out::println);

        System.out.println("=================================");
        list = HanlpTokenlizer.tokenlize("福建福州市马尾马尾镇江滨东路鸿山公寓104栋101号");
        list.forEach(System.out::println);

        System.out.println("=================================");
        list = HanlpTokenlizer.tokenlize("福建厦门翔安区");
        list.forEach(System.out::println);

        System.out.println("=================================");
        List<String> words = HanlpTokenlizer.tokenlizeSplit("福建省福州市马尾区马尾镇江滨东路鸿山公寓104栋101号");
        words.forEach(System.out::println);
    }
}
