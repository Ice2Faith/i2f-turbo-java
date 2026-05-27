package i2f.design.pattern.structural.facade.subsystem;

import lombok.Data;

/**
 * 外观模式 —— 窗帘子系统（Curtain System）
 *
 * <p><b>角色：</b>子系统类（Subsystem Class）</p>
 *
 * <p><b>模式说明：</b>窗帘子系统包含开合控制、角度调节等功能。
 * 通过外观类可以与其他设备联动，实现场景化控制。</p>
 *
 * <p><b>命名立意：</b>"窗帘系统"是智能家居的辅助设备，
 * 通常与灯光、空调等配合使用，实现更智能的家居体验。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 */
@Data
public class CurtainSystem {

    /**
     * 窗帘是否开启
     */
    private boolean isOpen = false;

    /**
     * 开启角度（0-100，0表示完全关闭，100表示完全打开）
     */
    private int openAngle = 0;

    /**
     * 打开窗帘
     */
    public void open() {
        this.isOpen = true;
        this.openAngle = 100;
        System.out.println("    [窗帘] 窗帘已完全打开");
    }

    /**
     * 关闭窗帘
     */
    public void close() {
        this.isOpen = false;
        this.openAngle = 0;
        System.out.println("    [窗帘] 窗帘已完全关闭");
    }

    /**
     * 设置开启角度
     *
     * @param angle 角度（0-100）
     */
    public void setAngle(int angle) {
        this.openAngle = Math.max(0, Math.min(100, angle));
        this.isOpen = this.openAngle > 0;
        System.out.println("    [窗帘] 窗帘角度已设置为: " + this.openAngle + "%");
    }

    /**
     * 半开模式：50%开启
     */
    public void setHalfOpen() {
        this.openAngle = 50;
        this.isOpen = true;
        System.out.println("    [窗帘] 窗帘已半开（50%）");
    }

    /**
     * 晨起模式：缓缓打开
     */
    public void setMorningMode() {
        System.out.println("    [窗帘] 晨起模式：窗帘正在缓缓打开...");
        this.openAngle = 80;
        this.isOpen = true;
        System.out.println("    [窗帘] 窗帘已打开至80%");
    }
}
