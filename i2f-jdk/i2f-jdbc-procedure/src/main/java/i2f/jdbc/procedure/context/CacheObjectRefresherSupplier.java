package i2f.jdbc.procedure.context;

import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2025/3/5 20:54
 * @desc
 */
@NoArgsConstructor
public abstract class CacheObjectRefresherSupplier<R, T> {
    public static final String DEFAULT_THREAD_NAME = "cache-object-refresher";
    protected volatile T cache = null;
    protected volatile Thread refreshThread = null;
    protected AtomicBoolean refreshing = new AtomicBoolean(false);

    protected String threadName = DEFAULT_THREAD_NAME;

    public CacheObjectRefresherSupplier(T cache) {
        this.cache = cache;
    }

    public CacheObjectRefresherSupplier(T cache, String threadName) {
        this.cache = cache;
        this.threadName = threadName;
    }


    public R get() {
        if (isMissingCache()) {
            synchronized (this) {
                if (isMissingCache()) {
                    refresh();
                }
            }
        }
        T ret = cache;
        return wrapGet(ret);
    }

    public boolean isMissingCache() {
        return cache == null;
    }

    public R getDirect() {
        T ret = cache;
        return wrapGet(ret);
    }

    public T getCache() {
        return cache;
    }

    public abstract R wrapGet(T ret);

    public void startRefreshThread(long intervalSeconds) {
        if (intervalSeconds < 0) {
            refreshing.set(false);
            if (refreshThread != null) {
                refreshThread.interrupt();
            }
            refreshThread = null;
            return;
        }
        refreshing.set(true);
        Thread thread = new Thread(() -> {
            while (refreshing.get()) {
                try {
                    refresh();
                } catch (Exception e) {
                }
                try {
                    Thread.sleep(intervalSeconds * 1000);
                } catch (Exception e) {
                }
            }
        });
        thread.setName(threadName == null ? DEFAULT_THREAD_NAME : threadName);
        thread.setDaemon(true);
        thread.start();
        refreshThread = thread;
    }

    public abstract void refresh();

}
