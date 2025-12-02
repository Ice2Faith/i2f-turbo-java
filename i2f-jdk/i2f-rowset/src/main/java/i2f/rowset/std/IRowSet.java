package i2f.rowset.std;

import java.util.Iterator;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/12/2 22:12
 * @desc
 */
public interface IRowSet<T> extends Iterator<T> {
    List<IRowHeader> getHeaders();

}
