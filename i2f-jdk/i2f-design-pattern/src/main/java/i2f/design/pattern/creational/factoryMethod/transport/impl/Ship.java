package i2f.design.pattern.creational.factoryMethod.transport.impl;

import i2f.design.pattern.creational.factoryMethod.transport.Transport;
import i2f.design.pattern.creational.factoryMethod.logistics.impl.SeaLogistics;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 工厂方法模式 —— 轮船（Concrete Product：Ship）
 *
 * <p><b>角色：</b>具体产品（Concrete Product）</p>
 *
 * <p><b>说明：</b>轮船是海运物流的运输工具，通过海洋航线完成跨国/跨洲货物配送。
 * 由 {@link SeaLogistics} 工厂方法创建，客户端无需直接 new 此类。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see Transport
 * @see SeaLogistics
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Ship extends Transport {

    /**
     * 船舶编号。
     */
    private String vesselCode;

    public Ship(String name, double capacity, double speed, String vesselCode) {
        super(name, capacity, speed);
        this.vesselCode = vesselCode;
    }

    @Override
    public String deliver(String destination) {
        return String.format("[轮船·%s] 通过海运运输 %.1f 吨货物，以 %.0f km/h 的航速驶向【%s】",
                getName(), getCapacity(), getSpeed(), destination);
    }

    @Override
    public String getType() {
        return "远洋轮船";
    }

    @Override
    public String toString() {
        return String.format("Ship{name='%s', capacity=%.1f吨, speed=%.0fkm/h, vesselCode='%s'}",
                getName(), getCapacity(), getSpeed(), vesselCode);
    }
}
