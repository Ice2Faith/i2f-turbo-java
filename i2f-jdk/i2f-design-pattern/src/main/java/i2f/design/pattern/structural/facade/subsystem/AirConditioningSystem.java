package i2f.design.pattern.structural.facade.subsystem;

import lombok.Data;

/**
 * 外观模式 —— 空调子系统（Air Conditioning System）
 *
 * <p><b>角色：</b>子系统类（Subsystem Class）</p>
 *
 * <p><b>模式说明：</b>空调子系统包含温度调节、模式切换、风速控制等复杂功能。
 * 通过外观类可以提供"舒适模式"、"节能模式"等一键操作。</p>
 *
 * <p><b>命名立意：</b>"空调系统"是另一个常见的智能家居子系统，
 * 用户日常只需要简单的制冷/制热，但系统内部包含复杂的控制逻辑。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 */
@Data
public class AirConditioningSystem {

    /**
     * 空调是否开启
     */
    private boolean isOn = false;

    /**
     * 设定温度（摄氏度）
     */
    private int temperature = 24;

    /**
     * 运行模式
     */
    private String mode = "制冷";

    /**
     * 风速
     */
    private String windSpeed = "自动";

    /**
     * 开启空调
     */
    public void turnOn() {
        this.isOn = true;
        System.out.println("    [空调] 空调已开启 | 模式: " + mode + " | 温度: " + temperature + "°C | 风速: " + windSpeed);
    }

    /**
     * 关闭空调
     */
    public void turnOff() {
        this.isOn = false;
        System.out.println("    [空调] 空调已关闭");
    }

    /**
     * 设置温度
     *
     * @param temperature 温度值（摄氏度）
     */
    public void setTemperature(int temperature) {
        this.temperature = Math.max(16, Math.min(30, temperature));
        System.out.println("    [空调] 温度已设置为: " + this.temperature + "°C");
    }

    /**
     * 设置运行模式
     *
     * @param mode 模式名称（制冷/制热/除湿/送风）
     */
    public void setMode(String mode) {
        this.mode = mode;
        System.out.println("    [空调] 模式已切换至: " + this.mode);
    }

    /**
     * 设置风速
     *
     * @param windSpeed 风速（低/中/高/自动）
     */
    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
        System.out.println("    [空调] 风速已设置为: " + this.windSpeed);
    }

    /**
     * 舒适模式：24°C、制冷、自动风速
     */
    public void setComfortMode() {
        this.temperature = 24;
        this.mode = "制冷";
        this.windSpeed = "自动";
        System.out.println("    [空调] 已切换至舒适模式（24°C、制冷、自动风速）");
    }

    /**
     * 节能模式：26°C、制冷、低风速
     */
    public void setEcoMode() {
        this.temperature = 26;
        this.mode = "制冷";
        this.windSpeed = "低";
        System.out.println("    [空调] 已切换至节能模式（26°C、制冷、低风速）");
    }
}
