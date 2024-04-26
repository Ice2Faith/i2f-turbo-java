package i2f.convert.tree;

import java.util.Collection;

/**
 * @author Ice2Faith
 * @date 2022/3/31 14:27
 * @desc
 */
public interface IChildren<T> {
    Collection<T> getChildren();

    void cleanChildren();
}
