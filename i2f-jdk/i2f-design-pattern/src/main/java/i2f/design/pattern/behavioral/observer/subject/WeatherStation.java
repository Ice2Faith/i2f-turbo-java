package i2f.design.pattern.behavioral.observer.subject;

import i2f.design.pattern.behavioral.observer.observer.Observer;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 观察者模式 —— 气象站（Concrete Subject：WeatherStation）
 *
 * <p><b>角色：</b>具体目标（Concrete Subject）</p>
 *
 * <p><b>模式说明：</b>实现 Subject 接口，维护观察者列表和气象数据状态。
 * 当气象数据（温度、湿度、气压）发生变化时，自动通知所有已注册的观察者。</p>
 *
 * <p><b>命名立意：</b>气象站是真实的气象数据采集设备，持续监测温度、湿度、气压等数据。
 * 多个显示设备（观察者）订阅气象站的数据，当数据更新时，气象站主动推送给所有订阅者，
 * 体现了"一对多"的依赖关系和"发布-订阅"机制。</p>
 *
 * <p><b>与 Spring 的对比：</b></p>
 * <pre>
 *  本示例                         Spring 事件机制
 *  ──────────────────────────    ──────────────────────────────────
 *  Subject                       ApplicationEventPublisher
 *  Observer                      ApplicationListener
 *  WeatherStation                事件发布者（如 Service 层）
 *  notifyObservers()             publishEvent(new ApplicationEvent())
 *  update(subject)               onApplicationEvent(event)
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see Subject
 * @see Observer
 */
@Data
public class WeatherStation implements Subject {

    /**
     * 观察者列表。
     */
    private final List<Observer> observers = new ArrayList<>();

    /**
     * 温度（摄氏度）。
     */
    private double temperature;

    /**
     * 湿度（百分比）。
     */
    private double humidity;

    /**
     * 气压（百帕）。
     */
    private double pressure;

    public WeatherStation() {
    }

    public WeatherStation(double temperature, double humidity, double pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
    }

    @Override
    public void attach(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("  [气象站] 新观察者已订阅：" + observer.getClass().getSimpleName());
        }
    }

    @Override
    public void detach(Observer observer) {
        if (observers.remove(observer)) {
            System.out.println("  [气象站] 观察者已取消订阅：" + observer.getClass().getSimpleName());
        }
    }

    @Override
    public void notifyObservers() {
        System.out.println("  [气象站] 数据已更新，正在通知 " + observers.size() + " 个观察者...");
        for (Observer observer : observers) {
            observer.update(this);
        }
    }

    /**
     * 更新气象数据并自动通知所有观察者。
     *
     * <p>这是气象站的核心业务方法：设置新的气象数据后，
     * 立即调用 notifyObservers() 推送给所有订阅者。</p>
     *
     * @param temperature 温度（摄氏度）
     * @param humidity    湿度（百分比）
     * @param pressure    气压（百帕）
     */
    public void setMeasurements(double temperature, double humidity, double pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        System.out.println("  [气象站] 气象数据已更新 → 温度: " + temperature + "°C, 湿度: " + humidity + "%, 气压: " + pressure + " hPa");
        // 数据更新后，自动通知所有观察者
        notifyObservers();
    }
}
