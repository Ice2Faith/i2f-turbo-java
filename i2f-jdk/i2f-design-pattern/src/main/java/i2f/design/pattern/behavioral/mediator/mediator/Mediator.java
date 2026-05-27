package i2f.design.pattern.behavioral.mediator.mediator;

import i2f.design.pattern.behavioral.mediator.component.Component;

/**
 * 中介者模式 —— 楼宇控制器（Mediator）
 *
 * <p><b>角色：</b>抽象中介者（Abstract Mediator）</p>
 *
 * <p><b>模式说明：</b>定义中介者接口，声明用于与各同事对象（Component）通信的方法。
 * 中介者的核心职责是封装多个对象之间的复杂交互，使这些对象不需要显式地相互引用，
 * 从而降低它们之间的耦合度，实现"迪米特法则"（最少知识原则）。</p>
 *
 * <p><b>命名立意：</b>以"智能楼宇控制系统"为场景——楼宇控制器作为中介者，
 * 协调灯光、空调、窗帘、安防等各个子系统的工作。当某个子系统状态变化时，
 * 它只需通知楼宇控制器，由控制器决定如何联动其他子系统，
 * 各子系统之间互不直接依赖。</p>
 *
 * <p><b>与观察者模式的区别：</b></p>
 * <pre>
 *  观察者模式（Observer）              中介者模式（Mediator）
 *  ────────────────────────────────   ────────────────────────────────
 *  一对多的依赖关系                      多对多的复杂交互
 *  主题通知所有观察者                    中介者协调所有参与者
 *  观察者之间无交互                      参与者之间通过中介者间接交互
 *  适合事件通知场景                      适合复杂协作场景
 * </pre>
 *
 * <p><b>模式结构：</b></p>
 * <pre>
 *  Mediator（抽象中介者）
 *    └─ notify(sender, event): void     ← 接收同事对象的通知并协调
 *
 *  ConcreteMediator（具体中介者）
 *    └─ SmartBuildingController         ← 智能楼宇控制器，协调各子系统
 *
 *  Colleague（抽象同事）
 *    └─ Component                       ← 楼宇组件抽象
 *
 *  ConcreteColleague（具体同事）
 *    ├─ Light                           ← 灯光系统
 *    ├─ AirConditioner                  ← 空调系统
 *    ├─ Curtain                         ← 窗帘系统
 *    └─ SecuritySystem                  ← 安防系统
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see Component
 */
public abstract class Mediator {

    /**
     * 接收同事对象的通知并进行协调。
     *
     * <p>当某个同事对象（Component）状态发生变化时，调用此方法通知中介者，
     * 由中介者决定如何协调其他同事对象。这是中介者模式的核心方法。</p>
     *
     * @param sender 发送通知的同事对象
     * @param event  发生的事件类型
     */
    public abstract void notify(Component sender, String event);
}
