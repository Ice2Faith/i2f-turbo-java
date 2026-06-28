package i2f.design.pattern.behavioral.observer;

import i2f.design.pattern.behavioral.observer.observer.Observer;
import i2f.design.pattern.behavioral.observer.observer.impl.ComputerDisplay;
import i2f.design.pattern.behavioral.observer.observer.impl.PhoneDisplay;
import i2f.design.pattern.behavioral.observer.observer.impl.StatisticsDisplay;
import i2f.design.pattern.behavioral.observer.subject.WeatherStation;

/**
 * 观察者模式 —— 调用演示
 *
 * <p>演示观察者模式的核心机制：气象站（Subject）作为被观察者，维护多个显示设备（Observer）作为观察者。
 * 当气象站数据更新时，自动通知所有观察者进行刷新，实现一对多的依赖关系和发布-订阅机制。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 */
public class Test {
    public static void main(String[] args) {

        // ==================== 1. 观察者模式核心演示 ====================
        System.out.println("====== 观察者模式（Observer）演示 ======");
        System.out.println("场景：气象站（Subject）发布气象数据，多个显示设备（Observer）订阅并自动更新");
        System.out.println("      体现了发布-订阅机制和一对多的依赖关系\n");

        // ==================== 2. 创建气象站（被观察者） ====================
        System.out.println("────── 步骤 1：创建气象站 ──────");
        WeatherStation weatherStation = new WeatherStation(25.0, 60.0, 1013.0);
        System.out.println("  气象站已初始化，初始数据 → 温度: 25.0°C, 湿度: 60.0%, 气压: 1013.0 hPa\n");

        // ==================== 3. 创建显示设备（观察者） ====================
        System.out.println("────── 步骤 2：创建显示设备（观察者） ──────");
        PhoneDisplay phoneDisplay = new PhoneDisplay();
        ComputerDisplay computerDisplay = new ComputerDisplay();
        StatisticsDisplay statisticsDisplay = new StatisticsDisplay();
        System.out.println("  已创建 3 个显示设备：手机、电脑、统计面板\n");

        // ==================== 4. 注册观察者 ====================
        System.out.println("────── 步骤 3：订阅气象站数据 ──────");
        weatherStation.attach(phoneDisplay);
        weatherStation.attach(computerDisplay);
        weatherStation.attach(statisticsDisplay);
        System.out.println();

        // ==================== 5. 第一次数据更新 ====================
        System.out.println("────── 步骤 4：第一次数据更新（自动通知所有观察者） ──────");
        weatherStation.setMeasurements(28.5, 65.0, 1012.5);
        System.out.println();

        // ==================== 6. 第二次数据更新 ====================
        System.out.println("────── 步骤 5：第二次数据更新 ──────");
        weatherStation.setMeasurements(30.2, 70.0, 1011.0);
        System.out.println();

        // ==================== 7. 第三次数据更新 ====================
        System.out.println("────── 步骤 6：第三次数据更新 ──────");
        weatherStation.setMeasurements(22.8, 55.0, 1015.0);
        System.out.println();

        // ==================== 8. 移除观察者演示 ====================
        System.out.println("────── 步骤 7：取消订阅（移除观察者） ──────");
        System.out.println("  手机显示屏取消订阅...\n");
        weatherStation.detach(phoneDisplay);

        // ==================== 9. 移除后的数据更新 ====================
        System.out.println("────── 步骤 8：数据更新（仅通知剩余观察者） ──────");
        weatherStation.setMeasurements(26.0, 62.0, 1013.5);
        System.out.println();

        // ==================== 10. 面向抽象编程演示 ====================
        System.out.println("====== 面向抽象编程 —— 统一管理观察者 ======");
        System.out.println("通过统一接口（Observer）管理不同类型的显示设备：\n");

        WeatherStation newStation = new WeatherStation(20.0, 50.0, 1020.0);
        Observer[] displays = {new PhoneDisplay(), new ComputerDisplay(), new StatisticsDisplay()};

        System.out.println("批量注册观察者：");
        for (Observer display : displays) {
            newStation.attach(display);
        }
        System.out.println();

        System.out.println("数据更新后，所有观察者自动接收通知：");
        newStation.setMeasurements(23.5, 58.0, 1018.0);
        System.out.println();

        // ==================== 11. 动态添加观察者演示 ====================
        System.out.println("====== 动态扩展 —— 运行时新增观察者 ======");
        System.out.println("在系统运行过程中，可以随时添加新的观察者：\n");

        WeatherStation dynamicStation = new WeatherStation(25.0, 60.0, 1013.0);
        PhoneDisplay dynamicPhone = new PhoneDisplay();
        dynamicStation.attach(dynamicPhone);

        System.out.println("\n第一次更新：");
        dynamicStation.setMeasurements(27.0, 63.0, 1012.0);

        System.out.println("\n动态添加电脑显示屏...");
        ComputerDisplay dynamicComputer = new ComputerDisplay();
        dynamicStation.attach(dynamicComputer);

        System.out.println("\n第二次更新（两个观察者同时接收）：");
        dynamicStation.setMeasurements(29.0, 68.0, 1010.0);
        System.out.println();

        // ==================== 12. 模式优势总结 ====================
        System.out.println("====== 观察者模式优势总结 ======");
        System.out.println("1. 遵循开闭原则：新增观察者无需修改气象站代码，只需实现 Observer 接口");
        System.out.println("2. 松耦合：气象站无需知道观察者的具体类型，只依赖 Observer 抽象接口");
        System.out.println("3. 支持广播通信：一次数据更新，自动通知所有订阅者");
        System.out.println("4. 动态订阅：运行时可以随时添加或移除观察者");
        System.out.println("5. 符合依赖倒置原则：高层模块（气象站）和低层模块（显示设备）都依赖抽象（Observer）");
        System.out.println("\n====== 与 Spring 事件机制的对应关系 ======");
        System.out.println("Subject → ApplicationEventPublisher（事件发布器）");
        System.out.println("Observer → ApplicationListener（事件监听器）");
        System.out.println("notifyObservers() → publishEvent()（发布事件）");
        System.out.println("update(subject) → onApplicationEvent(event)（处理事件）");
    }
}
