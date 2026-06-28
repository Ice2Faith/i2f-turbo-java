package i2f.design.pattern.structural.bridge.device.impl;

import i2f.design.pattern.structural.bridge.device.Device;
import i2f.design.pattern.structural.bridge.remote.RemoteControl;
import lombok.Data;

/**
 * 桥接模式 —— 收音机（Concrete Implementor：Radio）
 *
 * <p><b>角色：</b>具体实现（Concrete Implementor）</p>
 *
 * <p><b>说明：</b>收音机设备实现 {@link Device} 接口，提供收音机特有的操作：
 * 频率调谐、音量调节等。遥控器通过 {@link Device} 接口控制收音机，
 * 无需关心收音机内部实现细节，体现桥接模式的"实现分离"思想。</p>
 *
 * <p><b>独立变化体现：</b>收音机可以独立演进（如增加FM/AM切换、预设电台），
 * 不会影响遥控器层的代码；反之，遥控器增加新功能也不会影响收音机实现。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see Device
 * @see RemoteControl
 */
@Data
public class Radio implements Device {

    /**
     * 收音机品牌型号。
     */
    private String brand;

    /**
     * 当前频率（MHz）。
     */
    private double frequency;

    /**
     * 当前音量（0-100）。
     */
    private int volume;

    /**
     * 电源状态。
     */
    private boolean isPowerOn;

    public Radio(String brand) {
        this.brand = brand;
        this.frequency = 88.0;
        this.volume = 15;
        this.isPowerOn = false;
    }

    @Override
    public void powerOn() {
        this.isPowerOn = true;
        System.out.println("    📻 [" + brand + "] 收音机已开启");
    }

    @Override
    public void powerOff() {
        this.isPowerOn = false;
        System.out.println("    📻 [" + brand + "] 收音机已关闭");
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
     * 调谐频率（收音机特有功能）。
     *
     * @param frequency 目标频率（MHz）
     */
    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    @Override
    public String getStatus() {
        return String.format("收音机[品牌=%s, 频率=%.1fMHz, 音量=%d, 电源=%s]",
                brand, frequency, volume, isPowerOn ? "开启" : "关闭");
    }

    @Override
    public String getDeviceType() {
        return "数字收音机";
    }

    @Override
    public String toString() {
        return getStatus();
    }
}
