package i2f.environment.std;

/**
 * @author Ice2Faith
 * @date 2024/8/9 8:51
 * @desc
 */
public interface IWritableEnvironment extends IEnvironment {
    void setProperty(String name, String value);

    default void setInteger(String name, Integer value) {
        setProperty(name, value == null ? null : String.valueOf(value));
    }

    default void setLong(String name, Long value) {
        setProperty(name, value == null ? null : String.valueOf(value));
    }

    default void setBoolean(String name, Boolean value) {
        setProperty(name, value == null ? null : String.valueOf(value));
    }

    default void setDouble(String name, Double value) {
        setProperty(name, value == null ? null : String.valueOf(value));
    }
}
