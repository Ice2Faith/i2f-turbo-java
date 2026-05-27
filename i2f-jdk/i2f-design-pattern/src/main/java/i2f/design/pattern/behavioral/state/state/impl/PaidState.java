package i2f.design.pattern.behavioral.state.state.impl;

import i2f.design.pattern.behavioral.state.OrderState;
import i2f.design.pattern.behavioral.state.order.Order;

/**
 * 状态模式 —— 已支付状态（Concrete State：PaidState）
 *
 * <p><b>角色：</b>具体状态（Concrete State）</p>
 *
 * <p><b>说明：</b>订单已完成支付，等待商家发货。
 * 此状态下允许执行"发货"和"取消"操作，不允许"支付"和"确认收货"。</p>
 *
 * <p><b>状态转换规则：</b></p>
 * <pre>
 *  已支付 ──发货──→ 配送中（ShippingState）
 *  已支付 ──取消──→ 已取消（CancelledState）
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 11:00
 * @see OrderState
 * @see ShippingState
 * @see CancelledState
 */
public class PaidState implements OrderState {

    @Override
    public void pay(Order order) {
        System.out.println("  ✗ 操作失败：订单已支付，无需重复支付");
        System.out.println("  提示：当前状态为" + order.getStateName());
    }

    @Override
    public void ship(Order order) {
        System.out.println("  ✓ 发货成功！商品正在配送途中");
        // 状态转换：已支付 → 配送中
        order.setState(new ShippingState());
    }

    @Override
    public void confirmReceive(Order order) {
        System.out.println("  ✗ 操作失败：商品尚未发货，无法确认收货");
        System.out.println("  提示：请等待商家发货");
    }

    @Override
    public void cancel(Order order) {
        System.out.println("  ✓ 订单已取消，退款将原路返回");
        // 状态转换：已支付 → 已取消
        order.setState(new CancelledState());
    }

    @Override
    public String getStateName() {
        return "已支付";
    }
}
