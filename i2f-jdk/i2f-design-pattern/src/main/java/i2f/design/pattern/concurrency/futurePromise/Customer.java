package i2f.design.pattern.concurrency.futurePromise;

import i2f.design.pattern.concurrency.futurePromise.exception.CancellationException;
import i2f.design.pattern.concurrency.futurePromise.exception.ExecutionException;
import i2f.design.pattern.concurrency.futurePromise.exception.TimeoutException;
import i2f.design.pattern.concurrency.futurePromise.task.Future;
import i2f.design.pattern.concurrency.futurePromise.task.FutureTask;

/**
 * Future/Promise 模式 —— 顾客（任务调用者：提交订单并等待取餐）
 *
 * <p><b>角色：</b>异步任务的调用者（Consumer / Task Caller）</p>
 *
 * <p><b>模式说明：</b>Customer 代表异步任务的调用者——顾客点单后获得 Future 对象，
 * 可以在未来某个时刻通过 Future 获取结果。
 * 顾客可以选择阻塞等待、超时等待、轮询检查或取消订单。</p>
 *
 * <p><b>命名立意：</b>以"餐厅顾客"为场景——
 * 顾客点单后获得取餐凭据（Future），可以坐在座位上等待（阻塞 get），
 * 也可以时不时查看是否做好（isDone 轮询），或者等不及了取消订单（cancel）。</p>
 *
 * <p><b>典型使用方式：</b></p>
 * <pre>
 *  1. 阻塞等待：food = future.get()              // 一直等到做好
 *  2. 超时等待：food = future.get(5000)          // 最多等 5 秒
 *  3. 轮询检查：while (!future.isDone()) { ... } //  periodically 检查
 *  4. 取消订单：future.cancel()                  // 不做了
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 14:00
 * @see Future
 * @see Kitchen
 */
public class Customer {

    /**
     * 顾客姓名。
     */
    private final String customerName;

    public Customer(String customerName) {
        this.customerName = customerName;
    }

    /**
     * 点单并提交异步任务。
     *
     * <p>顾客点单后，创建 FutureTask（取餐凭据），
     * 将 Promise 端交给厨房制作，自己保留 Future 端等待取餐。</p>
     *
     * @param food 要点的菜品
     * @param kitchen 负责制作的厨房
     * @return Future 对象（取餐凭据）
     */
    public Future<Food> order(Food food, Kitchen kitchen) {
        System.out.println("  [顾客] " + customerName + " 点单：" + food.getDescription());

        // 创建 FutureTask（同时具备 Promise 和 Future 能力）
        FutureTask<Food> futureTask = new FutureTask<>("ORD-" + System.currentTimeMillis());

        // 将 Promise 端交给厨房（厨房完成后更新结果）
        kitchen.cookAsync(food, futureTask);

        // 顾客保留 Future 端（用于未来取餐）
        return futureTask;
    }

    /**
     * 阻塞等待取餐。
     *
     * <p>顾客坐在座位上等待，直到厨房制作完成。
     * 这是最简单的方式，但会阻塞当前线程。</p>
     *
     * @param future Future 对象（取餐凭据）
     * @return 制作完成的菜品
     */
    public Food waitForFood(Future<Food> future) {
        try {
            System.out.println("  [顾客] " + customerName + " 开始等待取餐...");
            return future.get();
        } catch (ExecutionException e) {
            System.out.println("  [顾客] " + customerName + " 取餐失败：" + e.getMessage());
            return null;
        } catch (CancellationException e) {
            System.out.println("  [顾客] " + customerName + " 订单已取消");
            return null;
        } catch (InterruptedException e) {
            System.out.println("  [顾客] " + customerName + " 等待被中断");
            Thread.currentThread().interrupt();
            return null;
        }
    }

    /**
     * 超时等待取餐。
     *
     * <p>顾客最多等待指定时间，超时后不再等待。
     * 适用于顾客有其他事情要做，不能无限等待的场景。</p>
     *
     * @param future Future 对象（取餐凭据）
     * @param timeoutMillis 超时时间（毫秒）
     * @return 制作完成的菜品，如果超时则返回 null
     */
    public Food waitForFoodWithTimeout(Future<Food> future, long timeoutMillis) {
        try {
            System.out.println("  [顾客] " + customerName + " 开始等待取餐（最多等待 " + timeoutMillis + "ms）...");
            return future.get(timeoutMillis);
        } catch (ExecutionException e) {
            System.out.println("  [顾客] " + customerName + " 取餐失败：" + e.getMessage());
            return null;
        } catch (CancellationException e) {
            System.out.println("  [顾客] " + customerName + " 订单已取消");
            return null;
        } catch (TimeoutException e) {
            System.out.println("  [顾客] " + customerName + " 等待超时：" + e.getMessage());
            return null;
        } catch (InterruptedException e) {
            System.out.println("  [顾客] " + customerName + " 等待被中断");
            Thread.currentThread().interrupt();
            return null;
        }
    }

    /**
     * 轮询检查订单状态。
     *
     * <p>顾客不阻塞等待，而是定期查看订单是否完成。
     * 适用于顾客想做其他事情（如刷手机）的场景。</p>
     *
     * @param future Future 对象（取餐凭据）
     * @param pollIntervalMillis 轮询间隔（毫秒）
     */
    public void pollOrderStatus(Future<Food> future, long pollIntervalMillis) {
        System.out.println("  [顾客] " + customerName + " 开始轮询订单状态...");

        while (!future.isDone()) {
            System.out.println("  [顾客] " + customerName + " 查看订单：还没做好，继续等待...");
            try {
                Thread.sleep(pollIntervalMillis);
            } catch (InterruptedException e) {
                System.out.println("  [顾客] " + customerName + " 轮询被中断");
                Thread.currentThread().interrupt();
                return;
            }
        }

        // 订单已完成，取餐
        waitForFood(future);
    }

    /**
     * 取消订单。
     *
     * <p>顾客等不及了，取消订单。
     * 如果厨房还没开始制作或制作中，可以尝试取消；
     * 如果已经制作完成，则取消失败。</p>
     *
     * @param future Future 对象（取餐凭据）
     * @return 是否取消成功
     */
    public boolean cancelOrder(Future<Food> future) {
        if (future instanceof FutureTask) {
            System.out.println("  [顾客] " + customerName + " 取消订单...");
            return ((FutureTask<Food>) future).cancel();
        }
        return false;
    }

    /**
     * 获取顾客姓名。
     *
     * @return 顾客姓名
     */
    public String getCustomerName() {
        return customerName;
    }
}
