package i2f.design.pattern.creational.singleton.eager;

import lombok.Data;

/**
 * 单例模式 —— 饿汉式（Eager Initialization）
 *
 * <p><b>模式说明：</b>类被 JVM 加载时立即创建唯一实例，无需任何同步控制。
 * 利用 JVM 类加载机制（Class Loading 由 ClassLoader 保证线程安全）天然实现多线程安全。</p>
 *
 * <p><b>与懒汉式（DCL）对比：</b></p>
 * <pre>
 *  饿汉式                          懒汉式（DCL）
 *  ─────────────────────────────   ───────────────────────────────
 *  类加载时立即创建实例              首次调用 getInstance() 才创建
 *  天然线程安全，无需 synchronized   需要 volatile + 双重 null 检查
 *  内存占用早（类加载即分配）         内存占用晚（用时才分配）
 *  适合实例轻量或确定会被使用的场景   适合实例重量或不一定使用的场景
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 */
@Data
public class Earth {

    /**
     * 饿汉式单例实例引用。
     *
     * <p><b>初始化时机：</b>JVM 加载 Earth 类时，静态字段按声明顺序依次初始化，
     * {@code initialInstance()} 在此处被直接调用，实例在类加载完成前就已创建。</p>
     *
     * <p><b>无需 volatile：</b>JVM 类加载机制通过 {@code <clinit>} 类初始化锁（LC）
     * 保证静态字段初始化的原子性与可见性，所有线程在访问该字段前已发生
     * happens-before 关系，不存在重排序风险，故此处不需要 volatile。</p>
     */
    private static Earth instance = initialInstance();

    /**
     * 私有构造方法。
     *
     * <p>外部无法通过 {@code new Earth()} 创建实例，
     * 所有实例化权归 {@link #initialInstance()} 独占控制。</p>
     */
    private Earth() {

    }

    /**
     * 创建并初始化单例实例（仅在类加载阶段调用一次）。
     *
     * <p>将创建逻辑与 {@code getInstance()} 分离，职责更清晰，
     * 便于集中扩展初始化步骤（如资源预加载、参数校验等），
     * 且此方法在类加载的单线程上下文中执行，天然线程安全。</p>
     *
     * @return 初始化完成的唯一实例
     */
    private static Earth initialInstance() {
        Earth ret = new Earth();
        // 其他初始化逻辑
        return ret;
    }

    /**
     * 获取单例实例（全局访问点）。
     *
     * <p>由于实例在类加载时已完成创建，此方法仅做一次 volatile 读，
     * <b>无需任何加锁</b>，并发性能等同于普通字段读取。</p>
     *
     * @return Earth 的唯一实例
     */
    public static Earth getInstance() {
        return instance;
    }

}
