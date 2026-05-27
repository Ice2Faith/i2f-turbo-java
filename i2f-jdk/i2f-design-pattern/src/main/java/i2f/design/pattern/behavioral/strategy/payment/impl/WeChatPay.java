package i2f.design.pattern.behavioral.strategy.payment.impl;

import i2f.design.pattern.behavioral.strategy.payment.PaymentStrategy;

/**
 * 策略模式 —— 微信支付（Concrete Strategy：WeChatPay）
 *
 * <p><b>角色：</b>具体策略（Concrete Strategy）</p>
 *
 * <p><b>说明：</b>实现微信支付算法，包括支付流程和手续费计算。
 * 微信支付手续费通常为 0.6%，与支付宝类似但商户体系不同。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see PaymentStrategy
 */
public class WeChatPay implements PaymentStrategy {

    /**
     * 微信商户号（模拟）。
     */
    private static final String MCH_ID = "WX_1620260521";

    @Override
    public String pay(double amount) {
        double fee = calculateFee(amount);
        return String.format("[微信支付] 订单金额 %.2f 元，手续费 %.2f 元（费率 0.6%%），" +
                "实际到账 %.2f 元，商户号：%s", amount, fee, amount - fee, MCH_ID);
    }

    @Override
    public String getPaymentName() {
        return "微信支付";
    }

    @Override
    public double calculateFee(double amount) {
        // 微信支付手续费：0.6%
        return amount * 0.006;
    }
}
