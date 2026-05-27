package i2f.design.pattern.concurrency.producerConsumer.consumer;

import i2f.design.pattern.concurrency.producerConsumer.buffer.FixedSizeBuffer;
import i2f.design.pattern.concurrency.producerConsumer.product.Dish;

/**
 * 生产者-消费者模式 —— 服务员（Consumer：Waiter）
 *
 * <p><b>角色：</b>消费者（Consumer）</p>
 *
 * <p><b>模式说明：</b>负责从共享缓冲区中取出产品（菜品）并进行消费（送给顾客）。
 * 当缓冲区为空时，消费者会阻塞等待，直到生产者放入新产品后才有数据可以消费。</p>
 *
 * <p><b>命名立意：</b>"服务员"天然地充当消费者角色——
 * 服务员从出餐台（缓冲区）取走厨师制作好的菜品，然后送给顾客。
 * 如果出餐台上没有菜品，服务员就需要等待厨师制作。</p>
 *
 * <p><b>消费流程：</b></p>
 * <pre>
 *  1. 从缓冲区取出菜品（buffer.take，如果缓冲区空则等待）
 *  2. 将菜品送给顾客（模拟送餐时间）
 *  3. 重复以上步骤
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see FixedSizeBuffer
 * @see Dish
 */
public class Waiter implements Runnable {

    /**
     * 共享缓冲区（出餐台）。
     */
    private final FixedSizeBuffer buffer;

    /**
     * 服务员名称。
     */
    private final String name;

    /**
     * 需要服务的顾客数量（即需要消费的菜品数量）。
     */
    private final int customerCount;

    /**
     * 构造服务员。
     *
     * @param name          服务员名称
     * @param buffer        共享缓冲区
     * @param customerCount 需要服务的顾客数量
     */
    public Waiter(String name, FixedSizeBuffer buffer, int customerCount) {
        this.name = name;
        this.buffer = buffer;
        this.customerCount = customerCount;
    }

    /**
     * 执行消费任务。
     *
     * <p>依次从缓冲区取出菜品并送给顾客，直到服务完所有顾客。</p>
     */
    @Override
    public void run() {
        try {
            System.out.println("====== " + name + " 开始服务 ======");

            for (int i = 1; i <= customerCount; i++) {
                // 1. 从缓冲区取出菜品（如果缓冲区空则等待）
                Dish dish = buffer.take();

                // 2. 模拟送餐给顾客的时间
                System.out.println("  [" + name + "] 正在送菜给顾客 " + i + ": " + dish.getDescription());
                Thread.sleep(2000); // 模拟送餐时间
                System.out.println("  [" + name + "] 顾客 " + i + " 已收到: " + dish.getName());
                System.out.println();

                // 短暂休息，模拟真实场景
                Thread.sleep(500);
            }

            System.out.println("====== " + name + " 完成所有服务 ======");

        } catch (InterruptedException e) {
            System.err.println("[" + name + "] 被中断: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 获取服务员名称。
     *
     * @return 服务员名称
     */
    public String getName() {
        return name;
    }
}
