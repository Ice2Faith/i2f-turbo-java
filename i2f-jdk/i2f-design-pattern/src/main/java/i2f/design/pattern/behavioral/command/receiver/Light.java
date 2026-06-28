package i2f.design.pattern.behavioral.command.receiver;

import i2f.design.pattern.behavioral.command.command.impl.LightOffCommand;
import i2f.design.pattern.behavioral.command.command.impl.LightOnCommand;

/**
 * 命令模式 —— 电灯（Receiver：Light）
 *
 * <p><b>角色：</b>接收者（Receiver）</p>
 *
 * <p><b>模式说明：</b>接收者是真正执行命令的对象，包含与业务逻辑相关的具体操作。
 * 命令对象会持有接收者的引用，并在 {@code execute()} 方法中调用接收者的相应方法。
 * 调用者（Invoker）不直接与接收者交互，而是通过命令对象间接调用。</p>
 *
 * <p><b>命名立意：</b>电灯是智能家居中的基础设备，支持开灯、关灯、调节亮度等操作。
 * 每个操作都可以被封装为一个独立的命令对象，遥控器通过命令对象控制电灯，
 * 无需直接操作电灯实例，实现完全解耦。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see LightOnCommand
 * @see LightOffCommand
 */
public class Light {

    /**
     * 电灯所在房间。
     */
    private final String room;

    /**
     * 当前亮度（0-100，0 表示关闭）。
     */
    private int brightness;

    /**
     * 电灯开关状态。
     */
    private boolean isOn;

    public Light(String room) {
        this.room = room;
        this.brightness = 0;
        this.isOn = false;
    }

    /**
     * 开灯。
     *
     * <p>将电灯亮度设置为默认值（50%），标记为开启状态。</p>
     */
    public void turnOn() {
        this.isOn = true;
        this.brightness = 50;
        System.out.println(String.format("  💡 [%s] 电灯已开启，当前亮度：%d%%", room, brightness));
    }

    /**
     * 关灯。
     *
     * <p>将电灯亮度设置为 0，标记为关闭状态。</p>
     */
    public void turnOff() {
        this.isOn = false;
        this.brightness = 0;
        System.out.println(String.format("  💡 [%s] 电灯已关闭", room));
    }

    /**
     * 调节亮度。
     *
     * @param newBrightness 新的亮度值（0-100）
     */
    public void setBrightness(int newBrightness) {
        if (newBrightness < 0 || newBrightness > 100) {
            throw new IllegalArgumentException("亮度必须在 0-100 之间");
        }
        this.brightness = newBrightness;
        this.isOn = newBrightness > 0;
        System.out.println(String.format("  💡 [%s] 亮度已调节至 %d%%", room, brightness));
    }

    /**
     * 获取当前亮度。
     *
     * @return 亮度值（0-100）
     */
    public int getBrightness() {
        return brightness;
    }

    /**
     * 获取电灯状态。
     *
     * @return 是否开启
     */
    public boolean isOn() {
        return isOn;
    }

    /**
     * 获取电灯所在房间。
     *
     * @return 房间名称
     */
    public String getRoom() {
        return room;
    }

    @Override
    public String toString() {
        return String.format("Light{room='%s', isOn=%s, brightness=%d%%}", room, isOn, brightness);
    }
}
