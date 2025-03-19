package i2f.invokable;


/**
 * @author Ice2Faith
 * @date 2022/3/25 20:38
 * @desc
 */
public interface IInvokable {
    Object invoke(Object ivkObj,
                  Object... args) throws Throwable;
}
