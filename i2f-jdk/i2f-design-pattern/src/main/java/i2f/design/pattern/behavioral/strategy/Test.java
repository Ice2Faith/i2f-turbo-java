package i2f.design.pattern.behavioral.strategy;

import i2f.design.pattern.behavioral.strategy.order.OrderContext;
import i2f.design.pattern.behavioral.strategy.payment.PaymentStrategy;
import i2f.design.pattern.behavioral.strategy.payment.impl.AliPay;
import i2f.design.pattern.behavioral.strategy.payment.impl.CreditCardPay;
import i2f.design.pattern.behavioral.strategy.payment.impl.WeChatPay;

/**
 * 策略模式 —— 调用演示
 *
 * <p>演示策略模式的核心机制：上下文（{@link OrderContext}）持有策略接口引用，
 * 运行时动态切换具体策略（{@link PaymentStrategy}），实现算法与使用的解耦。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 */
public class Test {
    public static void main(String[] args) {

        // ==================== 1. 策略模式核心演示 ====================
        System.out.println("====== 策略模式（Strategy）演示 ======");
        System.out.println("场景：电商订单（Context）通过支付策略（Strategy）完成结算");
        System.out.println("      不同的支付方式封装为独立策略，运行时可动态切换\n");

        // ==================== 2. 支付宝支付 ====================
        System.out.println("────── 订单 1：使用支付宝支付 ──────");
        OrderContext order1 = new OrderContext("ORDER-20260521-001");
        order1.setPaymentStrategy(new AliPay());
        order1.checkout(1000.00);

        System.out.println();

        // ==================== 3. 微信支付 ====================
        System.out.println("────── 订单 2：使用微信支付 ──────");
        OrderContext order2 = new OrderContext("ORDER-20260521-002");
        order2.setPaymentStrategy(new WeChatPay());
        order2.checkout(500.00);

        System.out.println();

        // ==================== 4. 信用卡支付 ====================
        System.out.println("────── 订单 3：使用信用卡支付 ──────");
        OrderContext order3 = new OrderContext("ORDER-20260521-003");
        order3.setPaymentStrategy(new CreditCardPay());
        order3.checkout(2000.00);

        System.out.println();

        // ==================== 5. 动态切换策略演示 ====================
        System.out.println("====== 动态切换策略 —— 同一订单更换支付方式 ======");
        System.out.println("用户先选择支付宝，后改为微信支付：\n");

        OrderContext order4 = new OrderContext("ORDER-20260521-004");
        
        // 第一次选择支付宝
        System.out.println("【第一次尝试】");
        order4.setPaymentStrategy(new AliPay());
        order4.checkout(800.00);

        System.out.println();

        // 改为微信支付
        System.out.println("【第二次尝试（更换支付方式）】");
        order4.setPaymentStrategy(new WeChatPay());
        order4.checkout(800.00);

        System.out.println();

        // ==================== 6. 面向抽象编程演示 ====================
        System.out.println("====== 面向抽象编程 —— 批量处理不同支付策略的订单 ======");
        System.out.println("通过统一接口调度不同的支付策略：\n");

        PaymentStrategy[] strategies = {
            new AliPay(),
            new WeChatPay(),
            new CreditCardPay()
        };
        
        String[] orderNos = {"ORDER-A-001", "ORDER-A-002", "ORDER-A-003"};
        double[] amounts = {100.00, 200.00, 300.00};

        for (int i = 0; i < strategies.length; i++) {
            System.out.println("批量订单 " + (i + 1) + "：");
            OrderContext order = new OrderContext(orderNos[i]);
            order.setPaymentStrategy(strategies[i]);
            order.checkout(amounts[i]);
            System.out.println();
        }

        // ==================== 7. 策略模式优势总结 ====================
        System.out.println("====== 策略模式优势总结 ======");
        System.out.println("1. 遵循开闭原则：新增支付方式只需新增策略类，无需修改已有代码");
        System.out.println("2. 避免多重条件判断：用策略类替代 if-else/switch 分支");
        System.out.println("3. 算法可独立演化：每种支付策略独立封装，互不影响");
        System.out.println("4. 运行时动态切换：同一上下文可在运行时更换策略");
        System.out.println("5. 符合单一职责：每个策略类只负责一种算法的实现");
    }
}
