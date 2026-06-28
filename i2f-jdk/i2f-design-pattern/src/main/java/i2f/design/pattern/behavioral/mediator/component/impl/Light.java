package i2f.design.pattern.behavioral.mediator.component.impl;

import i2f.design.pattern.behavioral.mediator.component.Component;
import i2f.design.pattern.behavioral.mediator.mediator.Mediator;

/**
 * 中介者模式 —— 灯光系统（Light）
 *
 * <p><b>角色：</b>具体同事（Concrete Colleague）</p>
 *
 * <p><b>业务场景：</b>智能楼宇的灯光控制系统。当灯光状态变化时（如用户手动开灯），
 * 它会通知中介者（楼宇控制器），由中介者决定如何联动其他设备（如自动关闭窗帘、调节空调）。</p>
 *
 * <p><b>解耦体现：</b>灯光系统不需要知道空调、窗帘、安防系统的存在，
 * 它只需将状态变化通知给中介者即可。这符合"迪米特法则"——最少知识原则。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see Component
 * @see Mediator
 */
public class Light extends Component {

    /**
     * 灯光状态：true=开启，false=关闭。
     */
    private boolean isOn;

    public Light(Mediator mediator) {
        super(mediator);
        this.isOn = false;
    }

    /**
     * 开启灯光。
     *
     * <p>灯光开启后，通知中介者进行联动处理（如：夜间模式自动拉上窗帘）。</p>
     */
    public void turnOn() {
        this.isOn = true;
        System.out.println("  💡 [" + getName() + "] 灯光已开启");
        // 通知中介者：灯光已开启
        mediator.notify(this, "LIGHT_ON");
    }

    /**
     * 关闭灯光。
     *
     * <p>灯光关闭后，通知中介者进行联动处理。</p>
     */
    public void turnOff() {
        this.isOn = false;
        System.out.println("  💡 [" + getName() + "] 灯光已关闭");
        // 通知中介者：灯光已关闭
        mediator.notify(this, "LIGHT_OFF");
    }

    /**
     * 由中介者调用的控制方法。
     *
     * <p>中介者根据业务逻辑协调各设备时，会直接调用同事对象的方法。
     * 例如：安防系统触发报警时，中介者会调用此方法自动开启所有灯光。</p>
     *
     * @param on true=开启，false=关闭
     */
    public void setState(boolean on) {
        this.isOn = on;
        System.out.println("  💡 [" + getName() + "] 被中介者控制 -> " + (on ? "开启" : "关闭"));
    }

    public boolean isOn() {
        return isOn;
    }

    @Override
    public String getName() {
        return "灯光系统";
    }

    @Override
    public String toString() {
        return "Light{name='" + getName() + "', isOn=" + isOn + "}";
    }
}
