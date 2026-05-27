package i2f.design.pattern.behavioral.state;

import i2f.design.pattern.behavioral.state.order.Order;
import i2f.design.pattern.behavioral.state.state.impl.CancelledState;
import i2f.design.pattern.behavioral.state.state.impl.CompletedState;
import i2f.design.pattern.behavioral.state.state.impl.PaidState;
import i2f.design.pattern.behavioral.state.state.impl.PendingState;
import i2f.design.pattern.behavioral.state.state.impl.ShippingState;

/**
 * 状态模式 —— 订单状态（State：OrderState）
 *
 * <p><b>角色：</b>抽象状态（Abstract State）</p>
 *
 * <p><b>模式说明：</b>定义一个接口，封装与 {@link Order} 特定状态相关的行为。
 * 每个具体状态类实现这些方法，定义该状态下的具体操作逻辑和状态转换规则。
 * 这就是状态模式的核心：<b>"对象的行为随状态改变而改变"</b>。</p>
 *
 * <p><b>命名立意：</b>"订单状态"天然地充当"状态"角色——
 * 待支付状态（{@link PendingState}）允许支付操作、
 * 已支付状态（{@link PaidState}）允许发货操作、
 * 配送中状态（{@link ShippingState}）允许确认收货操作。
 * 订单对象只需委托当前状态对象处理请求，状态自行决定允许执行的操作。</p>
 *
 * <p><b>与传统 if/else 方式的区别：</b></p>
 * <pre>
 *  if/else 方式                          状态模式（State Pattern）
 *  ─────────────────────────────────────   ─────────────────────────────────────
 *  所有状态逻辑集中在一个类中               每个状态独立成类，职责清晰
 *  新增状态需修改大量条件判断（违反开闭原则） 新增状态只需新增一个状态类（符合开闭原则）
 *  代码臃肿，难以维护                      代码分散到各状态类，易于扩展
 * </pre>
 *
 * <p><b>模式结构：</b></p>
 * <pre>
 *  State（抽象状态）
 *    ├─ pay(order): void                  ← 支付操作
 *    ├─ ship(order): void                 ← 发货操作
 *    ├─ confirmReceive(order): void       ← 确认收货操作
 *    ├─ cancel(order): void               ← 取消订单操作
 *    └─ getStateName(): String            ← 获取状态名称
 *
 *  ConcreteState（具体状态）
 *    ├─ PendingState    → 允许支付、取消
 *    ├─ PaidState       → 允许发货
 *    ├─ ShippingState   → 允许确认收货
 *    ├─ CompletedState  → 终态，不允许任何操作
 *    └─ CancelledState  → 终态，不允许任何操作
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 11:00
 * @see Order
 * @see PendingState
 * @see PaidState
 * @see ShippingState
 * @see CompletedState
 * @see CancelledState
 */
public interface OrderState {

    /**
     * 支付订单。
     *
     * <p>不同状态下支付操作的合法性不同：
     * 待支付状态允许支付并转为已支付状态，其他状态不允许支付。</p>
     *
     * @param order 订单上下文
     */
    void pay(Order order);

    /**
     * 发货。
     *
     * <p>不同状态下发货操作的合法性不同：
     * 已支付状态允许发货并转为配送中状态，其他状态不允许发货。</p>
     *
     * @param order 订单上下文
     */
    void ship(Order order);

    /**
     * 确认收货。
     *
     * <p>不同状态下确认收货操作的合法性不同：
     * 配送中状态允许确认收货并转为已完成状态，其他状态不允许确认收货。</p>
     *
     * @param order 订单上下文
     */
    void confirmReceive(Order order);

    /**
     * 取消订单。
     *
     * <p>不同状态下取消操作的合法性不同：
     * 待支付和已支付状态允许取消，配送中和已完成状态不允许取消。</p>
     *
     * @param order 订单上下文
     */
    void cancel(Order order);

    /**
     * 获取当前状态名称。
     *
     * @return 状态名称
     */
    String getStateName();
}
