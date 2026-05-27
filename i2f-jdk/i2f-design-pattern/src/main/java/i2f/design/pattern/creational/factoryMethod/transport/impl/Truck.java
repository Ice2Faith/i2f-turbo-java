package i2f.design.pattern.creational.factoryMethod.transport.impl;

import i2f.design.pattern.creational.factoryMethod.transport.Transport;
import i2f.design.pattern.creational.factoryMethod.logistics.impl.RoadLogistics;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 工厂方法模式 —— 卡车（Concrete Product：Truck）
 *
 * <p><b>角色：</b>具体产品（Concrete Product）</p>
 *
 * <p><b>说明：</b>卡车是公路物流的运输工具，通过陆路完成货物配送。
 * 由 {@link RoadLogistics} 工厂方法创建，客户端无需直接 new 此类。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see Transport
 * @see RoadLogistics
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Truck extends Transport {

    /**
     * 车牌号。
     */
    private String plateNumber;

    public Truck(String name, double capacity, double speed, String plateNumber) {
        super(name, capacity, speed);
        this.plateNumber = plateNumber;
    }

    @Override
    public String deliver(String destination) {
        return String.format("[卡车·%s] 通过公路运输 %.1f 吨货物，以 %.0f km/h 的速度驶向【%s】",
                getName(), getCapacity(), getSpeed(), destination);
    }

    @Override
    public String getType() {
        return "公路卡车";
    }

    @Override
    public String toString() {
        return String.format("Truck{name='%s', capacity=%.1f吨, speed=%.0fkm/h, plateNumber='%s'}",
                getName(), getCapacity(), getSpeed(), plateNumber);
    }
}
