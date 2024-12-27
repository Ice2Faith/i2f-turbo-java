package i2f.uid;

/**
 * @author Ice2Faith
 * @date 2024/12/27 23:21
 * @desc
 */
public interface IIntUid extends IStringUid {
    int nextIntId();

    @Override
    default String nextStringId() {
        return String.format("%08x", nextIntId());
    }
}
