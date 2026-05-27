package i2f.design.pattern.creational.factoryMethod.logistics.impl;

import i2f.design.pattern.creational.factoryMethod.transport.Transport;
import i2f.design.pattern.creational.factoryMethod.transport.impl.Ship;
import i2f.design.pattern.creational.factoryMethod.logistics.Logistics;

/**
 * 工厂方法模式 —— 海运物流（Concrete Creator：SeaLogistics）
 *
 * <p><b>角色：</b>具体创建者（Concrete Creator）</p>
 *
 * <p><b>说明：</b>海运物流公司重写工厂方法 {@link #createTransport()}，
 * 返回轮船（{@link Ship}）作为运输工具。
 * 这体现了工厂方法模式的核心——<b>子类决定实例化哪一个产品类</b>。</p>
 *
 * <p><b>开闭原则体现：</b>如果未来需要新增"航空物流"，
 * 只需新增 {@code AirLogistics} 子类并返回 {@code Airplane} 产品，
 * 无需修改现有任何代码。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see Logistics
 * @see Ship
 */
public class SeaLogistics extends Logistics {

    /**
     * 工厂方法实现：创建轮船。
     *
     * <p>海运物流公司选择轮船作为运输工具，
     * 配置标准远洋运输参数（载重 5000 吨、航速 30 km/h）。</p>
     *
     * @return 配置好的轮船实例
     */
    @Override
    public Transport createTransport() {
        return new Ship("远洋巨轮-东方号", 5000.0, 30.0, "COSCO-2026");
    }

    @Override
    public String getCompanyName() {
        return "中远海运物流";
    }
}
