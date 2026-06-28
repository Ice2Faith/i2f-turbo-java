package i2f.design.pattern.creational.prototype;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;

/**
 * 原型模式 —— 克隆羊（Prototype: Sheep）
 *
 * <p><b>模式说明：</b>用原型实例指定创建对象的种类，并通过拷贝（clone）这些原型创建新的对象，
 * 无需依赖构造函数即可快速复制出结构相同的新实例。</p>
 *
 * <p><b>命名立意：</b>以"克隆羊多莉（Dolly）"为灵感——世界上第一只体细胞克隆动物，
 * 完美对应原型模式中"从已有对象拷贝出新对象"的核心思想。</p>
 *
 * <p><b>浅拷贝（Shallow Copy）与深拷贝（Deep Copy）对比：</b></p>
 * <pre>
 *  浅拷贝                              深拷贝
 *  ─────────────────────────────────   ─────────────────────────────────
 *  基本类型字段：值复制                 基本类型字段：值复制
 *  引用类型字段：仅复制引用（共享对象）   引用类型字段：递归复制整个对象图
 *  性能高，但修改引用对象会相互影响      性能低，但完全独立互不影响
 *  实现：Object#clone()                实现：序列化/反序列化 或 手动递归拷贝
 * </pre>
 *
 * <p><b>适用场景：</b></p>
 * <ul>
 *   <li>创建对象成本较高（如初始化耗时长、依赖外部资源），希望通过拷贝快速生成。</li>
 *   <li>需要保留对象在某一时刻的状态快照。</li>
 *   <li>需要避免复杂的构造函数层级或工厂体系。</li>
 * </ul>
 *
 * <p><b>接口实现意图：</b></p>
 * <ul>
 *   <li>{@link Cloneable} —— 标记接口，声明本类允许被 {@link Object#clone()} 按位复制。
 *       若未实现此接口，调用 {@code clone()} 将抛出 {@link CloneNotSupportedException}。
 *       <b>作用：</b>为浅拷贝提供 JVM 级别的底层支持（native 方法直接内存复制，性能高）。</li>
 *   <li>{@link Serializable} —— 序列化标记接口，声明本类的实例可被序列化为字节流。
 *       <b>作用：</b>为深拷贝提供实现基础——将对象写入字节流再反序列化回来，
 *       从而递归复制整个对象图（包括所有引用类型字段），实现完全独立的副本。</li>
 * </ul>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 */
@Data
@NoArgsConstructor
public class Sheep implements Cloneable, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 羊的名称。
     */
    private String name;

    /**
     * 羊的年龄。
     */
    private int age;

    /**
     * 羊的毛色。
     */
    private String color;

    /**
     * 羊的同伴引用（用于演示浅拷贝与深拷贝对引用类型字段的不同处理）。
     *
     * <p>浅拷贝时：原型与克隆体共享同一个 companion 对象。<br>
     * 深拷贝时：克隆体拥有独立的 companion 副本。</p>
     */
    private Sheep companion;

    public Sheep(String name, int age, String color) {
        this.name = name;
        this.age = age;
        this.color = color;
    }

    // ========================= 浅拷贝 =========================

    /**
     * 浅拷贝（Shallow Clone）。
     *
     * <p>调用 {@link Object#clone()} 实现字段逐位复制：
     * 基本类型按值复制，引用类型仅复制引用地址。
     * 因此原型与克隆体的 {@link #companion} 字段指向同一对象。</p>
     *
     * @return 浅拷贝的新 Sheep 实例
     */
    @Override
    public Sheep clone() {
        try {
            return (Sheep) super.clone();
        } catch (CloneNotSupportedException e) {
            // 实现了 Cloneable 接口，理论上不会抛出此异常
            throw new RuntimeException("克隆失败", e);
        }
    }


    // ========================= 深拷贝 =========================

    /**
     * 深拷贝（Deep Clone）—— 基于序列化实现。
     *
     * <p>将对象序列化为字节流再反序列化回来，从而递归复制整个对象图，
     * 克隆体与原型完全独立，修改任一方不会影响另一方。</p>
     *
     * <p><b>前提条件：</b>对象及其所有引用字段均需实现 {@link Serializable} 接口。</p>
     *
     * @return 深拷贝的新 Sheep 实例
     */
    public Sheep deepClone() {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {

            // 序列化：将当前对象写入字节流
            oos.writeObject(this);
            oos.flush();

            // 反序列化：从字节流中重建全新的对象图
            try (ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
                 ObjectInputStream ois = new ObjectInputStream(bis)) {
                return (Sheep) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("深拷贝失败", e);
        }
    }

    /**
     * 浅拷贝（Shallow Clone）—— 基于构造函数手动复制。
     *
     * <p>通过 new 创建新对象并逐字段赋值，引用类型字段仅复制引用地址（共享对象）。</p>
     *
     * <p><b>与 {@link #clone()} 的差异：</b></p>
     * <pre>
     *  clone()（JVM native）                shallowCloneDirect()（手动构造）
     *  ─────────────────────────────────   ─────────────────────────────────
     *  依赖 Cloneable 标记接口              不依赖任何标记接口
     *  JVM native 内存按位复制，性能更高     通过构造函数 + setter 赋值，性能略低
     *  自动复制所有字段（含 private）        需手动逐字段赋值，新增字段易遗漏
     *  绕过构造函数（不触发构造逻辑）         经过构造函数（会触发构造中的校验/初始化）
     *  返回 Object 需强转                   直接返回正确类型，类型安全
     * </pre>
     *
     * <p><b>适用场景：</b>类未实现 Cloneable、或需要在拷贝时触发构造函数中的校验逻辑。</p>
     *
     * @return 浅拷贝的新 Sheep 实例（companion 与原型共享同一引用）
     */
    public Sheep shallowCloneDirect() {
        Sheep ret = new Sheep(this.name, this.age, this.color);
        ret.setCompanion(this.companion);
        return ret;
    }

    /**
     * 深拷贝（Deep Clone）—— 基于手动递归复制实现。
     *
     * <p>通过 new 创建新对象，基本类型与 String 直接赋值（天然独立），
     * 引用类型字段递归调用其 {@code deepCloneDirect()} 方法逐层复制，
     * 最终生成完全独立的对象图。</p>
     *
     * <p><b>与 {@link #deepClone()} 的差异：</b></p>
     * <pre>
     *  deepClone()（序列化方式）             deepCloneDirect()（手动递归）
     *  ─────────────────────────────────   ─────────────────────────────────
     *  依赖 Serializable 标记接口           不依赖任何标记接口
     *  通用性强，自动处理任意深度对象图       需为每个引用字段手动编写递归逻辑
     *  性能较低（涉及 IO 流序列化/反序列化）  性能较高（直接内存操作，无 IO 开销）
     *  新增引用字段时无需修改拷贝逻辑         新增引用字段时必须同步更新此方法（易遗漏）
     *  可能触发 serialVersionUID 兼容问题    无序列化兼容性问题
     * </pre>
     *
     * <p><b>适用场景：</b>对象图层级浅且结构稳定、对性能敏感、或类无法实现 Serializable 时。</p>
     *
     * @return 深拷贝的新 Sheep 实例（companion 为全新的独立副本）
     */
    public Sheep deepCloneDirect() {
        Sheep ret = new Sheep(this.name, this.age, this.color);
        if (this.companion != null) {
            ret.setCompanion(this.companion.deepCloneDirect());
        }
        return ret;
    }

}
