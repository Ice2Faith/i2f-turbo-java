package i2f.design.pattern.behavioral.strategy.order;

import i2f.design.pattern.behavioral.strategy.payment.PaymentStrategy;

/**
 * 策略模式 —— 订单上下文（Context：OrderContext）
 *
 * <p><b>角色：</b>上下文（Context）</p>
 *
 * <p><b>模式说明：</b>持有策略接口的引用，通过该引用调用具体的策略算法。
 * 上下文不关心具体使用哪种策略，只需知道策略接口定义了 {@link PaymentStrategy#pay(double)} 方法即可。
 * 运行时可以动态切换策略（{@link #setPaymentStrategy(PaymentStrategy)}），
 * 这是策略模式的核心优势之一。</p>
 *
 * <p><b>命名立意：</b>以"电商订单"为场景——订单在结算时可以选择不同的支付方式，
 * 订单对象（Context）持有支付策略（Strategy）引用，
 * 用户选择哪种支付方式，订单就使用哪种策略完成支付。</p>
 *
 * <p><b>与 if-else 的对比：</b></p>
 * <pre>
 *  传统 if-else 写法                          策略模式写法
 *  ─────────────────────────────────────     ─────────────────────────────────────
 *  if (type == "alipay") {                    context.setPaymentStrategy(new AliPay());
 *      // 支付宝支付逻辑                       context.checkout(100.0);
 *  } else if (type == "wechat") {             // 新增支付方式无需修改订单代码
 *      // 微信支付逻辑
 *  } else if (type == "credit") {
 *      // 信用卡支付逻辑
 *  }                                          符合开闭原则 vs 违反开闭原则
 *  </pre>
 *
 * <p><b>模式结构：</b></p>
 * <pre>
 *  OrderContext（上下文）
 *    ├─ paymentStrategy: PaymentStrategy     ← 策略引用（可动态切换）
 *    ├─ setPaymentStrategy(strategy)         ← 设置策略
 *    └─ checkout(amount)                     ← 使用策略执行业务
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see PaymentStrategy
 */
public class OrderContext {

    /**
     * 当前使用的支付策略。
     *
     * <p>通过组合而非继承的方式引入策略，
     * 使订单与支付方式解耦，符合依赖倒置原则。</p>
     */
    private PaymentStrategy paymentStrategy;

    /**
     * 订单编号。
     */
    private String orderNo;

    public OrderContext(String orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 设置支付策略。
     *
     * <p>这是策略模式的关键方法——运行时动态切换算法。
     * 用户可以在支付前随时更换支付方式，订单逻辑无需修改。</p>
     *
     * @param paymentStrategy 支付策略实例
     */
    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    /**
     * 执行订单结算。
     *
     * <p>该方法演示了策略模式的使用方式：
     * 上下文调用策略接口的方法，具体算法由注入的策略实例决定。
     * 订单类<b>不关心</b>具体是哪种支付方式——多态的力量。</p>
     *
     * @param amount 支付金额（元）
     * @return 支付结果描述
     */
    public String checkout(double amount) {
        if (paymentStrategy == null) {
            throw new IllegalStateException("未设置支付策略，请先调用 setPaymentStrategy()");
        }

        System.out.println("  订单编号：" + orderNo);
        System.out.println("  支付方式：" + paymentStrategy.getPaymentName());
        System.out.println("  支付结果：" + paymentStrategy.pay(amount));
        return paymentStrategy.pay(amount);
    }

    /**
     * 获取订单编号。
     *
     * @return 订单编号
     */
    public String getOrderNo() {
        return orderNo;
    }
}
