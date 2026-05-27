package i2f.design.pattern.structural.bridge.device;

import i2f.design.pattern.structural.bridge.device.impl.TV;
import i2f.design.pattern.structural.bridge.device.impl.Radio;
import i2f.design.pattern.structural.bridge.remote.RemoteControl;

/**
 * 桥接模式 —— 设备接口（Implementor：Device）
 *
 * <p><b>角色：</b>实现部分接口（Implementor Interface）</p>
 *
 * <p><b>模式说明：</b>定义设备的基本操作接口，与遥控器抽象层分离。
 * 这是桥接模式的"实现部分"维度——不同的设备（电视、收音机等）
 * 实现此接口，提供各自的具体操作方式。
 * 遥控器（抽象部分）通过组合此接口来调用设备功能，
 * 二者可以独立变化，互不影响。</p>
 *
 * <p><b>命名立意：</b>以"智能家居控制"为场景——遥控器（抽象）负责控制逻辑，
 * 设备（实现）负责具体执行。新增设备类型（如空调、音响）无需修改遥控器代码，
 * 新增遥控器功能（如语音控制）也无需修改设备代码，
 * 完美体现桥接模式"抽象与实现分离"的核心思想。</p>
 *
 * <p><b>类层次结构：</b></p>
 * <pre>
 *  Implementor（实现接口）    ConcreteImplementor（具体实现）
 *  ──────────────────────   ─────────────────────────────────────
 *  Device                   TV（电视 —— 频道切换、音量调节）
 *                           Radio（收音机 —— 频率调谐、音量调节）
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see TV
 * @see Radio
 * @see RemoteControl
 */
public interface Device {

    /**
     * 打开设备电源。
     */
    void powerOn();

    /**
     * 关闭设备电源。
     */
    void powerOff();

    /**
     * 增加音量。
     *
     * @return 当前音量值
     */
    int volumeUp();

    /**
     * 降低音量。
     *
     * @return 当前音量值
     */
    int volumeDown();

    /**
     * 获取设备当前状态描述。
     *
     * @return 状态信息
     */
    String getStatus();

    /**
     * 获取设备类型名称。
     *
     * @return 设备类型
     */
    String getDeviceType();
}
