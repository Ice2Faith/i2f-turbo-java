package i2f.design.pattern.structural.facade;

import i2f.design.pattern.structural.facade.subsystem.AirConditioningSystem;
import i2f.design.pattern.structural.facade.subsystem.AudioSystem;
import i2f.design.pattern.structural.facade.subsystem.CurtainSystem;
import i2f.design.pattern.structural.facade.subsystem.LightSystem;
import lombok.Data;

/**
 * 外观模式 —— 智能家居外观（Smart Home Facade）
 *
 * <p><b>角色：</b>外观类（Facade）</p>
 *
 * <p><b>模式说明：</b>这是外观模式的核心类。它为复杂的智能家居子系统
 * （灯光、空调、音响、窗帘）提供一个统一的高层接口。
 * 客户端无需了解各个子系统的复杂操作，只需通过外观类提供的一键场景方法即可。</p>
 *
 * <p><b>命名立意：</b>"智能家居外观"充当家庭设备控制的统一入口——
 * 用户不需要分别操作灯光、空调、音响、窗帘的每一个细节，
 * 而是通过"回家模式"、"影院模式"、"睡眠模式"等场景化操作一键控制所有设备。</p>
 *
 * <p><b>与子系统的关系：</b></p>
 * <pre>
 *  Facade（外观类）
 *    └─ SmartHomeFacade
 *         ├─ LightSystem        （灯光子系统）
 *         ├─ AirConditioningSystem  （空调子系统）
 *         ├─ AudioSystem        （音响子系统）
 *         └─ CurtainSystem      （窗帘子系统）
 *
 *  外观类提供的方法：
 *    ├─ goHomeMode()           → 回家模式（开灯+开空调+拉开窗帘）
 *    ├─ leaveHomeMode()        → 离家模式（关闭所有设备）
 *    ├─ cinemaMode()           → 影院模式（暗光+空调+音响+关窗帘）
 *    ├─ sleepMode()            → 睡眠模式（关闭大部分设备，仅保留空调）
 *    └─ wakeUpMode()           → 起床模式（开窗帘+开灯+关空调）
 * </pre>
 *
 * <p><b>模式优势：</b></p>
 * <ul>
 *   <li>降低客户端与子系统之间的耦合度</li>
 *   <li>提供简单易用的高层接口，隐藏子系统复杂性</li>
 *   <li>不影响子系统的独立使用（客户端仍可直接访问子系统）</li>
 *   <li>便于维护：子系统变化不影响客户端，只需修改外观类</li>
 * </ul>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see LightSystem
 * @see AirConditioningSystem
 * @see AudioSystem
 * @see CurtainSystem
 */
@Data
public class SmartHomeFacade {

    /**
     * 灯光子系统
     */
    private final LightSystem lightSystem;

    /**
     * 空调子系统
     */
    private final AirConditioningSystem airConditioningSystem;

    /**
     * 音响子系统
     */
    private final AudioSystem audioSystem;

    /**
     * 窗帘子系统
     */
    private final CurtainSystem curtainSystem;

    /**
     * 构造外观类，初始化所有子系统。
     *
     * <p>外观类负责组合和管理所有子系统实例，
     * 客户端只需与外观类交互，无需关心子系统的创建和初始化。</p>
     */
    public SmartHomeFacade() {
        this.lightSystem = new LightSystem();
        this.airConditioningSystem = new AirConditioningSystem();
        this.audioSystem = new AudioSystem();
        this.curtainSystem = new CurtainSystem();
    }

    /**
     * 回家模式 —— 一键开启家庭设备。
     *
     * <p>典型操作序列：</p>
     * <ol>
     *   <li>打开灯光（默认亮度）</li>
     *   <li>打开空调（舒适模式）</li>
     *   <li>打开窗帘</li>
     *   <li>打开音响（轻音乐）</li>
     * </ol>
     */
    public void goHomeMode() {
        System.out.println("  ╔══════════════════════════════════════╗");
        System.out.println("  ║        🏠 回家模式 启动              ║");
        System.out.println("  ╚══════════════════════════════════════╝");

        lightSystem.turnOn();
        airConditioningSystem.setComfortMode();
        airConditioningSystem.turnOn();
        curtainSystem.open();
        audioSystem.setMusicMode();
        audioSystem.turnOn();
        audioSystem.play();

        System.out.println("  ✓ 回家模式完成，欢迎回家！\n");
    }

