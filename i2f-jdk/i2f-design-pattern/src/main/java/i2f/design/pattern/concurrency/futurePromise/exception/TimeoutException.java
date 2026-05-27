package i2f.design.pattern.concurrency.futurePromise.exception;

import i2f.design.pattern.concurrency.futurePromise.task.Future;

/**
 * 超时异常 —— 异步任务等待超时时抛出的异常。
 *
 * <p>当通过 {@link Future#get(long)} 获取结果时，如果超过指定时间任务仍未完成，
 * 将抛出此异常。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 14:00
 */
public class TimeoutException extends Exception {

    public TimeoutException(String message) {
        super(message);
    }
}
