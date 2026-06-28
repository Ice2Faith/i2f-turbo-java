package i2f.design.pattern.behavioral.mediator.component;

import i2f.design.pattern.behavioral.mediator.mediator.Mediator;

/**
 * 中介者模式 —— 楼宇组件（Component）
 *
 * <p><b>角色：</b>抽象同事（Abstract Colleague）</p>
 *
 * <p><b>模式说明：</b>定义所有同事对象的公共接口，包含对中介者的引用。
 * 同事对象通过中介者与其他同事通信，而不是直接相互引用。
 * 这体现了中介者模式的核心思想：**将网状依赖转化为星形依赖**。</p>
 *
 * <p><b>依赖关系对比：</b></p>
 * <pre>
 *  没有中介者（网状依赖 —— 耦合度高）    使用中介者（星形依赖 —— 耦合度低）
 *  ─────────────────────────────────   ─────────────────────────────────
 *  A ←→ B ←→ C                         A → Mediator ← B
 *  ↑    ↓    ↑                              ↓
 *  D ←→ E ←→ F                         C → Mediator ← D
 *                                         ↓
 *                                    E → Mediator ← F
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see Mediator
 */
public abstract class Component {

    /**
     * 所属的中介者对象。
     *
     * <p>每个同事对象持有中介者的引用，通过中介者与其他同事通信。
     * 这样同事对象之间不需要直接相互引用，降低了耦合度。</p>
     */
    protected Mediator mediator;

    public Component(Mediator mediator) {
        this.mediator = mediator;
    }

    /**
     * 设置中介者。
     *
     * @param mediator 中介者对象
     */
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    /**
     * 获取组件名称。
     *
     * @return 组件名称
     */
    public abstract String getName();
}
