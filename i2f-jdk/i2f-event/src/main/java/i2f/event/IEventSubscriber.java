package i2f.event;

/**
 * @author Ice2Faith
 * @date 2024/8/9 21:06
 * @desc
 */
@FunctionalInterface
public interface IEventSubscriber<T> {
    default boolean test(T event) {
        return true;
    }

    void handle(T event);
}
