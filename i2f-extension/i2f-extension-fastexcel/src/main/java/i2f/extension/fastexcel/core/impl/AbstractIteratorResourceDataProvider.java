package i2f.extension.fastexcel.core.impl;

import i2f.extension.fastexcel.core.ExcelExportPage;
import i2f.extension.fastexcel.core.IDataProvider;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2025/12/18 8:30
 * @desc
 */
public abstract class AbstractIteratorResourceDataProvider<T> implements IDataProvider, Closeable {
    protected Class<T> elemClass;

    protected final AtomicBoolean hasInit=new AtomicBoolean(false);
    protected transient Iterator<T> iterator;

    public AbstractIteratorResourceDataProvider(Class<T> elemClass) {
        this.elemClass = elemClass;
    }

    @Override
    public boolean supportPage() {
        return true;
    }

    public Iterator<T> getIterator(){
        if(hasInit.getAndSet(true)){
            return this.iterator;
        }
        this.iterator= initIterator();
        return this.iterator;
    }

    public abstract Iterator<T> initIterator();

    @Override
    public List<?> getData(ExcelExportPage page) {
        List<T> ret=new LinkedList<>();
        Iterator<T> iterator = getIterator();
        if(iterator==null){
            return ret;
        }
        int count=0;
        while(count<page.getSize() && iterator.hasNext()){
            T next = iterator.next();
            ret.add(next);
            count++;
        }
        if(count<page.getSize()){
            try {
                close();
            } catch (IOException e) {

            }
        }
        return ret;
    }

    @Override
    public Class<?> getDataClass() {
        return elemClass;
    }

    @Override
    public void close() throws IOException {
        try {
            releaseResource(this.iterator);
        } catch (Exception e) {

        }
        this.iterator=null;
    }

    public abstract void releaseResource(Iterator<T> iterator) throws Exception;

    @Override
    protected void finalize() throws Throwable {
        close();
    }
}