    /**
     * 离家模式 —— 一键关闭所有设备。
     *
     * <p>典型操作序列：</p>
     * <ol>
     *   <li>关闭灯光</li>
     *   <li>关闭空调</li>
     *   <li>关闭音响</li>
     *   <li>关闭窗帘</li>
     * </ol>
     */
    public void leaveHomeMode() {
        System.out.println("  ╔══════════════════════════════════════╗");
        System.out.println("  ║        🚪 离家模式 启动              ║");
        System.out.println("  ╚══════════════════════════════════════╝");

        lightSystem.turnOff();
        airConditioningSystem.turnOff();
        audioSystem.turnOff();
        curtainSystem.close();

        System.out.println("  ✓ 离家模式完成，所有设备已关闭，安心出门！\n");
    }

    /**
     * 影院模式 —— 打造沉浸式观影环境。
     *
     * <p>典型操作序列：</p>
     * <ol>
     *   <li>灯光调暗（影院模式）</li>
     *   <li>空调开启（舒适模式）</li>
     *   <li>音响切换至影院模式</li>
     *   <li>关闭窗帘（隔绝外部光线）</li>
     * </ol>
     */
    public void cinemaMode() {
        System.out.println("  ╔══════════════════════════════════════╗");
        System.out.println("  ║        🎬 影院模式 启动              ║");
        System.out.println("  ╚══════════════════════════════════════╝");

        lightSystem.setCinemaMode();
        lightSystem.turnOn();
        airConditioningSystem.setComfortMode();
        airConditioningSystem.turnOn();
        audioSystem.setCinemaMode();
        audioSystem.turnOn();
        audioSystem.play();
        curtainSystem.close();

        System.out.println("  ✓ 影院模式完成，享受观影时光！\n");
    }

    /**
     * 睡眠模式 —— 准备入睡环境。
     *
     * <p>典型操作序列：</p>
     * <ol>
     *   <li>关闭灯光</li>
     *   <li>空调切换至节能模式（整夜运行）</li>
     *   <li>关闭音响</li>
     *   <li>关闭窗帘</li>
     * </ol>
     */
    public void sleepMode() {
        System.out.println("  ╔══════════════════════════════════════╗");
        System.out.println("  ║        🌙 睡眠模式 启动              ║");
        System.out.println("  ╚══════════════════════════════════════╝");

        lightSystem.turnOff();
        airConditioningSystem.setEcoMode();
        airConditioningSystem.turnOn();
        audioSystem.turnOff();
        curtainSystem.close();

        System.out.println("  ✓ 睡眠模式完成，晚安好梦！\n");
    }

    /**
     * 起床模式 —— 清晨唤醒。
     *
     * <p>典型操作序列：</p>
     * <ol>
     *   <li>窗帘缓缓打开（晨起模式）</li>
     *   <li>灯光开启（柔和亮度）</li>
     *   <li>关闭空调（节省能源）</li>
     *   <li>音响播放轻音乐</li>
     * </ol>
     */
    public void wakeUpMode() {
        System.out.println("  ╔══════════════════════════════════════╗");
        System.out.println("  ║        ☀️ 起床模式 启动              ║");
        System.out.println("  ╚══════════════════════════════════════╝");

        curtainSystem.setMorningMode();
        lightSystem.setBrightness(40);
        lightSystem.setColor("暖白色");
        lightSystem.turnOn();
        airConditioningSystem.turnOff();
        audioSystem.setMusicMode();
        audioSystem.turnOn();
        audioSystem.play();

        System.out.println("  ✓ 起床模式完成，新的一天开始！\n");
    }

}
