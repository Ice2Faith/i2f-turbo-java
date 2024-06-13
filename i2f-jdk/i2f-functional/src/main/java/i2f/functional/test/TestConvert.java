package i2f.functional.test;

import i2f.functional.exception.UnDeclaredFunctionalException;
import i2f.functional.func.except.impl.IExFunction1;

import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/5/17 20:58
 * @desc
 */
public class TestConvert {
    public static void main(String[] args) {

    }

    public static <T, R> Function<T, R> convert(IExFunction1<R, T> ret) {
        return new Function<T, R>() {
            @Override
            public R apply(T t) {
                try {
                    return ret.apply(t);
                } catch (Throwable e) {
                    throw new UnDeclaredFunctionalException(e.getMessage(), e);
                }
            }
        };
    }
}
