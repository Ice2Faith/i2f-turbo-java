package i2f.design.pattern.creational.singleton.lazy;

import lombok.Data;

/**
 * 单例模式 —— 懒汉式（双重检查锁，Double-Checked Locking）
 *
 * <p><b>模式说明：</b>实例在第一次被访问时才创建（延迟加载），
 * 通过 volatile + 双重 null 检查 + synchronized 保证多线程环境下的正确性与高性能。</p>
 *
 * <pre>
 * 执行流程（并发场景）：
 *
 *  线程A          线程B
 *   |               |
 *   | 外层检查null   | 外层检查null
 *   | (均为null,     |
 *   |  均进入同步块) |
 *   |               |
 *   | 获得锁        | 等待锁
 *   | 内层检查null  |
 *   | 创建实例      |
 *   | 释放锁        |
 *   |               | 获得锁
 *   |               | 内层检查非null → 直接返回
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 */
@Data
public class Earth {

    /**
     * 单例实例引用。
     *
     * <p><b>为何必须使用 volatile？</b></p>
     * <p>对象创建在 JVM 层面分为三步：</p>
     * <ol>
     *   <li>在堆上分配内存空间</li>
     *   <li>调用构造方法，初始化对象字段</li>
     *   <li>将内存地址赋值给引用变量 instance</li>
     * </ol>
     * <p>JVM / CPU 在不影响单线程语义的前提下，可能将步骤 2 与步骤 3 <b>重排序</b>，
     * 使得 instance 先指向一块"未初始化完毕"的内存。
     * 若此时另一个线程执行外层 {@code if(instance!=null)} 检查，
     * 将拿到一个半初始化的对象，产生难以排查的 Bug。</p>
     * <p>{@code volatile} 通过在写操作后插入 <b>StoreLoad 内存屏障</b>，
     * 禁止步骤 2、3 发生重排序，确保其他线程读到的 instance 一定是完整初始化后的对象。</p>
     */
    private static volatile Earth instance;

    /**
     * 私有构造方法。
     *
     * <p>禁止外部通过 {@code new Earth()} 创建实例，
     * 将实例化权完全收归 {@link #getInstance()} 控制，
     * 从而保证全局唯一性。</p>
     */
    private Earth() {

    }

    /**
     * 单例对象的初始化工厂方法。
     *
     * <p>将"创建对象"与"初始化附加逻辑"分离到独立方法，
     * 职责清晰，且便于子类或测试场景重写初始化步骤。</p>
     * <p>此方法仅在 {@link #getInstance()} 内部的同步块中被调用，
     * 天然处于单线程上下文，无需额外同步。</p>
     *
     * @return 完整初始化后的 Earth 实例
     */
    private static Earth initialInstance() {
        Earth ret = new Earth();
        // 其他初始化逻辑
        return ret;
    }

    /**
     * 获取 Earth 单例实例（双重检查锁实现）。
     *
     * <p><b>第一次 null 检查（外层，无锁）：</b><br>
     * 实例已存在时，绝大多数调用直接在此返回，
     * 完全避免进入 synchronized 块的锁竞争开销，保证高并发读性能。</p>
     *
     * <p><b>synchronized(Earth.class)：</b><br>
     * 以 Class 对象作为互斥锁，同一时刻只允许一个线程进入创建逻辑，
     * 防止多个线程同时通过外层检查后重复创建实例。</p>
     *
     * <p><b>第二次 null 检查（内层，持锁）：</b><br>
     * 解决"线程 A 和线程 B 同时通过外层检查"的竞态条件：
     * A 先拿到锁并完成创建后，B 拿到锁时必须再次判断，
     * 若已非 null 则直接返回，避免重复实例化。</p>
     *
     * @return 全局唯一的 Earth 实例
     */
    public static Earth getInstance() {
        // 第一次检查：实例已存在则直接返回，避免无谓的锁竞争（高频读路径）
        if (instance != null) {
            return instance;
        }
        // 加锁：保证同一时刻只有一个线程执行实例化逻辑
        synchronized (Earth.class) {
            // 第二次检查：防止在等待锁期间其他线程已完成初始化（关键！缺少此检查会导致重复创建）
            if (instance != null) {
                return instance;
            }
            // 此时确认 instance 为 null，且处于同步块内，安全创建实例
            // volatile 保证赋值对其他线程立即可见，且赋值发生在对象完整初始化之后
            instance = initialInstance();
        }
        return instance;
    }

}
