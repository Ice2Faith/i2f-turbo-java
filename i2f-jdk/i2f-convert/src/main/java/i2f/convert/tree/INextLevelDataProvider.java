package i2f.convert.tree;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2022/3/27 22:30
 * @desc
 */
public interface INextLevelDataProvider<T> {
    List<T> getNextLevel(List<T> list);
}
