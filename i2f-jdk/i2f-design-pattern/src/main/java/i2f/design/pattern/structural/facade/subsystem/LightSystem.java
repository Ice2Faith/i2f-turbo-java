package i2f.design.pattern.structural.facade.subsystem;

import lombok.Data;

/**
 * 外观模式 —— 灯光子系统（Light System）
 *
 * <p><b>角色：</b>子系统类（Subsystem Class）</p>
 *
 * <p><b>模式说明：</b>灯光系统是智能家居的一个复杂子系统，包含多个操作：
 * 开灯、关灯、调节亮度、设置颜色等。客户端如果直接操作这些细节会非常繁琐。
 * 通过外观类（{@link i2f.design.pattern.structural.facade.SmartHomeFacade}）可以一键控制。</p>
 *
 * <p><b>命名立意：</b>"灯光系统"是家庭中常见的智能设备子系统，
 * 包含多种复杂的控制功能，但在日常生活中用户只需要简单的"开启/关闭"操作。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 */
@Data
public class LightSystem {

    /**
     * 灯光是否开启
     */
    private boolean isOn = false;

    /**
     * 亮度级别（0-100）
     */
    private int brightness = 50;

    /**
     * 灯光颜色
     */
    private String color = "白色";

    /**
     * 开启灯光
     */
    public void turnOn() {
        this.isOn = true;
        System.out.println("    [灯光] 灯光已开启 | 亮度: " + brightness + "% | 颜色: " + color);
    }

    /**
     * 关闭灯光
     */
    public void turnOff() {
        this.isOn = false;
        System.out.println("    [灯光] 灯光已关闭");
    }

    /**
     * 调节亮度
     *
     * @param level 亮度级别（0-100）
     */
    public void setBrightness(int level) {
        this.brightness = Math.max(0, Math.min(100, level));
        System.out.println("    [灯光] 亮度已调节至: " + this.brightness + "%");
    }

    /**
     * 设置灯光颜色
     *
     * @param color 颜色名称
     */
    public void setColor(String color) {
        this.color = color;
        System.out.println("    [灯光] 颜色已设置为: " + this.color);
    }

    /**
     * 阅读模式：高亮度、冷白色
     */
    public void setReadingMode() {
        this.brightness = 90;
        this.color = "冷白色";
        System.out.println("    [灯光] 已切换至阅读模式（亮度90%，冷白色）");
    }

    /**
     * 影院模式：低亮度、暖色调
     */
    public void setCinemaMode() {
        this.brightness = 20;
        this.color = "暖黄色";
        System.out.println("    [灯光] 已切换至影院模式（亮度20%，暖黄色）");
    }
}
