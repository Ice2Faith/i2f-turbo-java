package i2f.design.pattern.concurrency.futurePromise.task;

import i2f.design.pattern.concurrency.futurePromise.exception.CancellationException;
import i2f.design.pattern.concurrency.futurePromise.exception.ExecutionException;
import i2f.design.pattern.concurrency.futurePromise.exception.TimeoutException;

/**
 * Future/Promise 模式 —— FutureTask（Promise 与 Future 的统一实现）
 *
 * <p><b>角色：</b>核心实现类（同时实现 Promise 和 Future 接口）</p>
 *
 * <p><b>模式说明：</b>FutureTask 是 Promise 和 Future 的具体实现，
 * 内部通过 synchronized + wait/notify 实现线程同步。
 * 任务执行者通过 Promise 端写入结果，任务调用者通过 Future 端读取结果，
 * 两者共享同一个状态机。</p>
 *
 * <p><b>命名立意：</b>以"餐厅订单系统"为场景——
 * 订单（FutureTask）既是厨房的制作指令（Promise），也是顾客的取餐凭据（Future）。
 * 厨房完成后更新订单状态（set），顾客凭订单号查询状态（get）。</p>
 *
 * <p><b>状态机：</b></p>
 * <pre>
 *  PENDING（等待中）
 *      ↓
 *  ├─→ DONE（已完成）     → result 有值
 *  ├─→ FAILED（已失败）   → exception 有值
 *  └─→ CANCELLED（已取消）→ 无结果
 * </pre>
 *
 * <p><b>线程安全保证：</b></p>
 * <ul>
 *   <li>所有状态转换通过 synchronized 块保护</li>
 *   <li>Pending 状态下的 get() 调用会阻塞等待（wait）</li>
 *   <li>set/cancel 完成后唤醒所有等待线程（notifyAll）</li>
 *   <li>结果只能设置一次，重复设置抛出异常</li>
 * </ul>
 *
 * @param <T> 异步结果的类型
 * @author Ice2Faith
 * @date 2026/5/21 14:00
 * @see Promise
 * @see Future
 */
public class FutureTask<T> implements Promise<T>, Future<T> {

    /**
     * 任务状态枚举。
     */
    private enum State {
        /**
         * 等待中
         */
        PENDING,
        /**
         * 已完成
         */
        DONE,
        /**
         * 已失败
         */
        FAILED,
        /**
         * 已取消
         */
        CANCELLED
    }

    /**
     * 当前任务状态。
     */
    private volatile State state = State.PENDING;

    /**
     * 异步任务的结果（成功时设置）。
     */
    private T result;

    /**
     * 异步任务的异常（失败时设置）。
     */
    private Throwable exception;

    /**
     * 订单编号（用于日志输出）。
     */
    private final String orderId;

    public FutureTask(String orderId) {
        this.orderId = orderId;
    }

    // ==================== Promise 接口实现（写入端） ====================

    @Override
    public synchronized void set(T result) {
        if (state != State.PENDING) {
            throw new IllegalStateException("订单 " + orderId + " 已完成，无法重复设置结果（当前状态：" + state + "）");
        }
        this.result = result;
        this.state = State.DONE;
        System.out.println("  [厨房] 订单 " + orderId + " 制作完成 ✓");
        notifyAll(); // 唤醒所有等待该结果的线程
    }

    @Override
    public synchronized void setException(Throwable exception) {
        if (state != State.PENDING) {
            throw new IllegalStateException("订单 " + orderId + " 已完成，无法设置异常（当前状态：" + state + "）");
        }
        this.exception = exception;
        this.state = State.FAILED;
        System.out.println("  [厨房] 订单 " + orderId + " 制作失败 ✗: " + exception.getMessage());
        notifyAll(); // 唤醒所有等待该结果的线程
    }

    @Override
    public synchronized boolean cancel() {
        if (state != State.PENDING) {
            System.out.println("  [厨房] 订单 " + orderId + " 无法取消（当前状态：" + state + "）");
            return false;
        }
        this.state = State.CANCELLED;
        System.out.println("  [厨房] 订单 " + orderId + " 已取消 ⚠");
        notifyAll(); // 唤醒所有等待该结果的线程
        return true;
    }

    // ==================== Future 接口实现（读取端） ====================

    @Override
    public synchronized T get() throws ExecutionException, CancellationException, InterruptedException {
        // 等待任务完成
        while (state == State.PENDING) {
            System.out.println("  [顾客] 订单 " + orderId + " 正在制作中，等待...");
            wait();
        }

        // 根据状态返回结果或抛出异常
        return handleCompletion();
    }

    @Override
    public synchronized T get(long timeoutMillis) throws ExecutionException, CancellationException, TimeoutException, InterruptedException {
        long startTime = System.currentTimeMillis();
        long remainingTime = timeoutMillis;

        // 等待任务完成（带超时）
        while (state == State.PENDING && remainingTime > 0) {
            System.out.println("  [顾客] 订单 " + orderId + " 正在制作中，等待（剩余 " + remainingTime + "ms）...");
            wait(remainingTime);
            remainingTime = timeoutMillis - (System.currentTimeMillis() - startTime);
        }

        // 超时检查
        if (state == State.PENDING) {
            throw new TimeoutException("订单 " + orderId + " 等待超时（" + timeoutMillis + "ms）");
        }

        // 根据状态返回结果或抛出异常
        return handleCompletion();
    }

    @Override
    public boolean isDone() {
        return state != State.PENDING;
    }

    @Override
    public boolean isCancelled() {
        return state == State.CANCELLED;
    }

    // ==================== 内部辅助方法 ====================

    /**
     * 处理任务完成后的状态。
     *
     * @return 任务结果
     * @throws ExecutionException    如果任务执行失败
     * @throws CancellationException 如果任务已取消
     */
    private T handleCompletion() throws ExecutionException, CancellationException {
        switch (state) {
            case DONE:
                System.out.println("  [顾客] 订单 " + orderId + " 取餐成功: " + result);
                return result;
            case FAILED:
                System.out.println("  [顾客] 订单 " + orderId + " 取餐失败: " + exception.getMessage());
                throw new ExecutionException("订单 " + orderId + " 执行失败", exception);
            case CANCELLED:
                System.out.println("  [顾客] 订单 " + orderId + " 已被取消");
                throw new CancellationException("订单 " + orderId + " 已被取消");
            default:
                throw new IllegalStateException("订单 " + orderId + " 状态异常: " + state);
        }
    }

    /**
     * 获取订单编号。
     *
     * @return 订单编号
     */
    public String getOrderId() {
        return orderId;
    }

    @Override
    public String toString() {
        return "FutureTask{orderId='" + orderId + "', state=" + state + "}";
    }
}
