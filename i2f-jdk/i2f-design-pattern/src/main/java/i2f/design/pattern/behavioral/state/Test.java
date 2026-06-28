package i2f.design.pattern.behavioral.state;

import i2f.design.pattern.behavioral.state.order.Order;

/**
 * 状态模式 —— 调用演示
 *
 * <p>演示状态模式的核心机制：订单（Context）将操作委托给当前状态对象（State）处理，
 * 状态对象根据当前状态决定操作是否合法，并控制状态转换。
 * 客户端无需编写任何 if/else 判断，状态模式自动处理所有状态流转逻辑。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 11:00
 */
public class Test {
    public static void main(String[] args) {

        // ==================== 1. 状态模式核心演示 ====================
        System.out.println("====== 状态模式（State Pattern）演示 ======");
        System.out.println("场景：电商订单（Context）在不同状态（State）下允许执行的操作不同");
        System.out.println("      状态对象自行决定操作合法性和状态转换规则\n");

        // ==================== 2. 正常流程：待支付 → 已支付 → 配送中 → 已完成 ====================
        System.out.println("────── 场景一：订单正常完成流程 ──────");
        Order order1 = new Order("ORD-20260521-001", "iPhone 16 Pro", 8999.00);
        
        // 尝试在待支付状态下执行非法操作
        order1.ship();
        order1.confirmReceive();
        
        // 支付订单：待支付 → 已支付
        order1.pay();
        
        // 尝试在已支付状态下执行非法操作
        order1.pay();
        order1.confirmReceive();
        
        // 发货：已支付 → 配送中
        order1.ship();
        
        // 尝试在配送中状态下执行非法操作
        order1.pay();
        order1.ship();
        order1.cancel();
        
        // 确认收货：配送中 → 已完成
        order1.confirmReceive();
        
        // 尝试在已完成状态下执行任何操作（均为非法）
        order1.pay();
        order1.ship();
        order1.confirmReceive();
        order1.cancel();

        System.out.println("\n" + order1);

        System.out.println("\n==========================================\n");

        // ==================== 3. 取消流程：待支付 → 已取消 ====================
        System.out.println("────── 场景二：订单取消流程（待支付阶段取消） ──────");
        Order order2 = new Order("ORD-20260521-002", "MacBook Pro 14寸", 14999.00);
        
        // 取消订单：待支付 → 已取消
        order2.cancel();
        
        // 尝试在已取消状态下执行任何操作（均为非法）
        order2.pay();
        order2.ship();
        order2.cancel();

        System.out.println("\n" + order2);

        System.out.println("\n==========================================\n");

        // ==================== 4. 取消流程：已支付 → 已取消 ====================
        System.out.println("────── 场景三：订单取消流程（已支付阶段取消） ──────");
        Order order3 = new Order("ORD-20260521-003", "AirPods Pro 2", 1899.00);
        
        // 支付订单：待支付 → 已支付
        order3.pay();
        
        // 取消订单：已支付 → 已取消（退款）
        order3.cancel();
        
        // 尝试在已取消状态下执行任何操作（均为非法）
        order3.pay();
        order3.ship();

        System.out.println("\n" + order3);

        System.out.println("\n==========================================\n");

        // ==================== 5. 多订单并发演示 ====================
        System.out.println("====== 面向抽象编程 —— 统一管理多个订单 ======");
        System.out.println("通过统一接口调度不同状态的订单：\n");

        Order[] orders = {
            new Order("ORD-20260521-004", "iPad Air", 4799.00),
            new Order("ORD-20260521-005", "Apple Watch", 2999.00),
            new Order("ORD-20260521-006", "HomePod mini", 799.00)
        };

        // 批量处理订单
        System.out.println("\n── 批量操作：所有订单执行支付 ──");
        for (Order order : orders) {
            order.pay();
        }

        System.out.println("\n── 批量操作：所有订单执行发货 ──");
        for (Order order : orders) {
            order.ship();
        }

        System.out.println("\n── 批量操作：所有订单确认收货 ──");
        for (Order order : orders) {
            order.confirmReceive();
        }

        // 打印最终状态
        System.out.println("\n====== 最终订单状态汇总 ======");
        for (Order order : orders) {
            System.out.println(order);
        }

        System.out.println();

        // ==================== 6. 模式优势总结 ====================
        System.out.println("====== 状态模式优势总结 ======");
        System.out.println("1. 遵循开闭原则：新增状态只需新增状态类，无需修改已有代码");
        System.out.println("2. 遵循单一职责：每个状态类只负责定义该状态下的行为规则");
        System.out.println("3. 消除大量 if/else：状态转换逻辑分散到各状态类，代码清晰");
        System.out.println("4. 状态转换显式化：每个状态类明确定义可转换的下一个状态");
        System.out.println("5. 客户端面向抽象编程：Order 无需知道具体状态类型，委托即可");
        System.out.println("6. 易于扩展：新增状态（如'退款中'）不影响现有状态逻辑");
    }
}
