package i2f.event;

/**
 * @author Ice2Faith
 * @date 2024/8/9 21:01
 * @desc
 */
public interface IEventPublisher<T> {
    void publish(T event);

    void subscribe(IEventSubscriber<T> subscriber);
}
