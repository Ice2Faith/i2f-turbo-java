package i2f.design.pattern.concurrency.futurePromise;

import i2f.design.pattern.concurrency.futurePromise.task.Future;

/**
 * Future/Promise 模式 —— 调用演示
 *
 * <p>演示 Future/Promise 模式的核心机制：任务调用者（顾客）提交异步任务后立即获得 Future 对象，
 * 任务执行者（厨房）在后台异步执行任务，完成后通过 Promise 写入结果，
 * 调用者在未来某个时刻通过 Future 获取结果。</p>
 *
 * <p><b>核心思想：</b>将一个"占位对象"代表异步计算的结果，
 * 调用方无需阻塞等待任务完成，可以在未来的某个时间点获取结果。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 14:00
 */
public class Test {
    public static void main(String[] args) {

        // ==================== 1. 基础演示：阻塞等待取餐 ====================
        System.out.println("====== Future/Promise 模式（阻塞等待）演示 ======");
        System.out.println("场景：顾客点单后获得取餐凭据（Future），厨房异步制作（Promise），完成后顾客取餐\n");

        Kitchen kitchen = new Kitchen("主厨房");
        Customer customer1 = new Customer("张三");

        // 顾客点单，立即获得 Future（取餐凭据）
        Food order1Food = new Food("宫保鸡丁", 38.0, 2);
        Future<Food> order1Future = customer1.order(order1Food, kitchen);

        // 顾客阻塞等待取餐（会等待 2 秒）
        Food result1 = customer1.waitForFood(order1Future);
        if (result1 != null) {
            System.out.println("  ✓ " + customer1.getCustomerName() + " 成功取餐：" + result1.getDescription());
        }

        System.out.println();

        // ==================== 2. 超时等待演示 ====================
        System.out.println("====== 超时等待演示 ======");
        System.out.println("场景：顾客最多等待 1.5 秒，但菜品需要 3 秒制作，将触发超时\n");

        Customer customer2 = new Customer("李四");
        Food order2Food = new Food("佛跳墙", 188.0, 3);
        Future<Food> order2Future = customer2.order(order2Food, kitchen);

        // 超时等待（1.5 秒 < 3 秒制作时间，将超时）
        Food result2 = customer2.waitForFoodWithTimeout(order2Future, 1500);
        if (result2 == null) {
            System.out.println("  ⚠ " + customer2.getCustomerName() + " 等不及了，离开餐厅");
        }

        System.out.println();

        // ==================== 3. 轮询检查演示 ====================
        System.out.println("====== 轮询检查演示 ======");
        System.out.println("场景：顾客不阻塞等待，而是每 0.5 秒查看一次订单状态\n");

        Customer customer3 = new Customer("王五");
        Food order3Food = new Food("红烧肉", 45.0, 2);
        Future<Food> order3Future = customer3.order(order3Food, kitchen);

        // 轮询检查（每 500ms 检查一次）
        customer3.pollOrderStatus(order3Future, 500);

        System.out.println();

        // ==================== 4. 取消订单演示 ====================
        System.out.println("====== 取消订单演示 ======");
        System.out.println("场景：顾客点单后等不及，尝试取消订单\n");

        Customer customer4 = new Customer("赵六");
        Food order4Food = new Food("北京烤鸭", 128.0, 5);
        Future<Food> order4Future = customer4.order(order4Food, kitchen);

        // 等待 1 秒后取消
        try {
            System.out.println("  [顾客] " + customer4.getCustomerName() + " 等待 1 秒后决定取消...");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 尝试取消订单
        boolean cancelled = customer4.cancelOrder(order4Future);
        if (cancelled) {
            System.out.println("  ✓ 订单取消成功");
        } else {
            System.out.println("  ✗ 订单已制作完成，无法取消");
        }

        // 尝试取餐（将抛出 CancellationException）
        try {
            customer4.waitForFood(order4Future);
        } catch (Exception e) {
            System.out.println("  ⚠ 取餐异常：" + e.getClass().getSimpleName());
        }

        System.out.println();

        // ==================== 5. 多订单并发演示 ====================
        System.out.println("====== 多订单并发演示 ======");
        System.out.println("场景：多个顾客同时点单，厨房并发制作，各自独立取餐\n");

        Kitchen kitchen2 = new Kitchen("分厨房");
        Customer[] customers = {
                new Customer("顾客A"),
                new Customer("顾客B"),
                new Customer("顾客C")
        };
        Food[] foods = {
                new Food("麻婆豆腐", 25.0, 1),
                new Food("鱼香肉丝", 35.0, 2),
                new Food("水煮鱼", 68.0, 3)
        };

        // 同时提交多个订单
        Future<Food>[] futures = new Future[customers.length];
        for (int i = 0; i < customers.length; i++) {
            futures[i] = customers[i].order(foods[i], kitchen2);
        }

        System.out.println("  [系统] 所有订单已提交，厨房开始并发制作...\n");

        // 各自等待取餐
        for (int i = 0; i < customers.length; i++) {
            Food result = customers[i].waitForFood(futures[i]);
            if (result != null) {
                System.out.println("  ✓ " + customers[i].getCustomerName() + " 取餐成功：" + result.getName() + "\n");
            }
        }

        // ==================== 6. 重复获取结果演示 ====================
        System.out.println("====== 重复获取结果演示 ======");
        System.out.println("场景：同一个 Future 可以多次调用 get()，结果保持不变\n");

        Customer customer5 = new Customer("孙七");
        Food order5Food = new Food("蛋炒饭", 15.0, 1);
        Future<Food> order5Future = customer5.order(order5Food, kitchen);

        // 第一次取餐
        Food result5a = customer5.waitForFood(order5Future);
        System.out.println("  第一次取餐：" + (result5a != null ? result5a.getName() : "失败"));

        // 第二次取餐（结果应该相同，且立即返回，无需等待）
        System.out.println("  [顾客] 再次取餐（应立即返回，无需等待）...");
        Food result5b = customer5.waitForFood(order5Future);
        System.out.println("  第二次取餐：" + (result5b != null ? result5b.getName() : "失败"));

        System.out.println("  result5a == result5b ? " + (result5a == result5b));

        System.out.println();

        // ==================== 7. 状态检查演示 ====================
        System.out.println("====== 状态检查演示 ======");
        System.out.println("场景：通过 isDone() 和 isCancelled() 检查订单状态\n");

        Customer customer6 = new Customer("周八");
        Food order6Food = new Food("清蒸鲈鱼", 58.0, 2);
        Future<Food> order6Future = customer6.order(order6Food, kitchen);

        // 立即检查状态
        System.out.println("  订单刚提交：isDone = " + order6Future.isDone() + ", isCancelled = " + order6Future.isCancelled());

        // 等待 1 秒后检查
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("  等待 1 秒后：isDone = " + order6Future.isDone() + ", isCancelled = " + order6Future.isCancelled());

        // 取餐后检查
        customer6.waitForFood(order6Future);
        System.out.println("  取餐后：isDone = " + order6Future.isDone() + ", isCancelled = " + order6Future.isCancelled());

        System.out.println();

        // ==================== 8. 模式优势总结 ====================
        System.out.println("====== Future/Promise 模式优势总结 ======");
        System.out.println("1. 解耦任务提交与结果获取：调用者无需等待任务完成即可继续其他操作");
        System.out.println("2. 提升系统吞吐量：主线程无需阻塞，可处理更多请求");
        System.out.println("3. 灵活的结果获取方式：支持阻塞等待、超时等待、轮询检查、取消任务");
        System.out.println("4. 适用于异步编程、并发处理、非阻塞 IO 等场景");
        System.out.println("5. Promise/Future 分离：写入端（Promise）与读取端（Future）职责清晰");
        System.out.println("\n典型应用：");
        System.out.println("  - 异步任务执行（如 @Async）");
        System.out.println("  - 非阻塞网络请求（如 HTTP Client）");
        System.out.println("  - 并发数据聚合（多个独立任务并行执行后汇总）");
        System.out.println("  - 响应式编程（如 WebFlux 的 Mono/Flux）");
    }
}
