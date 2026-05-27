package i2f.design.pattern.behavioral.state.state.impl;

import i2f.design.pattern.behavioral.state.OrderState;
import i2f.design.pattern.behavioral.state.order.Order;

/**
 * 状态模式 —— 待支付状态（Concrete State：PendingState）
 *
 * <p><b>角色：</b>具体状态（Concrete State）</p>
 *
 * <p><b>说明：</b>订单创建后的初始状态。
 * 此状态下允许执行"支付"和"取消"操作，不允许"发货"和"确认收货"。</p>
 *
 * <p><b>状态转换规则：</b></p>
 * <pre>
 *  待支付 ──支付──→ 已支付（PaidState）
 *  待支付 ──取消──→ 已取消（CancelledState）
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 11:00
 * @see OrderState
 * @see PaidState
 * @see CancelledState
 */
public class PendingState implements OrderState {

    @Override
    public void pay(Order order) {
        System.out.println("  ✓ 支付成功！订单金额 ¥" + order.getAmount() + " 已支付");
        // 状态转换：待支付 → 已支付
        order.setState(new PaidState());
    }

    @Override
    public void ship(Order order) {
        System.out.println("  ✗ 操作失败：订单未支付，无法发货");
        System.out.println("  提示：请先完成支付操作");
    }

    @Override
    public void confirmReceive(Order order) {
        System.out.println("  ✗ 操作失败：订单未支付，无法确认收货");
        System.out.println("  提示：请先完成支付操作");
    }

    @Override
    public void cancel(Order order) {
        System.out.println("  ✓ 订单已取消");
        // 状态转换：待支付 → 已取消
        order.setState(new CancelledState());
    }

    @Override
    public String getStateName() {
        return "待支付";
    }
}
