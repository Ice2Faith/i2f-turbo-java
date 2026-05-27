package i2f.design.pattern.creational.factoryMethod.logistics.impl;

import i2f.design.pattern.creational.factoryMethod.transport.Transport;
import i2f.design.pattern.creational.factoryMethod.transport.impl.Truck;
import i2f.design.pattern.creational.factoryMethod.logistics.Logistics;

/**
 * 工厂方法模式 —— 公路物流（Concrete Creator：RoadLogistics）
 *
 * <p><b>角色：</b>具体创建者（Concrete Creator）</p>
 *
 * <p><b>说明：</b>公路物流公司重写工厂方法 {@link #createTransport()}，
 * 返回卡车（{@link Truck}）作为运输工具。
 * 这体现了工厂方法模式的核心——<b>子类决定实例化哪一个产品类</b>。</p>
 *
 * <p><b>开闭原则体现：</b>如果未来需要新增"铁路物流"，
 * 只需新增 {@code RailLogistics} 子类并返回 {@code Train} 产品，
 * 无需修改现有任何代码。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see Logistics
 * @see Truck
 */
public class RoadLogistics extends Logistics {

    /**
     * 工厂方法实现：创建卡车。
     *
     * <p>公路物流公司选择卡车作为运输工具，
     * 配置标准公路运输参数（载重 20 吨、时速 80 km/h）。</p>
     *
     * @return 配置好的卡车实例
     */
    @Override
    public Transport createTransport() {
        return new Truck("重型卡车-A1", 20.0, 80.0, "京A·88888");
    }

    @Override
    public String getCompanyName() {
        return "顺丰公路物流";
    }
}
