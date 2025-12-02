package i2f.rowset.std.impl;

import i2f.rowset.std.IRowHeader;
import i2f.rowset.std.IRowSet;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * @author Ice2Faith
 * @date 2025/12/2 22:19
 * @desc
 */
@Data
@NoArgsConstructor
public class SimpleCollectionMapRowSet implements IRowSet<Map<String,Object>> {
    protected List<IRowHeader> headers;
    protected Collection<Map<String,Object>> collection;
    private Iterator<Map<String,Object>> iterator;

    public SimpleCollectionMapRowSet(List<String> headers, List<Map<String, Object>> collection) {
        this.collection=collection;
        this.iterator=collection.iterator();
        this.headers=new ArrayList<>();
        for (String header : headers) {
            this.headers.add(new SimpleRowHeader(header));
        }
    }

    public SimpleCollectionMapRowSet(Collection<Map<String,Object>> collection) {
        this.collection=collection;
        this.iterator=collection.iterator();
        this.headers=new ArrayList<>();
        for(Map<String,Object> row : collection){
            for (String key : row.keySet()) {
                this.headers.add(new SimpleRowHeader(key));
            }
            break;
        }
    }

    @Override
    public List<IRowHeader> getHeaders() {
        if(headers==null || headers.isEmpty()){
            headers=new ArrayList<>();
        }

        return headers;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Map<String, Object> next() {
        return iterator.next();
    }
}
