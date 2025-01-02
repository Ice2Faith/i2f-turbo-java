package i2f.uid;

/**
 * @author Ice2Faith
 * @date 2024/12/27 23:21
 * @desc
 */
public interface ILongUid extends IStringUid {
    long nextLongId();

    @Override
    default String nextStringId() {
        return String.format("%016x", nextLongId());
    }
}
