package i2f.design.pattern.behavioral.strategy.payment.impl;

import i2f.design.pattern.behavioral.strategy.payment.PaymentStrategy;

/**
 * 策略模式 —— 信用卡支付（Concrete Strategy：CreditCardPay）
 *
 * <p><b>角色：</b>具体策略（Concrete Strategy）</p>
 *
 * <p><b>说明：</b>实现信用卡支付算法，包括支付流程和手续费计算。
 * 信用卡支付手续费通常较高（约 2.5%），但支持分期付款。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see PaymentStrategy
 */
public class CreditCardPay implements PaymentStrategy {

    /**
     * 银行网关标识（模拟）。
     */
    private static final String BANK_GATEWAY = "CMB_CREDIT_GATEWAY";

    /**
     * 是否支持分期。
     */
    private final boolean supportInstallment;

    public CreditCardPay() {
        this.supportInstallment = true;
    }

    public CreditCardPay(boolean supportInstallment) {
        this.supportInstallment = supportInstallment;
    }

    @Override
    public String pay(double amount) {
        double fee = calculateFee(amount);
        String installmentInfo = supportInstallment ? "（支持分期）" : "（不支持分期）";
        return String.format("[信用卡] 订单金额 %.2f 元，手续费 %.2f 元（费率 2.5%%），" +
                "实际到账 %.2f 元，银行网关：%s%s", amount, fee, amount - fee, BANK_GATEWAY, installmentInfo);
    }

    @Override
    public String getPaymentName() {
        return "信用卡支付";
    }

    @Override
    public double calculateFee(double amount) {
        // 信用卡手续费：2.5%（高于第三方支付）
        return amount * 0.025;
    }
}
