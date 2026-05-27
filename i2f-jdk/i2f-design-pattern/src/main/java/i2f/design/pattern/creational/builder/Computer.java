package i2f.design.pattern.creational.builder;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 建造者模式 —— 电脑（Product：Computer）
 *
 * <p><b>角色：</b>产品（Product）</p>
 *
 * <p><b>模式说明：</b>被建造的复杂对象。电脑由多个部件组成（CPU、内存、硬盘、显卡等），
 * 不同配置的电脑构建过程相同，但部件规格不同，最终呈现出不同的产品表示。</p>
 *
 * <p><b>命名立意：</b>以"电脑组装"为场景——
 * 建造者模式的核心是<b>分离构建过程与表示</b>，
 * 同样的组装流程（安装CPU → 安装内存 → 安装硬盘 → 安装显卡）可以产出不同配置的电脑，
 * 如办公电脑（低配）与游戏电脑（高配）。</p>
 *
 * <p><b>类层次结构：</b></p>
 * <pre>
 *  Product（产品）
 *  ─────────────────
 *  Computer           ← 被建造的复杂对象，包含多个可选部件
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see i2f.design.pattern.creational.builder.Builder
 * @see i2f.design.pattern.creational.builder.impl.OfficeComputerBuilder
 * @see i2f.design.pattern.creational.builder.impl.GamingComputerBuilder
 * @see i2f.design.pattern.creational.builder.Director
 */
@Data
@NoArgsConstructor
public class Computer {

    /**
     * CPU 型号。
     */
    private String cpu;

    /**
     * 内存大小（GB）。
     */
    private int memory;

    /**
     * 硬盘类型与容量。
     */
    private String storage;

    /**
     * 显卡型号。
     */
    private String gpu;

    /**
     * 显示器尺寸（英寸）。
     */
    private double monitorSize;

    /**
     * 是否包含固态硬盘。
     */
    private boolean hasSSD;

    /**
     * 获取电脑配置描述。
     *
     * @return 配置详情字符串
     */
    public String getConfig() {
        return String.format("CPU: %s | 内存: %dGB | 硬盘: %s | 显卡: %s | 显示器: %.1f寸 | SSD: %s",
                cpu, memory, storage, gpu, monitorSize, hasSSD ? "是" : "否");
    }

    @Override
    public String toString() {
        return String.format("Computer{cpu='%s', memory=%dGB, storage='%s', gpu='%s', monitorSize=%.1f, hasSSD=%b}",
                cpu, memory, storage, gpu, monitorSize, hasSSD);
    }
}
