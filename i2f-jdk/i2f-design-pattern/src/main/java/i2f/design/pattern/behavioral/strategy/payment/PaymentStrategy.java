package i2f.design.pattern.behavioral.strategy.payment;

import i2f.design.pattern.behavioral.strategy.payment.impl.AliPay;
import i2f.design.pattern.behavioral.strategy.payment.impl.CreditCardPay;
import i2f.design.pattern.behavioral.strategy.payment.impl.WeChatPay;

/**
 * 策略模式 —— 支付策略（Strategy：PaymentStrategy）
 *
 * <p><b>角色：</b>抽象策略（Abstract Strategy）</p>
 *
 * <p><b>模式说明：</b>定义所有支付方式的公共接口，每种具体的支付方式（算法）都实现此接口。
 * 策略模式的核心在于：<b>将算法封装在独立的类中，使它们可以相互替换</b>。</p>
 *
 * <p><b>命名立意：</b>以"电商支付"为场景——不同的支付方式（支付宝、微信、信用卡）
 * 都是独立的策略实现，订单上下文（{@link i2f.design.pattern.behavioral.strategy.order.OrderContext}）
 * 可以在运行时动态切换支付策略，而无需修改订单处理逻辑。</p>
 *
 * <p><b>与工厂方法模式的区别：</b></p>
 * <pre>
 *  工厂方法模式（Factory Method）              策略模式（Strategy）
 *  ─────────────────────────────────────       ─────────────────────────────────────
 *  关注对象的创建过程                            关注算法/行为的封装与替换
 *  由子类决定创建哪种产品                        由上下文在运行时选择使用哪种策略
 *  解决"如何创建对象"的问题                      解决"如何选择算法"的问题
 * </pre>
 *
 * <p><b>模式结构：</b></p>
 * <pre>
 *  Strategy（抽象策略）
 *    └─ PaymentStrategy
 *         └─ pay(amount: double): String       ← 支付方法（算法接口）
 *
 *  ConcreteStrategy（具体策略）
 *    ├─ AliPay      → 支付宝支付
 *    ├─ WeChatPay   → 微信支付
 *    └─ CreditCardPay → 信用卡支付
 *
 *  Context（上下文）
 *    └─ OrderContext
 *         └─ 持有 PaymentStrategy 引用，运行时动态切换
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see AliPay
 * @see WeChatPay
 * @see CreditCardPay
 */
public interface PaymentStrategy {

    /**
     * 执行支付操作。
     *
     * <p>每种支付方式的具体算法在此实现，
     * 包括支付流程、手续费计算、第三方接口调用等。</p>
     *
     * @param amount 支付金额（元）
     * @return 支付结果描述
     */
    String pay(double amount);

    /**
     * 获取支付方式名称。
     *
     * @return 支付方式名称
     */
    String getPaymentName();

    /**
     * 计算手续费（不同策略的手续费算法不同）。
     *
     * <p>这体现了策略模式的核心价值：
     * 每种策略封装了自己的算法，客户端无需关心具体计算逻辑。</p>
     *
     * @param amount 支付金额（元）
     * @return 手续费（元）
     */
    double calculateFee(double amount);
}
