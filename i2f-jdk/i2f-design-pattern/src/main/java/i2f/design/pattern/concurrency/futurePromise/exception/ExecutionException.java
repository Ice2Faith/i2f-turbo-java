package i2f.design.pattern.concurrency.futurePromise.exception;

import i2f.design.pattern.concurrency.futurePromise.task.Future;

/**
 * 执行异常 —— 异步任务执行过程中发生的异常。
 *
 * <p>当通过 {@link Future#get()} 获取结果时，如果任务执行失败，
 * 将抛出此异常，原始异常可通过 {@link #getCause()} 获取。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 14:00
 */
public class ExecutionException extends Exception {

    public ExecutionException(String message) {
        super(message);
    }

    public ExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExecutionException(Throwable cause) {
        super(cause);
    }
}
