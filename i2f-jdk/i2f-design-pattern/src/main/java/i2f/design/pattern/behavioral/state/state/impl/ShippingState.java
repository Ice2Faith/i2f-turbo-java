package i2f.design.pattern.behavioral.state.state.impl;

import i2f.design.pattern.behavioral.state.OrderState;
import i2f.design.pattern.behavioral.state.order.Order;

/**
 * 状态模式 —— 配送中状态（Concrete State：ShippingState）
 *
 * <p><b>角色：</b>具体状态（Concrete State）</p>
 *
 * <p><b>说明：</b>商家已发货，商品正在配送途中。
 * 此状态下只允许执行"确认收货"操作，不允许"支付"、"发货"和"取消"。</p>
 *
 * <p><b>状态转换规则：</b></p>
 * <pre>
 *  配送中 ──确认收货──→ 已完成（CompletedState）
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 11:00
 * @see OrderState
 * @see CompletedState
 */
public class ShippingState implements OrderState {

    @Override
    public void pay(Order order) {
        System.out.println("  ✗ 操作失败：订单已支付，无需重复支付");
        System.out.println("  提示：当前状态为" + order.getStateName());
    }

    @Override
    public void ship(Order order) {
        System.out.println("  ✗ 操作失败：订单已发货，无需重复发货");
        System.out.println("  提示：当前状态为" + order.getStateName());
    }

    @Override
    public void confirmReceive(Order order) {
        System.out.println("  ✓ 确认收货成功！感谢您的购买");
        // 状态转换：配送中 → 已完成
        order.setState(new CompletedState());
    }

    @Override
    public void cancel(Order order) {
        System.out.println("  ✗ 操作失败：商品已在配送途中，无法取消订单");
        System.out.println("  提示：如不满意可申请退换货");
    }

    @Override
    public String getStateName() {
        return "配送中";
    }
}
