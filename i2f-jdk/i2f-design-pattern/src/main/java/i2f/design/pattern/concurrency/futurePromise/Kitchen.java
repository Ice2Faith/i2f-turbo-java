package i2f.design.pattern.concurrency.futurePromise;

import i2f.design.pattern.concurrency.futurePromise.task.FutureTask;
import i2f.design.pattern.concurrency.futurePromise.task.Promise;

/**
 * Future/Promise 模式 —— 厨房（任务执行者：异步制作食物）
 *
 * <p><b>角色：</b>异步任务的执行者（Producer / Task Executor）</p>
 *
 * <p><b>模式说明：</b>Kitchen 代表异步任务的执行者——厨房接收订单后异步制作菜品，
 * 制作完成后通过 Promise.set(food) 将结果写入 FutureTask，
 * 通知等待的顾客（Future.get() 被唤醒）。</p>
 *
 * <p><b>命名立意：</b>以"餐厅厨房"为场景——
 * 厨房接收订单（FutureTask）后，在后台线程中异步制作菜品，
 * 制作完成后更新订单状态（Promise 端），顾客即可取餐（Future 端）。</p>
 *
 * <p><b>与 Future/Promise 的关系：</b></p>
 * <pre>
 *  顾客点单 ──→ 创建 FutureTask ──→ 交给厨房
 *                                    ↓
 *                          厨房异步制作（新线程）
 *                                    ↓
 *                          制作完成 ──→ Promise.set(food)
 *                                    ↓
 *                          Future.get() 被唤醒，顾客取餐
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 14:00
 * @see FutureTask
 * @see Food
 */
public class Kitchen {

    /**
     * 厨房名称。
     */
    private final String kitchenName;

    public Kitchen(String kitchenName) {
        this.kitchenName = kitchenName;
    }

    /**
     * 异步制作菜品。
     *
     * <p>此方法立即返回，不阻塞调用线程。
     * 厨房会在后台线程中制作菜品，制作完成后通过 Promise 端更新结果。</p>
     *
     * @param food 要制作的菜品
     * @param promise Promise 端（用于写入结果）
     */
    public void cookAsync(Food food, Promise<Food> promise) {
        // 在新线程中异步执行制作任务
        new Thread(() -> {
            try {
                System.out.println("  [厨房] " + kitchenName + " 开始制作：" + food.getDescription());

                // 模拟制作耗时（转换为毫秒）
                Thread.sleep(food.getCookTimeSeconds() * 1000L);

                // 制作完成，通过 Promise 写入结果
                promise.set(food);

            } catch (InterruptedException e) {
                // 线程被中断，设置异常结果
                System.out.println("  [厨房] " + kitchenName + " 制作被中断");
                promise.setException(new RuntimeException("厨房制作被中断"));
                Thread.currentThread().interrupt();

            } catch (Exception e) {
                // 其他异常，设置异常结果
                System.out.println("  [厨房] " + kitchenName + " 制作失败：" + e.getMessage());
                promise.setException(e);
            }
        }, "Kitchen-Thread-" + food.getName()).start();
    }

    /**
     * 获取厨房名称。
     *
     * @return 厨房名称
     */
    public String getKitchenName() {
        return kitchenName;
    }
}
