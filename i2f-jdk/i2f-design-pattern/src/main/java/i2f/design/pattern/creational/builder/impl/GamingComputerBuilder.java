package i2f.design.pattern.creational.builder.impl;

import i2f.design.pattern.creational.builder.Builder;
import i2f.design.pattern.creational.builder.Computer;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 建造者模式 —— 游戏电脑建造者（ConcreteBuilder：GamingComputerBuilder）
 *
 * <p><b>角色：</b>具体建造者（ConcreteBuilder）</p>
 *
 * <p><b>说明：</b>实现 {@link Builder} 接口，专注于构建高性能游戏电脑配置。
 * 游戏电脑注重性能与体验，配置偏向高端：
 * 高端 CPU、大容量内存、大容量 SSD、独立高性能显卡、大尺寸高刷新率显示器。</p>
 *
 * <p><b>命名立意：</b>以"电脑组装"为场景——
 * 游戏电脑建造者按照标准组装流程，逐步安装适合游戏的配件，
 * 最终产出一台满足3A大作需求的高性能游戏电脑。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see Builder
 * @see Computer
 * @see OfficeComputerBuilder
 * @see i2f.design.pattern.creational.builder.Director
 */
@Data
@EqualsAndHashCode
public class GamingComputerBuilder implements Builder {

    /**
     * 正在构建的电脑实例。
     */
    private Computer computer = new Computer();

    @Override
    public Builder buildCPU(String cpu) {
        computer.setCpu(cpu);
        return this;
    }

    @Override
    public Builder buildMemory(int memory) {
        computer.setMemory(memory);
        return this;
    }

    @Override
    public Builder buildStorage(String storage) {
        computer.setStorage(storage);
        return this;
    }

    @Override
    public Builder buildGPU(String gpu) {
        computer.setGpu(gpu);
        return this;
    }

    @Override
    public Builder buildMonitor(double monitorSize) {
        computer.setMonitorSize(monitorSize);
        return this;
    }

    @Override
    public Builder buildSSD(boolean hasSSD) {
        computer.setHasSSD(hasSSD);
        return this;
    }

    @Override
    public Computer getComputer() {
        return computer;
    }
}
