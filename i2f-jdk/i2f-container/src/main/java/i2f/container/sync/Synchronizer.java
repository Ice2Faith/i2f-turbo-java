package i2f.container.sync;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/5/19 22:21
 * @desc
 */
public class Synchronizer<T> {
    private volatile T target;
    private ReadWriteLock lock=new ReentrantReadWriteLock();

    public Synchronizer(T target) {
        this.target = target;
    }

    public Synchronizer(T target, ReadWriteLock lock) {
        this.target = target;
        this.lock = lock;
    }

    public T get(){
        lock.readLock().lock();
        try{
            return target;
        }finally {
            lock.readLock().unlock();
        }
    }

    public <R> R readFunction(Function<T,R> function){
        lock.readLock().lock();
        try{
            return function.apply(target);
        }finally {
            lock.readLock().unlock();
        }
    }

    public <R,U> R readFunction(BiFunction<T,U,R> function,U value){
        lock.readLock().lock();
        try{
            return function.apply(target,value);
        }finally {
            lock.readLock().unlock();
        }
    }

    public void readConsumer(Consumer<T> consumer){
        lock.readLock().lock();
        try{
            consumer.accept(target);
        }finally {
            lock.readLock().unlock();
        }
    }

    public<U> void readConsumer(BiConsumer<T,U> consumer,U value){
        lock.readLock().lock();
        try{
            consumer.accept(target,value);
        }finally {
            lock.readLock().unlock();
        }
    }

    public <R> R writeFunction(Function<T,R> function){
        lock.writeLock().lock();
        try{
            return function.apply(target);
        }finally {
            lock.writeLock().unlock();
        }
    }

    public <R,U> R writeFunction(BiFunction<T,U,R> function,U value){
        lock.writeLock().lock();
        try{
            return function.apply(target,value);
        }finally {
            lock.writeLock().unlock();
        }
    }

    public void writeConsumer(Consumer<T> consumer){
        lock.writeLock().lock();
        try{
            consumer.accept(target);
        }finally {
            lock.writeLock().unlock();
        }
    }

    public<U> void writeConsumer(BiConsumer<T,U> consumer,U value){
        lock.writeLock().lock();
        try{
            consumer.accept(target,value);
        }finally {
            lock.writeLock().unlock();
        }
    }
}
