package i2f.lru;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * LRU 性质的List包装器
 * 提供touch方法，此方法执行后将会把对应的元素放到开头
 * 以便于后续访问时，减少遍历的次数，以提高性能
 * 因此，此列表的元素位置是不固定的
 * 一般用于读多写少的场景
 * 是一种用于系统内部性能自调节的组件
 *
 * @author Ice2Faith
 * @date 2025/5/13 11:24
 */
public class LruList<E> implements List<E> {
    protected final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    protected final LinkedList<E> delegate = new LinkedList<>();

    public LruList() {

    }

    public LruList(E ... elems){
        this.delegate.addAll(Arrays.asList(elems));
    }


    public LruList(Iterable<E> iterable) {
        for (E item : iterable) {
            delegate.add(item);
        }
    }

    public LruList(Iterator<E> iterator){
        while(iterator.hasNext()){
            delegate.add(iterator.next());
        }
    }

    public LruList(Enumeration<E> enumeration){
        while(enumeration.hasMoreElements()){
            delegate.add(enumeration.nextElement());
        }
    }

    public static<E> LruList<E> of(E ... elems){
        return new LruList<>(elems);
    }

    public static<E> LruList<E> of(Iterable<E> iterable){
        return new LruList<>(iterable);
    }

    public static<E> LruList<E> of(Iterator<E> iterator){
        return new LruList<>(iterator);
    }

    public static<E> LruList<E> of(Enumeration<E> enumeration){
        return new LruList<>(enumeration);
    }

