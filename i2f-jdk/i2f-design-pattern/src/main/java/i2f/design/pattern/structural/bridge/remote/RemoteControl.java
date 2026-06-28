package i2f.design.pattern.structural.bridge.remote;

import i2f.design.pattern.structural.bridge.device.Device;
import i2f.design.pattern.structural.bridge.device.impl.TV;
import i2f.design.pattern.structural.bridge.device.impl.Radio;
import i2f.design.pattern.structural.bridge.remote.impl.BasicRemote;
import i2f.design.pattern.structural.bridge.remote.impl.AdvancedRemote;

/**
 * 桥接模式 —— 遥控器（Abstraction：RemoteControl）
 *
 * <p><b>角色：</b>抽象部分（Abstraction）</p>
 *
 * <p><b>模式说明：</b>定义遥控器的控制逻辑，通过组合 {@link Device} 接口
 * 来操作设备，而非继承设备类。这是桥接模式的核心——<b>"抽象与实现分离"</b>。
 * 遥控器负责控制流程（开关、音量调节），设备负责具体执行，
 * 两个维度可以独立扩展：新增遥控器类型（如语音遥控）或新增设备类型（如空调），
 * 互不影响，避免类爆炸问题。</p>
 *
 * <p><b>命名立意：</b>以"智能家居控制"为场景——遥控器（抽象）是控制入口，
 * 设备（实现）是被控对象。基础遥控器提供基本功能，高级遥控器扩展更多功能。
 * 无论控制电视还是收音机，遥控器接口保持一致，
 * 体现"桥接"二字——在抽象与实现之间架起一座桥。</p>
 *
 * <p><b>模式结构：</b></p>
 * <pre>
 *  Abstraction（抽象部分）      Implementor（实现部分）
 *  ────────────────────────   ────────────────────────
 *  RemoteControl               Device
 *    └─ device: Device          ├─ powerOn()/powerOff()
 *    └─ togglePower()           ├─ volumeUp()/volumeDown()
 *    └─ volumeUp()              └─ getStatus()
 *    └─ volumeDown()
 *
 *  RefinedAbstraction（扩展抽象）  ConcreteImplementor（具体实现）
 *  ────────────────────────────   ─────────────────────────────
 *  BasicRemote（基础遥控）         ├─ TV（电视）
 *  AdvancedRemote（高级遥控）       └─ Radio（收音机）
 * </pre>
 *
 * <p><b>与继承的对比：</b></p>
 * <pre>
 *  继承方案（类爆炸）                        桥接方案（组合）
 *  ──────────────────────────────────────   ─────────────────────────────────────
 *  TVBasicRemote                            RemoteControl + Device（组合）
 *  TVAdvancedRemote                           ├─ BasicRemote 控制 TV
 *  RadioBasicRemote                           ├─ BasicRemote 控制 Radio
 *  RadioAdvancedRemote                        ├─ AdvancedRemote 控制 TV
 *  ...（每增加一个遥控器或设备，类数量相乘）     └─ AdvancedRemote 控制 Radio
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see Device
 * @see BasicRemote
 * @see AdvancedRemote
 */
public abstract class RemoteControl {

    /**
     * 被控制的设备（桥接模式的关键：组合而非继承）。
     *
     * <p>通过持有 {@link Device} 接口的引用，遥控器可以在运行时
     * 动态切换控制的设备（电视、收音机等），实现抽象与实现的解耦。</p>
     */
    protected Device device;

    public RemoteControl(Device device) {
        this.device = device;
    }

    /**
     * 切换设备电源状态（开/关）。
     */
    public void togglePower() {
        System.out.println("  ┌─ 遥控器操作：切换电源");
        if (isDevicePoweredOn()) {
            System.out.println("  │ 当前状态：设备已开启 → 执行关闭");
            device.powerOff();
        } else {
            System.out.println("  │ 当前状态：设备已关闭 → 执行开启");
            device.powerOn();
        }
        System.out.println("  └─ 操作完成\n");
    }

    /**
     * 增加音量。
     */
    public void volumeUp() {
        System.out.println("  ┌─ 遥控器操作：增加音量");
        int newVolume = device.volumeUp();
        System.out.println("  │ 设备类型：" + device.getDeviceType());
        System.out.println("  │ 音量调节：→ " + newVolume);
        System.out.println("  └─ 操作完成\n");
    }

    /**
     * 降低音量。
     */
    public void volumeDown() {
        System.out.println("  ┌─ 遥控器操作：降低音量");
        int newVolume = device.volumeDown();
        System.out.println("  │ 设备类型：" + device.getDeviceType());
        System.out.println("  │ 音量调节：→ " + newVolume);
        System.out.println("  └─ 操作完成\n");
    }

    /**
     * 检查设备是否已开启。
     *
     * @return true 如果设备电源已开启
     */
    public boolean isDevicePoweredOn() {
        return device.getStatus().contains("电源=开启");
    }

    /**
     * 获取当前控制的设备。
     *
     * @return 设备实例
     */
    public Device getDevice() {
        return device;
    }

    /**
     * 切换控制的设备（桥接模式的优势：运行时动态切换实现）。
     *
     * @param newDevice 新的设备实例
     */
    public void setDevice(Device newDevice) {
        System.out.println("  [桥接切换] 遥控器从 [" + device.getDeviceType() + "] 切换到 [" + newDevice.getDeviceType() + "]");
        this.device = newDevice;
    }

    /**
     * 获取遥控器类型描述。
     *
     * @return 遥控器类型
     */
    public abstract String getRemoteType();
}
