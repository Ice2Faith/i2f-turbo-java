package i2f.design.pattern.behavioral.state.state.impl;

import i2f.design.pattern.behavioral.state.OrderState;
import i2f.design.pattern.behavioral.state.order.Order;

/**
 * 状态模式 —— 已取消状态（Concrete State：CancelledState）
 *
 * <p><b>角色：</b>具体状态（Concrete State）</p>
 *
 * <p><b>说明：</b>订单已被取消。
 * 这是一个终态（Terminal State），不允许执行任何操作。</p>
 *
 * <p><b>状态转换规则：</b></p>
 * <pre>
 *  已取消 → 终态，无后续状态转换
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 11:00
 * @see OrderState
 */
public class CancelledState implements OrderState {

    @Override
    public void pay(Order order) {
        System.out.println("  ✗ 操作失败：订单已取消，无法支付");
        System.out.println("  提示：请重新下单");
    }

    @Override
    public void ship(Order order) {
        System.out.println("  ✗ 操作失败：订单已取消，无法发货");
        System.out.println("  提示：请重新下单");
    }

    @Override
    public void confirmReceive(Order order) {
        System.out.println("  ✗ 操作失败：订单已取消，无法确认收货");
        System.out.println("  提示：请重新下单");
    }

    @Override
    public void cancel(Order order) {
        System.out.println("  ✗ 操作失败：订单已取消，无需重复取消");
        System.out.println("  提示：当前状态为" + order.getStateName());
    }

    @Override
    public String getStateName() {
        return "已取消";
    }
}
