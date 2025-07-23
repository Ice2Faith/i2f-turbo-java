package i2f.lock.impl;

import i2f.lock.ILock;
import i2f.lock.IReadWriteLock;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Ice2Faith
 * @date 2025/7/23 16:13
 */
public class JdkReadWriteLock implements IReadWriteLock {
    protected ReadWriteLock lock = new ReentrantReadWriteLock();
    protected ILock readLock = new JdkLock(lock.readLock());
    protected ILock writeLock = new JdkLock(lock.writeLock());

    public JdkReadWriteLock() {
    }

    public JdkReadWriteLock(ReadWriteLock lock) {
        this.lock = lock;
        this.readLock = new JdkLock(lock.readLock());
        this.writeLock = new JdkLock(lock.writeLock());
    }

    @Override
    public ILock readLock() {
        return readLock;
    }

    @Override
    public ILock writeLock() {
        return writeLock;
    }
}
