package i2f.design.pattern.behavioral.mediator.component.impl;

import i2f.design.pattern.behavioral.mediator.component.Component;
import i2f.design.pattern.behavioral.mediator.mediator.Mediator;

/**
 * 中介者模式 —— 窗帘系统（Curtain）
 *
 * <p><b>角色：</b>具体同事（Concrete Colleague）</p>
 *
 * <p><b>业务场景：</b>智能楼宇的电动窗帘系统。窗帘的开合会影响室内采光和温度，
 * 因此需要与其他设备协调工作（如：开灯时自动拉上窗帘避免眩光、空调开启时关闭窗帘保温）。</p>
 *
 * <p><b>解耦优势：</b>如果没有中介者，窗帘系统需要直接依赖灯光、空调、安防等多个系统，
 * 导致复杂的网状依赖。使用中介者后，窗帘只需与楼宇控制器通信即可。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see Component
 * @see Mediator
 */
public class Curtain extends Component {

    /**
     * 窗帘状态：true=关闭（遮光），false=打开（透光）。
     */
    private boolean isClosed;

    public Curtain(Mediator mediator) {
        super(mediator);
        this.isClosed = false;
    }

    /**
     * 关闭窗帘（遮光模式）。
     *
     * <p>窗帘关闭后，通知中介者进行联动处理（如：可能需要自动开灯补充照明）。</p>
     */
    public void close() {
        this.isClosed = true;
        System.out.println("  🪟 [" + getName() + "] 窗帘已关闭（遮光）");
        // 通知中介者：窗帘已关闭
        mediator.notify(this, "CURTAIN_CLOSED");
    }

    /**
     * 打开窗帘（透光模式）。
     */
    public void open() {
        this.isClosed = false;
        System.out.println("  🪟 [" + getName() + "] 窗帘已打开（透光）");
        // 通知中介者：窗帘已打开
        mediator.notify(this, "CURTAIN_OPEN");
    }

    /**
     * 由中介者调用的控制方法。
     *
     * @param closed true=关闭，false=打开
     */
    public void setState(boolean closed) {
        this.isClosed = closed;
        System.out.println("  🪟 [" + getName() + "] 被中介者控制 -> " + (closed ? "关闭" : "打开"));
    }

    public boolean isClosed() {
        return isClosed;
    }

    @Override
    public String getName() {
        return "窗帘系统";
    }

    @Override
    public String toString() {
        return "Curtain{name='" + getName() + "', isClosed=" + isClosed + "}";
    }
}
