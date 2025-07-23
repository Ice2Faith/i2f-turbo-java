package i2f.lock.impl;

import i2f.lock.INotifyLock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Ice2Faith
 * @date 2025/7/23 16:13
 */
public class JdkLock implements INotifyLock {
    protected Lock lock = new ReentrantLock();
    protected Condition cond = lock.newCondition();

    public JdkLock() {
    }

    public JdkLock(Lock lock) {
        this.lock = lock;
    }

    public JdkLock(Lock lock, Condition cond) {
        this.lock = lock;
        this.cond = cond;
    }

    @Override
    public void lock() throws Throwable {
        lock.lock();
    }

    @Override
    public void unlock() throws Throwable {
        lock.unlock();
    }

    @Override
    public void signal() throws Throwable {
        cond.signal();
    }

    @Override
    public void signalAll() throws Throwable {
        cond.signalAll();
    }

    @Override
    public void await() throws Throwable {
        cond.await();
    }
}
