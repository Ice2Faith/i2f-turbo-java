package i2f.builder;

/**
 * @author Ice2Faith
 * @date 2026/7/2 16:38
 * @desc
 */
public interface BaseBuilder<T extends BaseBuilder<T>> {
    default Builder<T> toBuilder() {
        return Builder.of((T) this);
    }
}
