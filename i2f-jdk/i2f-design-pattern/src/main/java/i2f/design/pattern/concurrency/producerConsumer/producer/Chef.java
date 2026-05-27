package i2f.design.pattern.concurrency.producerConsumer.producer;

import i2f.design.pattern.concurrency.producerConsumer.buffer.FixedSizeBuffer;
import i2f.design.pattern.concurrency.producerConsumer.product.Dish;

/**
 * 生产者-消费者模式 —— 厨师（Producer：Chef）
 *
 * <p><b>角色：</b>生产者（Producer）</p>
 *
 * <p><b>模式说明：</b>负责生产产品（菜品）并放入共享缓冲区。
 * 当缓冲区满时，生产者会阻塞等待，直到消费者取出产品后才有空间继续生产。</p>
 *
 * <p><b>命名立意：</b>"厨师"天然地充当生产者角色——
 * 厨师按照菜单制作菜品，制作完成后将菜品放到出餐台（缓冲区）上，
 * 然后继续制作下一道菜。如果出餐台满了，厨师就需要等待。</p>
 *
 * <p><b>生产流程：</b></p>
 * <pre>
 *  1. 准备食材（模拟制作时间）
 *  2. 制作菜品（Thread.sleep 模拟耗时）
 *  3. 将菜品放入缓冲区（buffer.put）
 *  4. 重复以上步骤
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see FixedSizeBuffer
 * @see Dish
 */
public class Chef implements Runnable {

    /**
     * 共享缓冲区（出餐台）。
     */
    private final FixedSizeBuffer buffer;

    /**
     * 厨师名称。
     */
    private final String name;

    /**
     * 要生产的菜品列表。
     */
    private final Dish[] dishesToCook;

    /**
     * 构造厨师。
     *
     * @param name           厨师名称
     * @param buffer         共享缓冲区
     * @param dishesToCook   要生产的菜品列表
     */
    public Chef(String name, FixedSizeBuffer buffer, Dish[] dishesToCook) {
        this.name = name;
        this.buffer = buffer;
        this.dishesToCook = dishesToCook;
    }

    /**
     * 执行生产任务。
     *
     * <p>依次制作菜品列表中的每道菜，制作完成后放入缓冲区。</p>
     */
    @Override
    public void run() {
        try {
            System.out.println("====== " + name + " 开始工作 ======");

            for (Dish dish : dishesToCook) {
                // 1. 模拟制作菜品的时间
                System.out.println("  [" + name + "] 开始制作: " + dish.getDescription());
                Thread.sleep(dish.getCookTime() * 1000); // 模拟制作时间

                // 2. 将制作好的菜品放入缓冲区（如果缓冲区满则等待）
                buffer.put(dish);
                System.out.println("  [" + name + "] 制作完成: " + dish.getName());
                System.out.println();

                // 短暂休息，模拟真实场景
                Thread.sleep(500);
            }

            System.out.println("====== " + name + " 完成所有菜品制作 ======");

        } catch (InterruptedException e) {
            System.err.println("[" + name + "] 被中断: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 获取厨师名称。
     *
     * @return 厨师名称
     */
    public String getName() {
        return name;
    }
}
