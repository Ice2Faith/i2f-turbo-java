package i2f.design.pattern.concurrency.futurePromise;

import i2f.design.pattern.concurrency.futurePromise.task.FutureTask;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Future/Promise 模式 —— 菜品（异步任务的结果对象）
 *
 * <p><b>角色：</b>异步计算的结果（Product）</p>
 *
 * <p><b>模式说明：</b>Food 代表厨房异步制作的结果——菜品对象。
 * 当厨房完成制作后，通过 Promise.set(food) 将菜品交付给顾客。</p>
 *
 * <p><b>命名立意：</b>以"餐厅点餐"为场景——
 * 顾客点单后，厨房异步制作菜品（Food），制作完成后将菜品放入订单。
 * 菜品包含名称、价格、制作时间等属性，是异步任务的实际产出。</p>
 *
 * <p><b>与 Future/Promise 的关系：</b></p>
 * <pre>
 *  厨房（任务执行者）──制作──→ Food（结果对象）──Promise.set()──→ FutureTask
 *                                                                  ↓
 *  顾客（任务调用者）──Future.get()──→ 获取 Food 对象
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 14:00
 * @see FutureTask
 * @see Kitchen
 */
@Data
@NoArgsConstructor
public class Food {

    /**
     * 菜品名称。
     */
    private String name;

    /**
     * 菜品价格（元）。
     */
    private double price;

    /**
     * 制作耗时（秒）。
     */
    private int cookTimeSeconds;

    public Food(String name, double price, int cookTimeSeconds) {
        this.name = name;
        this.price = price;
        this.cookTimeSeconds = cookTimeSeconds;
    }

    /**
     * 获取菜品描述信息。
     *
     * @return 菜品描述
     */
    public String getDescription() {
        return String.format("%s（￥%.2f，制作耗时 %d 秒）", name, price, cookTimeSeconds);
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
