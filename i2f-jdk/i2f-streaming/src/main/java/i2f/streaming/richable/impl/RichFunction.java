package i2f.streaming.richable.impl;

import i2f.streaming.richable.BaseRichStreamProcessor;

import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/2/23 9:37
 * @desc
 */
public abstract class RichFunction<T, R> extends BaseRichStreamProcessor implements Function<T, R> {
}
