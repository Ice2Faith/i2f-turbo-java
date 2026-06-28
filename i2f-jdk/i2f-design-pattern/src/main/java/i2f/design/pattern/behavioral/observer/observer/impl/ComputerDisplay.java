package i2f.design.pattern.behavioral.observer.observer.impl;

import i2f.design.pattern.behavioral.observer.observer.Observer;
import i2f.design.pattern.behavioral.observer.subject.Subject;
import i2f.design.pattern.behavioral.observer.subject.WeatherStation;
import lombok.Data;

/**
 * 观察者模式 —— 电脑显示屏（Concrete Observer：ComputerDisplay）
 *
 * <p><b>角色：</b>具体观察者（Concrete Observer）</p>
 *
 * <p><b>模式说明：</b>实现 Observer 接口，订阅气象站的数据变化。
 * 当收到更新通知时，从气象站获取最新数据并以详细的格式显示在电脑屏幕上。</p>
 *
 * <p><b>命名立意：</b>电脑显示屏是气象数据的另一个消费者，相比手机屏幕，
 * 电脑屏幕空间更大，可以展示更详细的气象信息，包括温度、湿度和气压。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see Observer
 * @see WeatherStation
 */
@Data
public class ComputerDisplay implements Observer {

    /**
     * 当前温度（摄氏度）。
     */
    private double temperature;

    /**
     * 当前湿度（百分比）。
     */
    private double humidity;

    /**
     * 当前气压（百帕）。
     */
    private double pressure;

    @Override
    public void update(Subject subject) {
        if (subject instanceof WeatherStation) {
            WeatherStation weatherStation = (WeatherStation) subject;
            this.temperature = weatherStation.getTemperature();
            this.humidity = weatherStation.getHumidity();
            this.pressure = weatherStation.getPressure();
            display();
        }
    }

    /**
     * 在电脑屏幕上显示气象数据。
     *
     * <p>电脑屏幕空间较大，可以展示完整的气象信息，包括温度、湿度和气压。</p>
     */
    public void display() {
        System.out.println("    [💻 电脑显示屏] 详细气象数据 → 温度: " + temperature + "°C | 湿度: " + humidity + "% | 气压: " + pressure + " hPa");
    }
}
