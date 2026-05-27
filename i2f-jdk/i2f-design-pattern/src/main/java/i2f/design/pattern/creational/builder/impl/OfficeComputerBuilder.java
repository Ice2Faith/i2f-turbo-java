package i2f.design.pattern.creational.builder.impl;

import i2f.design.pattern.creational.builder.Builder;
import i2f.design.pattern.creational.builder.Computer;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 建造者模式 —— 办公电脑建造者（ConcreteBuilder：OfficeComputerBuilder）
 *
 * <p><b>角色：</b>具体建造者（ConcreteBuilder）</p>
 *
 * <p><b>说明：</b>实现 {@link Builder} 接口，专注于构建办公用途的电脑配置。
 * 办公电脑注重性价比与稳定性，配置偏向实用：
 * 中低端 CPU、适量内存、机械硬盘 + 小容量 SSD、集成显卡、普通显示器。</p>
 *
 * <p><b>命名立意：</b>以"电脑组装"为场景——
 * 办公电脑建造者按照标准组装流程，逐步安装适合办公的配件，
 * 最终产出一台满足日常办公需求的电脑。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see Builder
 * @see Computer
 * @see GamingComputerBuilder
 * @see i2f.design.pattern.creational.builder.Director
 */
@Data
@EqualsAndHashCode
public class OfficeComputerBuilder implements Builder {

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
