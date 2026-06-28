package i2f.design.pattern.concurrency.futurePromise.task;

import i2f.design.pattern.concurrency.futurePromise.exception.CancellationException;
import i2f.design.pattern.concurrency.futurePromise.exception.ExecutionException;
import i2f.design.pattern.concurrency.futurePromise.exception.TimeoutException;

/**
 * Future/Promise 模式 —— Future（读取端：任务调用者使用）
 *
 * <p><b>角色：</b>异步结果的读取者（Consumer Side）</p>
 *
 * <p><b>模式说明：</b>Future 代表一个"未来的结果"——任务调用者（如顾客）提交异步任务后，
 * 立即获得一个 Future 对象，可以在未来某个时刻通过它获取结果。
 * Future 与 Promise 共享同一个结果槽位，但 Future 只能读取，不能写入。</p>
 *
 * <p><b>命名立意：</b>以"餐厅取餐凭据"为场景——
 * 顾客点单后获得取餐凭据（Future），凭此凭据可以在未来取餐。
 * 顾客可以选择等待（阻塞 get）、轮询（isDone 检查）或放弃（cancel）。</p>
 *
 * <p><b>状态流转：</b></p>
 * <pre>
 *  Pending（进行中）
 *      ↓
 *  ├─→ Done（完成）    → get() 返回结果
 *  ├─→ Failed（失败）   → get() 抛出异常
 *  └─→ Cancelled（已取消） → get() 抛出异常
 * </pre>
 *
 * @param <T> 异步结果的类型
 * @author Ice2Faith
 * @date 2026/5/21 14:00
 * @see Promise
 * @see FutureTask
 */
public interface Future<T> {

    /**
     * 获取异步任务的结果（阻塞等待）。
     *
     * <p>如果任务尚未完成，调用线程将阻塞等待直到任务完成。
     * 任务完成后立即返回结果；如果任务失败或已取消，则抛出异常。</p>
     *
     * @return 异步计算的结果
     * @throws ExecutionException    如果任务执行过程中发生异常
     * @throws CancellationException 如果任务已被取消
     * @throws InterruptedException  如果等待过程中线程被中断
     */
    T get() throws ExecutionException, CancellationException, InterruptedException;

    /**
     * 获取异步任务的结果（带超时阻塞等待）。
     *
     * <p>与 {@link #get()} 类似，但最多等待指定的超时时间。
     * 如果超时后任务仍未完成，则抛出超时异常。</p>
     *
     * @param timeoutMillis 超时时间（毫秒）
     * @return 异步计算的结果
     * @throws ExecutionException    如果任务执行过程中发生异常
     * @throws CancellationException 如果任务已被取消
     * @throws TimeoutException      如果等待超时
     * @throws InterruptedException  如果等待过程中线程被中断
     */
    T get(long timeoutMillis) throws ExecutionException, CancellationException, TimeoutException, InterruptedException;

    /**
     * 检查异步任务是否已完成。
     *
     * <p>任务完成包括：成功完成、发生异常、被取消。
     * 此方法不会阻塞，立即返回结果。</p>
     *
     * @return 如果任务已完成则返回 true，否则返回 false
     */
    boolean isDone();

    /**
     * 检查异步任务是否已被取消。
     *
     * @return 如果任务已被取消则返回 true，否则返回 false
     */
    boolean isCancelled();
}
