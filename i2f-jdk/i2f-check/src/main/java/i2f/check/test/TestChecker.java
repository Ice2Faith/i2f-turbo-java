package i2f.check.test;

import i2f.check.Checker;
import i2f.check.Predicates;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2024/4/23 10:11
 * @desc
 */
public class TestChecker {
    public static void main(String[] args) {
        try {
            Checker.begin()
                    .test(1, Predicates::isNull, v -> "值应该为null")
                    .exceptMessage(msg -> new IllegalArgumentException(msg));
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            Checker.begin()
                    .not(null, Predicates::isNull, v -> "值不能为null")
                    .exceptMessage(msg -> new IllegalStateException(msg));
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            Checker.begin(new int[]{1, 2, 3})
                    .not(Predicates::isNull, v -> "数组不能为null")
                    .not(Predicates::isEmptyIntArray, v -> "数组长度不能为0")
                    .map(e -> {
                        List<Integer> ret = new ArrayList<>();
                        for (int i = 0; i < e.length; i++) {
                            ret.add(e[i]);
                        }
                        return ret;
                    })
                    .test(list -> list.contains(2), e -> "列表应该包含2")
                    .map(list -> list
                            .stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(","))
                    )
                    .next("123456789")
                    .not(Predicates::isBlankString, e -> "不能为空字符串")
                    .next(12.125)
                    .not(Predicates::isNull, e -> "数值不能为null")
                    .map(String::valueOf)
                    .not(str -> str.contains("."), e -> "字符串不应该包含小数点")
                    .exceptMessage(e -> new IllegalArgumentException(e));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