    public E touch(E val) {
        lock.writeLock().lock();
        try {
            if(!delegate.isEmpty()) {
                E head = delegate.get(0);
                if(head==val){
                    return head;
                }
            }
            boolean ok = delegate.remove(val);
            if (ok) {
                delegate.addFirst( val);
            }
            return val;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public E touch(int index) {
        lock.writeLock().lock();
        try {
            if(index==0){
                if(!delegate.isEmpty()) {
                    E head = delegate.get(0);
                    return head;
                }
            }
            E ret = delegate.remove(index);
            delegate.addFirst(ret);
            return ret;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public E touchFirst(Predicate<E> predicate){
        lock.writeLock().lock();
        try {
            E ret=null;
            for (E item : delegate) {
                if(predicate.test(item)){
                    ret=item;
                    touch(item);
                    break;
                }
            }
            return ret;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void touchIf(Predicate<E> predicate){
        lock.writeLock().lock();
        try {
            List<E> list=new ArrayList<>();
            Iterator<E> iterator = delegate.iterator();
            while(iterator.hasNext()){
                E val=iterator.next();
                if(predicate.test(val)){
                    list.add(val);
                    iterator.remove();
                }
            }
            for (E item : list) {
                delegate.add(0,item);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void syncDelegate(Consumer<List<E>> consumer){
        lock.writeLock().unlock();
        try{
            consumer.accept(delegate);
        }finally {
            lock.writeLock().unlock();
        }
    }

    public void sync(Consumer<LruList<E>> consumer){
        lock.writeLock().unlock();
        try{
            consumer.accept(this);
        }finally {
            lock.writeLock().unlock();
        }
    }

    public ReentrantReadWriteLock getLock(){
        return lock;
    }

    @Override
    public int size() {
        lock.readLock().lock();
        try {
            return delegate.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        lock.readLock().lock();
        try {
            return delegate.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean contains(Object o) {
        lock.readLock().lock();
        try {
            boolean ok= delegate.contains(o);
            return ok;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Iterator<E> iterator() {
        lock.readLock().lock();
        try {
            return new SyncWrappedIterator<>(delegate.iterator(),lock);
        } finally {
            lock.readLock().unlock();
        }
    }

    public static class SyncWrappedIterator<E> implements Iterator<E>{
        protected Iterator<E> delegate;
        protected ReadWriteLock lock=new ReentrantReadWriteLock();

        public SyncWrappedIterator(Iterator<E> delegate) {
            this.delegate = delegate;
        }

        public SyncWrappedIterator(Iterator<E> delegate, ReadWriteLock lock) {
            this.delegate = delegate;
            this.lock = lock;
        }

        @Override
        public boolean hasNext() {
            lock.readLock().lock();
            try{
                return delegate.hasNext();
            }finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public E next() {
            lock.readLock().lock();
            try{
                return delegate.next();
            }finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public void remove() {
            lock.writeLock().lock();
            try{
                delegate.remove();
            }finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            lock.readLock().lock();
            try{
                delegate.forEachRemaining(action);
            }finally {
                lock.readLock().unlock();
            }
        }
    }

    @Override
    public Object[] toArray() {
        lock.readLock().lock();
        try {
            return delegate.toArray();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public <T> T[] toArray(T[] a) {
        lock.readLock().lock();
        try {
            return delegate.toArray(a);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean add(E e) {
        lock.writeLock().lock();
        try {
            return delegate.add(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean remove(Object o) {
        lock.writeLock().lock();
        try {
            return delegate.remove(o);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        lock.readLock().lock();
        try {
            return delegate.containsAll(c);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        lock.writeLock().lock();
        try {
            return delegate.addAll(c);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        lock.writeLock().lock();
        try {
            return delegate.addAll(index,c);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        lock.writeLock().lock();
        try {
            return delegate.removeAll(c);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        lock.writeLock().lock();
        try {
            return delegate.retainAll(c);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void clear() {
        lock.writeLock().lock();
        try {
            delegate.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean equals(Object o) {
        lock.readLock().lock();
        try {
            return delegate.equals(o);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int hashCode() {
        lock.readLock().lock();
        try {
            return delegate.hashCode();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public E get(int index) {
        lock.writeLock().lock();
        try {
            return delegate.get(index);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public E set(int index, E element) {
        lock.writeLock().lock();
        try {
            return delegate.set(index,element);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void add(int index, E element) {
        lock.writeLock().lock();
        try {
             delegate.add(index,element);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public E remove(int index) {
        lock.writeLock().lock();
        try {
            return delegate.remove(index);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public int indexOf(Object o) {
        lock.writeLock().lock();
        try {
            int ret = delegate.indexOf(o);
            return ret;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public int lastIndexOf(Object o) {
        lock.readLock().lock();
        try {
            return delegate.lastIndexOf(o);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public ListIterator<E> listIterator() {
        lock.readLock().lock();
        try {
            return new SyncWrappedListIterator<>(delegate.listIterator(),lock);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        lock.readLock().lock();
        try {
            return new SyncWrappedListIterator<>(delegate.listIterator(index),lock);
        } finally {
            lock.readLock().unlock();
        }
    }

    public static class SyncWrappedListIterator<E> implements ListIterator<E> {
        protected ListIterator<E> delegate;
        protected ReadWriteLock lock=new ReentrantReadWriteLock();

        public SyncWrappedListIterator(ListIterator<E> delegate) {
            this.delegate = delegate;
        }

        public SyncWrappedListIterator(ListIterator<E> delegate, ReadWriteLock lock) {
            this.delegate = delegate;
            this.lock = lock;
        }

        @Override
        public boolean hasNext() {
            lock.readLock().lock();
            try{
                return delegate.hasNext();
            }finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public E next() {
            lock.readLock().lock();
            try{
                return delegate.next();
            }finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public boolean hasPrevious() {
            lock.readLock().lock();
            try{
                return delegate.hasPrevious();
            }finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public E previous() {
            lock.readLock().lock();
            try{
                return delegate.previous();
            }finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public int nextIndex() {
            lock.readLock().lock();
            try{
                return delegate.nextIndex();
            }finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public int previousIndex() {
            lock.readLock().lock();
            try{
                return delegate.previousIndex();
            }finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public void remove() {
            lock.writeLock().lock();
            try{
                delegate.remove();
            }finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void set(E e) {
            lock.writeLock().lock();
            try{
                delegate.set(e);
            }finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void add(E e) {
            lock.writeLock().lock();
            try{
                delegate.add(e);
            }finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            lock.readLock().lock();
            try{
                delegate.forEachRemaining(action);
            }finally {
                lock.readLock().unlock();
            }
        }
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        lock.readLock().lock();
        try {
            return delegate.subList(fromIndex, toIndex);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        lock.writeLock().lock();
        try {
            delegate.replaceAll(operator);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void sort(Comparator<? super E> c) {
        lock.writeLock().lock();
        try {
            delegate.sort(c);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Spliterator<E> spliterator() {
        lock.readLock().lock();
        try {
            return delegate.spliterator();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        lock.writeLock().lock();
        try {
            return delegate.removeIf(filter);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Stream<E> stream() {
        lock.readLock().lock();
        try {
            return delegate.stream();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Stream<E> parallelStream() {
        lock.readLock().lock();
        try {
            return delegate.parallelStream();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        lock.readLock().lock();
        try {
            delegate.forEach(action);
        } finally {
            lock.readLock().unlock();
        }
    }


    @Override
    public String toString() {
        lock.readLock().lock();
        try {
            return delegate.toString();
        } finally {
            lock.readLock().unlock();
        }
    }

}
