package i2f.lock;

/**
 * @author Ice2Faith
 * @date 2022/4/15 8:43
 * @desc
 */
public interface INotify {
    void signal() throws Throwable;
    void signalAll() throws Throwable;
    void await() throws Throwable;
}
