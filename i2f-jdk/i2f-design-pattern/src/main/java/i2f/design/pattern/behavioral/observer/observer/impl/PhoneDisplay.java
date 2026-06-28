package i2f.design.pattern.behavioral.observer.observer.impl;

import i2f.design.pattern.behavioral.observer.observer.Observer;
import i2f.design.pattern.behavioral.observer.subject.Subject;
import i2f.design.pattern.behavioral.observer.subject.WeatherStation;
import lombok.Data;

/**
 * 观察者模式 —— 手机显示屏（Concrete Observer：PhoneDisplay）
 *
 * <p><b>角色：</b>具体观察者（Concrete Observer）</p>
 *
 * <p><b>模式说明：</b>实现 Observer 接口，订阅气象站的数据变化。
 * 当收到更新通知时，从气象站获取最新数据并以适合手机屏幕的简洁格式显示。</p>
 *
 * <p><b>命名立意：</b>手机显示屏是气象数据的一个消费者，它关心温度和湿度，
 * 以简洁的方式展示给用户。当气象站数据更新时，手机屏幕自动刷新显示。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see Observer
 * @see WeatherStation
 */
@Data
public class PhoneDisplay implements Observer {

    /**
     * 当前温度（摄氏度）。
     */
    private double temperature;

    /**
     * 当前湿度（百分比）。
     */
    private double humidity;

    @Override
    public void update(Subject subject) {
        if (subject instanceof WeatherStation) {
            WeatherStation weatherStation = (WeatherStation) subject;
            this.temperature = weatherStation.getTemperature();
            this.humidity = weatherStation.getHumidity();
            display();
        }
    }

    /**
     * 在手机屏幕上显示气象数据。
     *
     * <p>手机屏幕空间有限，只显示最核心的温度和湿度信息。</p>
     */
    public void display() {
        System.out.println("    [📱 手机显示屏] 当前天气：温度 " + temperature + "°C | 湿度 " + humidity + "%");
    }
}
