package i2f.type.tuple.std;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/3/4 8:57
 * @desc
 */
public interface Tuple extends Iterable<Object>, Serializable {

    int size();

    Object get(int index);

    void set(int index, Object value);

    List<Object> toList();

    default boolean isEmpty() {
        return size() == 0;
    }

    default Object[] toArray() {
        return toList().toArray();
    }

    @Override
    default Iterator<Object> iterator() {
        return toList().iterator();
    }

}
