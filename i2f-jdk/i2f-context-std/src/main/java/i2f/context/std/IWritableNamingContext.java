package i2f.context.std;

/**
 * @author Ice2Faith
 * @date 2024/8/9 9:33
 * @desc
 */
public interface IWritableNamingContext extends INamingContext {
    void addBean(String name, Object bean);

    void removeBean(String name);
}
