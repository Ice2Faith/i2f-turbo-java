package i2f.thread;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

/**
 * @author Ice2Faith
 * @date 2024/8/20 20:44
 * @desc
 */
@Data
@NoArgsConstructor
public class ThreadTaskInfo<T> {
    protected Callable<T> task;
    protected CompletableFuture<T> future;
    protected long submitTs;
    protected long triggerTs;
    protected long finishTs;
    protected Throwable throwable;

    public ThreadTaskInfo(Callable<T> task, CompletableFuture<T> future) {
        this.task = task;
        this.future = future;
        this.submitTs = System.currentTimeMillis();
    }
}
