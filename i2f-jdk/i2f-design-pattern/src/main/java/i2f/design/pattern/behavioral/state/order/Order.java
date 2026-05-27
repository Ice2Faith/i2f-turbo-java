package i2f.design.pattern.behavioral.state.order;

import i2f.design.pattern.behavioral.state.OrderState;
import i2f.design.pattern.behavioral.state.state.impl.PendingState;
import lombok.Data;

/**
 * 状态模式 —— 订单（Context：Order）
 *
 * <p><b>角色：</b>上下文（Context）</p>
 *
 * <p><b>模式说明：</b>维护一个 {@link OrderState} 的实例，表示当前状态。
 * 所有状态相关的请求都委托给当前状态对象处理，由状态对象决定具体行为和状态转换。
 * 这就是状态模式的使用方式：<b>"Context 将请求委托给当前 State 对象处理"</b>。</p>
 *
 * <p><b>命名立意：</b>"电商订单"天然地充当"上下文"角色——
 * 订单持有当前状态（待支付、已支付、配送中等），
 * 当用户执行支付、发货、确认收货等操作时，
 * 订单将这些操作委托给当前状态对象处理，
 * 状态对象判断操作是否合法，并决定是否切换到下一个状态。</p>
 *
 * <p><b>状态流转示例：</b></p>
 * <pre>
 *  待支付 ──支付──→ 已支付 ──发货──→ 配送中 ──确认收货──→ 已完成
 *    │                │
 *    └────取消────────┘
 * </pre>
 *
 * <p><b>与传统方式的对比：</b></p>
 * <pre>
 *  传统方式（if/else）
 *  ──────────────────────────────────────────────
 *  public void pay() {
 *      if (state == PENDING) {
 *          state = PAID;
 *      } else if (state == PAID) {
 *          throw new Exception("已支付，无需重复支付");
 *      } else if ...  // 大量条件判断
 *  }
 *
 *  状态模式
 *  ──────────────────────────────────────────────
 *  public void pay() {
 *      currentState.pay(this);  // 委托给状态对象，简洁优雅
 *  }
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 11:00
 * @see OrderState
 * @see PendingState
 */
@Data
public class Order {

    /**
     * 订单编号。
     */
    private String orderNo;

    /**
     * 商品名称。
     */
    private String productName;

    /**
     * 订单金额。
     */
    private double amount;

    /**
     * 当前订单状态。
     *
     * <p>初始状态为"待支付"，后续状态转换由具体状态类控制。</p>
     */
    private OrderState currentState;

    public Order(String orderNo, String productName, double amount) {
        this.orderNo = orderNo;
        this.productName = productName;
        this.amount = amount;
        // 订单创建时，初始状态为"待支付"
        this.currentState = new PendingState();
        System.out.println("  订单创建成功：[" + orderNo + "] " + productName + " ¥" + amount);
        System.out.println("  当前状态：" + currentState.getStateName());
    }

    /**
     * 支付订单 —— 委托给当前状态对象处理。
     */
    public void pay() {
        System.out.println("\n── 执行操作：支付订单 ──");
        currentState.pay(this);
    }

    /**
     * 发货 —— 委托给当前状态对象处理。
     */
    public void ship() {
        System.out.println("\n── 执行操作：发货 ──");
        currentState.ship(this);
    }

    /**
     * 确认收货 —— 委托给当前状态对象处理。
     */
    public void confirmReceive() {
        System.out.println("\n── 执行操作：确认收货 ──");
        currentState.confirmReceive(this);
    }

    /**
     * 取消订单 —— 委托给当前状态对象处理。
     */
    public void cancel() {
        System.out.println("\n── 执行操作：取消订单 ──");
        currentState.cancel(this);
    }

    /**
     * 切换状态。
     *
     * <p>由具体状态类调用，完成状态转换。</p>
     *
     * @param newState 新状态
     */
    public void setState(OrderState newState) {
        System.out.println("  状态切换：" + this.currentState.getStateName() + " → " + newState.getStateName());
        this.currentState = newState;
    }

    public String getStateName(){
        if(this.currentState==null){
            return "状态未知";
        }
        return this.currentState.getStateName();
    }

    @Override
    public String toString() {
        return String.format("Order{orderNo='%s', product='%s', amount=%.2f, state='%s'}",
                orderNo, productName, amount, currentState.getStateName());
    }
}
