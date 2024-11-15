package i2f.iterator.iterator.impl;


import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class LazyIterator<T> implements Iterator<T> {
    private final AtomicBoolean isRequire = new AtomicBoolean(false);
    private Supplier<Iterator<T>> supplier;
    private Iterator<T> iterator;

    public LazyIterator(Supplier<Iterator<T>> supplier) {
        this.supplier = supplier;
    }

    private void requireCheck() {
        if (isRequire.get()) {
            return;
        }
        synchronized (this) {
            if (!isRequire.get()) {
                this.iterator = supplier.get();
            }
            isRequire.set(true);
        }
    }

    @Override
    public boolean hasNext() {
        requireCheck();
        return iterator.hasNext();
    }

    @Override
    public T next() {
        requireCheck();
        return iterator.next();
    }

    @Override
    public void remove() {
        requireCheck();
        iterator.remove();
    }
}
