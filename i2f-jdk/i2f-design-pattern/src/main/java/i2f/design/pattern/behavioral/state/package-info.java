/**
 * 状态模式（State）
 * <p>
 * 允许一个对象在其内部状态改变时改变它的行为，对象看起来似乎修改了它的类。
 * 分类：行为型模式
 * </p>
 *
 * <p><b>场景说明：</b>以"电商订单状态流转"为例——订单在不同状态（待支付、已支付、
 * 配送中、已完成、已取消）下，允许执行的操作各不相同。
 * 通过状态模式，将状态相关的行为封装到独立的状态类中，避免大量的 if/else 判断。</p>
 *
 * <p><b>模式结构：</b></p>
 * <pre>
 *  State（抽象状态）
 *    └─ OrderState
 *
 *  ConcreteState（具体状态）
 *    ├─ PendingState（待支付）
 *    ├─ PaidState（已支付）
 *    ├─ ShippingState（配送中）
 *    ├─ CompletedState（已完成）
 *    └─ CancelledState（已取消）
 *
 *  Context（上下文）
 *    └─ Order（订单，持有当前状态并委托状态对象处理请求）
 *
 *  Test（测试演示）
 *    └─ 演示订单状态流转过程
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 11:00
 */
package i2f.design.pattern.behavioral.state;
