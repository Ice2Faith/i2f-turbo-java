package i2f.design.pattern.behavioral.observer.observer.impl;

import i2f.design.pattern.behavioral.observer.observer.Observer;
import i2f.design.pattern.behavioral.observer.subject.Subject;
import i2f.design.pattern.behavioral.observer.subject.WeatherStation;
import lombok.Data;

/**
 * 观察者模式 —— 统计显示屏（Concrete Observer：StatisticsDisplay）
 *
 * <p><b>角色：</b>具体观察者（Concrete Observer）</p>
 *
 * <p><b>模式说明：</b>实现 Observer 接口，订阅气象站的数据变化。
 * 不仅显示当前数据，还统计并展示历史最高/最低温度等统计信息。</p>
 *
 * <p><b>命名立意：</b>统计显示屏是气象数据的高级消费者，它不仅关心当前数据，
 * 还维护历史记录并计算统计信息（如最高/最低温度），为数据分析提供支持。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see Observer
 * @see WeatherStation
 */
@Data
public class StatisticsDisplay implements Observer {

    /**
     * 当前温度（摄氏度）。
     */
    private double temperature;

    /**
     * 历史最高温度（摄氏度）。
     */
    private double maxTemperature = Double.MIN_VALUE;

    /**
     * 历史最低温度（摄氏度）。
     */
    private double minTemperature = Double.MAX_VALUE;

    /**
     * 数据更新次数。
     */
    private int updateCount = 0;

    @Override
    public void update(Subject subject) {
        if (subject instanceof WeatherStation) {
            WeatherStation weatherStation = (WeatherStation) subject;
            this.temperature = weatherStation.getTemperature();
            this.updateCount++;
            // 更新统计信息
            updateStatistics();
            display();
        }
    }

    /**
     * 更新统计信息。
     *
     * <p>每次收到新数据时，更新最高/最低温度记录。</p>
     */
    private void updateStatistics() {
        if (temperature > maxTemperature) {
            maxTemperature = temperature;
        }
        if (temperature < minTemperature) {
            minTemperature = temperature;
        }
    }

    /**
     * 在统计面板上显示气象数据和统计信息。
     *
     * <p>统计面板展示当前温度以及历史极值，帮助用户了解温度变化趋势。</p>
     */
    public void display() {
        System.out.println("    [📊 统计显示屏] 第 " + updateCount + " 次更新 → 当前: " + temperature + "°C | 最高: " + maxTemperature + "°C | 最低: " + minTemperature + "°C");
    }
}
