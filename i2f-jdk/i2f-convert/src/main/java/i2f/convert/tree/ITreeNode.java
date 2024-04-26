package i2f.convert.tree;


/**
 * @author Ice2Faith
 * @date 2021/9/29
 */
public interface ITreeNode<T> {
    void asMyChild(T val);

    boolean isMyChild(T val);

    boolean isMyParent(T val);
}
