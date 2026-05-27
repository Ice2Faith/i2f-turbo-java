package i2f.design.pattern.concurrency.futurePromise.exception;

import i2f.design.pattern.concurrency.futurePromise.task.Future;

/**
 * 取消异常 —— 异步任务被取消时抛出的异常。
 *
 * <p>当通过 {@link Future#get()} 获取结果时，如果任务已被取消，
 * 将抛出此异常。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 14:00
 */
public class CancellationException extends RuntimeException {

    public CancellationException(String message) {
        super(message);
    }
}
