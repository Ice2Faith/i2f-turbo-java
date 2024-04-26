package i2f.streaming.richable.impl;

import i2f.streaming.richable.BaseRichStreamProcessor;

import java.util.function.BiFunction;

/**
 * @author Ice2Faith
 * @date 2024/2/23 9:37
 * @desc
 */
public abstract class RichBiFunction<T, U, R> extends BaseRichStreamProcessor implements BiFunction<T, U, R> {
}
