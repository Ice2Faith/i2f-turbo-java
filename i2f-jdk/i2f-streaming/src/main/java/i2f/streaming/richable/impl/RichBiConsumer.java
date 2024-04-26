package i2f.streaming.richable.impl;

import i2f.streaming.richable.BaseRichStreamProcessor;

import java.util.function.BiConsumer;

/**
 * @author Ice2Faith
 * @date 2024/2/23 9:37
 * @desc
 */
public abstract class RichBiConsumer<T, U> extends BaseRichStreamProcessor implements BiConsumer<T, U> {
}
