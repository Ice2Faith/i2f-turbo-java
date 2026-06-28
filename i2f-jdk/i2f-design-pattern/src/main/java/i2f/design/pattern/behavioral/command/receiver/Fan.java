package i2f.design.pattern.behavioral.command.receiver;

import i2f.design.pattern.behavioral.command.command.impl.FanOnCommand;

/**
 * 命令模式 —— 电风扇（Receiver：Fan）
 *
 * <p><b>角色：</b>接收者（Receiver）</p>
 *
 * <p><b>说明：</b>电风扇是另一种智能家居设备，支持开关和调节档位操作。
 * 与电灯类似，它也可以被封装为命令对象，由遥控器统一调度。
 * 多个接收者（Light、Fan、TV 等）可以同时被同一个遥控器控制。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see FanOnCommand
 * @see i2f.design.pattern.behavioral.command.command.FanOffCommand
 */
public class Fan {

    /**
     * 电风扇所在位置。
     */
    private final String location;

    /**
     * 当前档位（0-3，0 表示关闭）。
     */
    private int speed;

    /**
     * 电风扇开关状态。
     */
    private boolean isOn;

    public Fan(String location) {
        this.location = location;
        this.speed = 0;
        this.isOn = false;
    }

    /**
     * 开启电风扇。
     *
     * <p>将电风扇档位设置为默认值（1 档），标记为开启状态。</p>
     */
    public void turnOn() {
        this.isOn = true;
        this.speed = 1;
        System.out.println(String.format("  🌀 [%s] 电风扇已开启，当前档位：%d 档", location, speed));
    }

    /**
     * 关闭电风扇。
     *
     * <p>将电风扇档位设置为 0，标记为关闭状态。</p>
     */
    public void turnOff() {
        this.isOn = false;
        this.speed = 0;
        System.out.println(String.format("  🌀 [%s] 电风扇已关闭", location));
    }

    /**
     * 调节档位。
     *
     * @param newSpeed 新的档位值（0-3）
     */
    public void setSpeed(int newSpeed) {
        if (newSpeed < 0 || newSpeed > 3) {
            throw new IllegalArgumentException("档位必须在 0-3 之间");
        }
        this.speed = newSpeed;
        this.isOn = newSpeed > 0;
        System.out.println(String.format("  🌀 [%s] 档位已调节至 %d 档", location, speed));
    }

    /**
     * 获取当前档位。
     *
     * @return 档位值（0-3）
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * 获取电风扇状态。
     *
     * @return 是否开启
     */
    public boolean isOn() {
        return isOn;
    }

    /**
     * 获取电风扇所在位置。
     *
     * @return 位置名称
     */
    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return String.format("Fan{location='%s', isOn=%s, speed=%d}", location, isOn, speed);
    }
}
