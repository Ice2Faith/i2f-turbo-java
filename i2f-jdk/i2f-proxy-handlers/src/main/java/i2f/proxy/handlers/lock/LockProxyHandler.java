package i2f.proxy.handlers.lock;

import i2f.annotations.ext.lock.Lock;
import i2f.invokable.IInvokable;
import i2f.invokable.method.impl.jdk.JdkMethod;
import i2f.lock.ILock;
import i2f.lock.ILockProvider;
import i2f.lock.impl.JdkCacheLockProvider;
import i2f.proxy.std.IProxyInvocationHandler;

import java.lang.reflect.Method;

/**
 * @author Ice2Faith
 * @date 2025/7/23 16:31
 */
public class LockProxyHandler implements IProxyInvocationHandler {
    protected ILockProvider lockProvider = new JdkCacheLockProvider();

    public LockProxyHandler() {
    }

    public LockProxyHandler(ILockProvider lockProvider) {
        this.lockProvider = lockProvider;
    }

    @Override
    public Object invoke(Object ivkObj, IInvokable invokable, Object... args) throws Throwable {
        if (!(invokable instanceof JdkMethod)) {
            throw new IllegalStateException("un-support invokable type=" + invokable.getClass());
        }

        JdkMethod jdkMethod = (JdkMethod) invokable;
        Method method = jdkMethod.getMethod();

        Lock ann = method.getDeclaredAnnotation(Lock.class);
        if (ann == null) {
            return invokable.invoke(ivkObj, args);
        }

        String lockName = "";
        if (ann.clazz()) {
            lockName = lockName + "#" + method.getDeclaringClass().getName();
        }
        if (ann.method()) {
            lockName = lockName + "#" + method.getName();
        }
        lockName = lockName + "#" + ann.value();

        ILock lock = lockProvider.getLock(lockName);
        lock.lock();
        try {
            return invokable.invoke(ivkObj, args);
        } finally {
            lock.unlock();
        }
    }
}
