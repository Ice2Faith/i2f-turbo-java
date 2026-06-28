package i2f.design.pattern.concurrency.producerConsumer;

import i2f.design.pattern.concurrency.producerConsumer.buffer.FixedSizeBuffer;
import i2f.design.pattern.concurrency.producerConsumer.consumer.Waiter;
import i2f.design.pattern.concurrency.producerConsumer.product.Dish;
import i2f.design.pattern.concurrency.producerConsumer.producer.Chef;

/**
 * 生产者-消费者模式 —— 调用演示
 *
 * <p>演示生产者-消费者模式的核心机制：通过固定大小的共享缓冲区（出餐台）
 * 解耦生产者（厨师）与消费者（服务员），实现生产与消费的协调。</p>
 *
 * <p>场景：餐厅厨房 —— 厨师制作菜品放入出餐台（缓冲区），服务员从出餐台取走菜品送给顾客。
 * 当出餐台满时厨师需要等待，当出餐台空时服务员需要等待。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 */
public class Test {
    public static void main(String[] args) {

        // ==================== 1. 模式核心演示 ====================
        System.out.println("====== 生产者-消费者模式（Producer-Consumer）演示 ======");
        System.out.println("场景：餐厅厨房 —— 厨师（Producer）制作菜品放入出餐台（Buffer），");
        System.out.println("      服务员（Consumer）从出餐台取走菜品送给顾客");
        System.out.println("      出餐台容量有限，满了厨师要等，空了服务员要等\n");

        // ==================== 2. 创建固定大小的缓冲区（出餐台） ====================
        int bufferSize = 3; // 出餐台最多同时放 3 道菜
        FixedSizeBuffer buffer = new FixedSizeBuffer(bufferSize);
        System.out.println("────── 初始化出餐台（固定容量缓冲区） ──────");
        System.out.println("出餐台容量: " + buffer.capacity() + " 道菜");
        System.out.println();

        // ==================== 3. 创建菜品菜单 ====================
        Dish[] menu = {
                new Dish("宫保鸡丁", "主菜", 3),
                new Dish("麻婆豆腐", "主菜", 2),
                new Dish("清炒时蔬", "配菜", 2),
                new Dish("番茄蛋汤", "汤品", 3),
                new Dish("糖醋里脊", "主菜", 4),
                new Dish("蒜蓉西兰花", "配菜", 2)
        };
        System.out.println("────── 今日菜单 ──────");
        for (int i = 0; i < menu.length; i++) {
            System.out.println("  " + (i + 1) + ". " + menu[i].getDescription());
        }
        System.out.println();

        // ==================== 4. 创建生产者和消费者 ====================
        System.out.println("────── 创建厨师（生产者）和服务员（消费者） ──────");

        // 厨师：负责制作所有菜品
        Chef chef = new Chef("王大厨", buffer, menu);

        // 服务员：需要服务 6 位顾客（对应 6 道菜）
        Waiter waiter = new Waiter("小李", buffer, menu.length);

        System.out.println("厨师: " + chef.getName());
        System.out.println("服务员: " + waiter.getName());
        System.out.println("需要服务的顾客数: " + menu.length);
        System.out.println();

        // ==================== 5. 启动线程执行 ====================
        System.out.println("====== 开始营业 ======\n");

        Thread chefThread = new Thread(chef, "厨师线程");
        Thread waiterThread = new Thread(waiter, "服务员线程");

        // 启动生产者和消费者线程
        chefThread.start();
        waiterThread.start();

        // ==================== 6. 等待线程执行完成 ====================
        try {
            // 等待厨师完成所有菜品制作
            chefThread.join();
            System.out.println("\n[系统] 厨师已完成所有菜品制作");

            // 等待服务员完成所有服务
            waiterThread.join();
            System.out.println("[系统] 服务员已完成所有服务");

        } catch (InterruptedException e) {
            System.err.println("[系统] 主线程被中断: " + e.getMessage());
            Thread.currentThread().interrupt();
        }

        // ==================== 7. 演示多生产者多消费者场景 ====================
        System.out.println("\n====== 进阶演示：多厨师多服务员协同工作 ======");
        System.out.println("场景：2 位厨师 + 2 位服务员，共享同一个出餐台\n");

        // 重新初始化缓冲区
        FixedSizeBuffer largeBuffer = new FixedSizeBuffer(4);
        System.out.println("出餐台容量: " + largeBuffer.capacity() + " 道菜");
        System.out.println();

        // 准备更多菜品
        Dish[] largeMenu = {
                new Dish("红烧肉", "主菜", 4),
                new Dish("水煮鱼", "主菜", 5),
                new Dish("干煸豆角", "配菜", 3),
                new Dish("酸辣土豆丝", "配菜", 2),
                new Dish("紫菜蛋花汤", "汤品", 2),
                new Dish("蛋炒饭", "主食", 3),
                new Dish("扬州炒饭", "主食", 3),
                new Dish("葱油拌面", "主食", 2)
        };

        System.out.println("────── 大型宴会菜单（8 道菜） ──────");
        for (int i = 0; i < largeMenu.length; i++) {
            System.out.println("  " + (i + 1) + ". " + largeMenu[i].getDescription());
        }
        System.out.println();

        // 创建 2 位厨师，各负责 4 道菜
        Chef chef1 = new Chef("张大厨", largeBuffer, new Dish[]{
                largeMenu[0], largeMenu[1], largeMenu[2], largeMenu[3]
        });
        Chef chef2 = new Chef("李大厨", largeBuffer, new Dish[]{
                largeMenu[4], largeMenu[5], largeMenu[6], largeMenu[7]
        });

        // 创建 2 位服务员，各负责 4 位顾客
        Waiter waiter1 = new Waiter("小王", largeBuffer, 4);
        Waiter waiter2 = new Waiter("小赵", largeBuffer, 4);

        System.out.println("────── 启动多线程协同工作 ──────");
        System.out.println("厨师: " + chef1.getName() + "、" + chef2.getName());
        System.out.println("服务员: " + waiter1.getName() + "、" + waiter2.getName());
        System.out.println();

        // 启动所有线程
        Thread chef1Thread = new Thread(chef1, "厨师1线程");
        Thread chef2Thread = new Thread(chef2, "厨师2线程");
        Thread waiter1Thread = new Thread(waiter1, "服务员1线程");
        Thread waiter2Thread = new Thread(waiter2, "服务员2线程");

        chef1Thread.start();
        chef2Thread.start();
        waiter1Thread.start();
        waiter2Thread.start();

        // 等待所有线程完成
        try {
            chef1Thread.join();
            chef2Thread.join();
            waiter1Thread.join();
            waiter2Thread.join();

            System.out.println("\n====== 所有任务完成 ======");

        } catch (InterruptedException e) {
            System.err.println("[系统] 主线程被中断: " + e.getMessage());
            Thread.currentThread().interrupt();
        }

        // ==================== 8. 模式优势总结 ====================
        System.out.println("\n====== 生产者-消费者模式优势总结 ======");
        System.out.println("1. 解耦生产者与消费者：厨师不需要知道服务员是谁，反之亦然");
        System.out.println("2. 缓冲流量峰值：出餐台可以暂存菜品，平衡制作速度与消费速度");
        System.out.println("3. 支持并发执行：多个厨师可以同时制作，多个服务员可以同时送餐");
        System.out.println("4. 阻塞协调机制：缓冲区满时生产者等待，空时消费者等待，避免资源浪费");
        System.out.println("5. 易于扩展：可以增加厨师或服务员的数量，无需修改核心逻辑");
    }
}
