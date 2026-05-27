package i2f.design.pattern.structural.bridge.remote.impl;

import i2f.design.pattern.structural.bridge.device.Device;
import i2f.design.pattern.structural.bridge.remote.RemoteControl;

/**
 * 桥接模式 —— 基础遥控器（Refined Abstraction：BasicRemote）
 *
 * <p><b>角色：</b>扩展抽象（Refined Abstraction）</p>
 *
 * <p><b>说明：</b>基础遥控器继承 {@link RemoteControl}，提供基本的遥控功能：
 * 电源开关、音量调节。它通过组合 {@link Device} 接口来控制各种设备，
 * 不关心设备的具体类型（电视、收音机等），体现桥接模式的"抽象与实现分离"。</p>
 *
 * <p><b>设计意图：</b>基础遥控器代表"最小功能集"，适用于简单场景。
 * 如果未来需要增加高级功能（如频道切换、定时关机），
 * 可以创建 {@link AdvancedRemote} 子类扩展，而无需修改此类。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see RemoteControl
 * @see AdvancedRemote
 * @see Device
 */
public class BasicRemote extends RemoteControl {

    public BasicRemote(Device device) {
        super(device);
    }

    @Override
    public String getRemoteType() {
        return "基础遥控器";
    }

    @Override
    public String toString() {
        return String.format("BasicRemote{控制的设备=%s}", device.getDeviceType());
    }
}
