package i2f.design.pattern.behavioral.strategy.payment.impl;

import i2f.design.pattern.behavioral.strategy.payment.PaymentStrategy;

/**
 * 策略模式 —— 支付宝支付（Concrete Strategy：AliPay）
 *
 * <p><b>角色：</b>具体策略（Concrete Strategy）</p>
 *
 * <p><b>说明：</b>实现支付宝支付算法，包括支付流程和手续费计算。
 * 支付宝手续费通常为 0.6%，提现免费。</p>
 *
 * <p><b>开闭原则体现：</b>如果未来需要新增"银联支付"，
 * 只需新增 {@code UnionPay} 类实现 {@link PaymentStrategy} 接口，
 * 无需修改现有任何支付策略或订单上下文代码。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see PaymentStrategy
 */
public class AliPay implements PaymentStrategy {

    /**
     * 支付宝商户 ID（模拟）。
     */
    private static final String MERCHANT_ID = "ALI_20260521001";

    @Override
    public String pay(double amount) {
        double fee = calculateFee(amount);
        return String.format("[支付宝] 订单金额 %.2f 元，手续费 %.2f 元（费率 0.6%%），" +
                "实际到账 %.2f 元，商户号：%s", amount, fee, amount - fee, MERCHANT_ID);
    }

    @Override
    public String getPaymentName() {
        return "支付宝支付";
    }

    @Override
    public double calculateFee(double amount) {
        // 支付宝手续费：0.6%
        return amount * 0.006;
    }
}
