package i2f.design.pattern.structural.bridge.device.impl;

import i2f.design.pattern.structural.bridge.device.Device;
import i2f.design.pattern.structural.bridge.remote.RemoteControl;
import lombok.Data;

/**
 * 桥接模式 —— 电视（Concrete Implementor：TV）
 *
 * <p><b>角色：</b>具体实现（Concrete Implementor）</p>
 *
 * <p><b>说明：</b>电视设备实现 {@link Device} 接口，提供电视特有的操作：
 * 频道切换、音量调节等。遥控器通过 {@link Device} 接口控制电视，
 * 无需关心电视内部实现细节，体现桥接模式的"实现分离"思想。</p>
 *
 * <p><b>独立变化体现：</b>电视可以独立演进（如增加智能功能、4K支持），
 * 不会影响遥控器层的代码；反之，遥控器增加新功能也不会影响电视实现。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see Device
 * @see RemoteControl
 */
@Data
public class TV implements Device {

    /**
     * 电视品牌型号。
     */
    private String brand;

    /**
     * 当前频道号。
     */
    private int channel;

    /**
     * 当前音量（0-100）。
     */
    private int volume;

    /**
     * 电源状态。
     */
    private boolean isPowerOn;

    public TV(String brand) {
        this.brand = brand;
        this.channel = 1;
        this.volume = 20;
        this.isPowerOn = false;
    }

    @Override
    public void powerOn() {
        this.isPowerOn = true;
        System.out.println("    📺 [" + brand + "] 电视已开启");
    }

    @Override
    public void powerOff() {
        this.isPowerOn = false;
        System.out.println("    📺 [" + brand + "] 电视已关闭");
    }

    @Override
    public int volumeUp() {
        if (volume < 100) {
            volume += 5;
        }
        return volume;
    }

    @Override
    public int volumeDown() {
        if (volume > 0) {
            volume -= 5;
        }
        return volume;
    }

    /**
     * 切换频道（电视特有功能）。
     *
     * @param channel 目标频道号
     */
    public void setChannel(int channel) {
        this.channel = channel;
    }

    @Override
    public String getStatus() {
        return String.format("电视[品牌=%s, 频道=%d, 音量=%d, 电源=%s]",
                brand, channel, volume, isPowerOn ? "开启" : "关闭");
    }

    @Override
    public String getDeviceType() {
        return "智能电视";
    }

    @Override
    public String toString() {
        return getStatus();
    }
}
