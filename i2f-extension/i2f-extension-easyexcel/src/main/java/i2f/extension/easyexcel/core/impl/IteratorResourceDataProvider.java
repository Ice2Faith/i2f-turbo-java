package i2f.extension.easyexcel.core.impl;

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2025/12/18 8:30
 * @desc
 */
public class IteratorResourceDataProvider<C,T> extends AbstractIteratorResourceDataProvider<T> {
    protected C context;
    protected Function<C,Iterator<T>> supplier;
    protected BiConsumer<Iterator<T>,C> finisher;

    public IteratorResourceDataProvider(Class<T> elemClass){
        super(elemClass);
    }

    public IteratorResourceDataProvider(C context, Class<T> elemClass, Function<C, Iterator<T>> supplier, BiConsumer<Iterator<T>, C> finisher) {
        super(elemClass);
        this.context = context;
        this.elemClass = elemClass;
        this.supplier = supplier;
        this.finisher = finisher;
    }

    @Override
    public Iterator<T> initIterator(){
        return supplier.apply(context);
    }

    @Override
    public void releaseResource(Iterator<T> iterator) throws Exception {
        finisher.accept(iterator,context);
    }

}
