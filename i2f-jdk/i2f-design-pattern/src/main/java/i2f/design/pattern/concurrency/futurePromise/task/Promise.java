package i2f.design.pattern.concurrency.futurePromise.task;

/**
 * Future/Promise 模式 —— Promise（写入端：任务执行者使用）
 *
 * <p><b>角色：</b>异步结果的写入者（Producer Side）</p>
 *
 * <p><b>模式说明：</b>Promise 代表一个"承诺"——任务执行者（如厨房）在完成异步计算后，
 * 通过 Promise 将结果写入。Promise 与 Future 共享同一个结果槽位，
 * 但 Promise 只能写入一次，Future 只能读取。</p>
 *
 * <p><b>命名立意：</b>以"餐厅取餐凭证"为场景——
 * 顾客点单后获得取餐凭据（Future），厨房制作完成后将菜品放入对应订单（Promise.set），
 * 顾客凭凭据取餐（Future.get）。Promise 是厨房端的写入权限，Future 是顾客端的读取权限。</p>
 *
 * <p><b>与 Future 的关系：</b></p>
 * <pre>
 *  Promise（写入端）                     Future（读取端）
 *  ────────────────────────────────      ────────────────────────────────
 *  由任务执行者持有（厨房）               由任务调用者持有（顾客）
 *  负责写入结果（set/cancel）             负责读取结果（get）
 *  只能写入一次                          可多次读取（但结果不变）
 * </pre>
 *
 * @param <T> 异步结果的类型
 * @author Ice2Faith
 * @date 2026/5/21 14:00
 * @see Future
 * @see FutureTask
 */
public interface Promise<T> {

    /**
     * 设置异步任务的成功结果。
     *
     * <p>此方法只能调用一次，多次调用将抛出异常。
     * 设置结果后，所有等待该结果的 Future#get() 将被唤醒并返回该值。</p>
     *
     * @param result 异步计算的结果
     * @throws IllegalStateException 如果结果已被设置或任务已取消
     */
    void set(T result);

    /**
     * 设置异步任务的异常结果。
     *
     * <p>当任务执行过程中发生异常时调用此方法，
     * 所有等待该结果的 Future#get() 将抛出该异常。</p>
     *
     * @param exception 任务执行过程中的异常
     * @throws IllegalStateException 如果结果已被设置或任务已取消
     */
    void setException(Throwable exception);

    /**
     * 取消异步任务。
     *
     * <p>尝试取消任务执行。如果任务已完成或已取消，则取消失败。
     * 取消后，所有等待该结果的 Future#get() 将抛出异常。</p>
     *
     * @return 如果成功取消则返回 true，否则返回 false
     */
    boolean cancel();
}
