package i2f.context.std;

/**
 * @author Ice2Faith
 * @date 2024/8/9 9:32
 * @desc
 */
public interface IWritableContext extends IContext {
    void addBean(Object bean);

    void removeBean(Object bean);
}
