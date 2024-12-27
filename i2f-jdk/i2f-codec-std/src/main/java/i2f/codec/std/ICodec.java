package i2f.codec.std;

/**
 * @author Ice2Faith
 * @date 2023/6/19 15:34
 * @desc
 */
public interface ICodec<E, D> {
    E encode(D data);

    D decode(E enc);
}
