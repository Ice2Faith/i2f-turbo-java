package i2f.design.pattern.structural.bridge.remote.impl;

import i2f.design.pattern.structural.bridge.device.Device;
import i2f.design.pattern.structural.bridge.device.impl.TV;
import i2f.design.pattern.structural.bridge.device.impl.Radio;
import i2f.design.pattern.structural.bridge.remote.RemoteControl;

/**
 * 桥接模式 —— 高级遥控器（Refined Abstraction：AdvancedRemote）
 *
 * <p><b>角色：</b>扩展抽象（Refined Abstraction）</p>
 *
 * <p><b>说明：</b>高级遥控器继承 {@link RemoteControl}，在基础功能之上扩展了
 * 设备特有功能：电视频道切换、收音机频率调谐。通过 {@code instanceof} 判断
 * 设备类型并向下转型调用特有方法，演示桥接模式如何在保持抽象的同时
 * 支持设备差异化操作。</p>
 *
 * <p><b>扩展性体现：</b>高级遥控器可以独立于设备演进——
 * 未来可增加语音控制、手势识别、定时关机等功能，
 * 而无需修改任何设备实现类。同时，新增设备类型（如空调）也无需修改此类，
 * 除非需要为空调提供特殊控制功能。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see RemoteControl
 * @see BasicRemote
 * @see TV
 * @see Radio
 */
public class AdvancedRemote extends RemoteControl {

    public AdvancedRemote(Device device) {
        super(device);
    }

    @Override
    public String getRemoteType() {
        return "高级遥控器";
    }

    /**
     * 切换到指定频道（仅电视支持）。
     *
     * <p>演示桥接模式如何在抽象层调用实现层的扩展功能。
     * 通过类型检查确保只在合适的设备上执行对应操作。</p>
     *
     * @param channel 目标频道号
     */
    public void setChannel(int channel) {
        System.out.println("  ┌─ 高级遥控器操作：切换频道");
        if (device instanceof TV) {
            TV tv = (TV) device;
            tv.setChannel(channel);
            System.out.println("  │ 设备类型：电视");
            System.out.println("  │ 频道切换：→ " + channel);
            System.out.println("  └─ 操作完成\n");
        } else {
            System.out.println("  │ 设备类型：" + device.getDeviceType());
            System.out.println("  │ ⚠️ 该设备不支持频道切换功能");
            System.out.println("  └─ 操作失败\n");
        }
    }

    /**
     * 调谐到指定频率（仅收音机支持）。
     *
     * <p>演示桥接模式如何在抽象层调用实现层的扩展功能。
     * 通过类型检查确保只在合适的设备上执行对应操作。</p>
     *
     * @param frequency 目标频率（MHz）
     */
    public void tuneFrequency(double frequency) {
        System.out.println("  ┌─ 高级遥控器操作：调谐频率");
        if (device instanceof Radio) {
            Radio radio = (Radio) device;
            radio.setFrequency(frequency);
            System.out.println("  │ 设备类型：收音机");
            System.out.println("  │ 频率调谐：→ " + frequency + " MHz");
            System.out.println("  └─ 操作完成\n");
        } else {
            System.out.println("  │ 设备类型：" + device.getDeviceType());
            System.out.println("  │ ⚠️ 该设备不支持频率调谐功能");
            System.out.println("  └─ 操作失败\n");
        }
    }

    /**
     * 静音功能（通用扩展功能）。
     */
    public void mute() {
        System.out.println("  ┌─ 高级遥控器操作：静音");
        while (device.volumeDown() > 0) {
            // 连续降低音量直到静音
        }
        System.out.println("  │ 设备类型：" + device.getDeviceType());
        System.out.println("  │ 静音操作：→ 音量已调至 0");
        System.out.println("  └─ 操作完成\n");
    }

    @Override
    public String toString() {
        return String.format("AdvancedRemote{控制的设备=%s}", device.getDeviceType());
    }
}
