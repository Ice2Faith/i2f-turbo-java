package i2f.rowset.std.impl;

import i2f.rowset.std.IRowHeader;
import i2f.rowset.std.IRowSet;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Iterator;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/12/2 22:16
 * @desc
 */
@Data
@NoArgsConstructor
public class SimpleIteratorRowSet<T> implements IRowSet<T> {
    protected List<IRowHeader> headers;
    protected Iterator<T> iterator;

    public SimpleIteratorRowSet(List<IRowHeader> headers, Iterator<T> iterator) {
        this.headers = headers;
        this.iterator = iterator;
    }

    public SimpleIteratorRowSet(List<IRowHeader> headers, Iterable<T> iterable) {
        this.headers = headers;
        this.iterator = iterable.iterator();
    }

    @Override
    public List<IRowHeader> getHeaders() {
        return headers;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        return iterator.next();
    }


}
