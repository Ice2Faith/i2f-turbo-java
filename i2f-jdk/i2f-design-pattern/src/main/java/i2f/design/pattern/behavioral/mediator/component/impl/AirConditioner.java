package i2f.design.pattern.behavioral.mediator.component.impl;

import i2f.design.pattern.behavioral.mediator.component.Component;
import i2f.design.pattern.behavioral.mediator.mediator.Mediator;

/**
 * 中介者模式 —— 空调系统（AirConditioner）
 *
 * <p><b>角色：</b>具体同事（Concrete Colleague）</p>
 *
 * <p><b>业务场景：</b>智能楼宇的空调温控系统。当空调启动或关闭时，
 * 通知中介者进行联动处理（如：空调启动时自动关闭窗户以保持温度）。</p>
 *
 * <p><b>设计亮点：</b>空调系统只关注自身的温度控制，不需要了解其他设备的状态。
 * 当需要与其他设备协作时（如温度过高时开启窗帘遮阳），完全由中介者负责协调。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see Component
 * @see Mediator
 */
public class AirConditioner extends Component {

    /**
     * 空调状态：true=开启，false=关闭。
     */
    private boolean isOn;

    /**
     * 设定温度（摄氏度）。
     */
    private double temperature;

    public AirConditioner(Mediator mediator) {
        super(mediator);
        this.isOn = false;
        this.temperature = 25.0;
    }

    /**
     * 开启空调。
     *
     * <p>空调启动后，通知中介者进行联动处理（如：自动关闭窗户）。</p>
     */
    public void turnOn() {
        this.isOn = true;
        System.out.println("  ❄️ [" + getName() + "] 空调已开启，设定温度：" + temperature + "°C");
        // 通知中介者：空调已开启
        mediator.notify(this, "AC_ON");
    }

    /**
     * 关闭空调。
     */
    public void turnOff() {
        this.isOn = false;
        System.out.println("  ❄️ [" + getName() + "] 空调已关闭");
        // 通知中介者：空调已关闭
        mediator.notify(this, "AC_OFF");
    }

    /**
     * 调节温度。
     *
     * @param temperature 目标温度
     */
    public void setTemperature(double temperature) {
        this.temperature = temperature;
        System.out.println("  ❄️ [" + getName() + "] 温度调节至：" + temperature + "°C");
    }

    /**
     * 由中介者调用的控制方法。
     *
     * @param on         true=开启，false=关闭
     * @param temperature 设定温度
     */
    public void setState(boolean on, double temperature) {
        this.isOn = on;
        this.temperature = temperature;
        System.out.println("  ❄️ [" + getName() + "] 被中介者控制 -> " + (on ? "开启" : "关闭") + "，温度：" + temperature + "°C");
    }

    public boolean isOn() {
        return isOn;
    }

    public double getTemperature() {
        return temperature;
    }

    @Override
    public String getName() {
        return "空调系统";
    }

    @Override
    public String toString() {
        return "AirConditioner{name='" + getName() + "', isOn=" + isOn + ", temperature=" + temperature + "°C}";
    }
}
