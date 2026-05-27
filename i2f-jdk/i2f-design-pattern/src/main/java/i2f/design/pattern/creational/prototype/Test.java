package i2f.design.pattern.creational.prototype;

/**
 * 原型模式 —— 调用演示
 *
 * <p>演示通过克隆原型对象快速创建新对象，并对比浅拷贝与深拷贝的行为差异。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 */
public class Test {
    public static void main(String[] args) {

        // ==================== 1. 构建原型对象 ====================
        System.out.println("====== 构建原型对象 ======");
        Sheep prototype = new Sheep("Dolly", 3, "白色");
        Sheep friend = new Sheep("Bella", 2, "黑色");
        prototype.setCompanion(friend);
        System.out.println("原型: " + prototype);

        // ==================== 2. 浅拷贝演示 ====================
        System.out.println("\n====== 浅拷贝（Shallow Clone） ======");
        Sheep shallowClone = prototype.clone();
        shallowClone.setName("Dolly-浅拷贝");
        System.out.println("克隆体: " + shallowClone);

        // 验证：浅拷贝的引用字段 companion 与原型共享同一对象
        System.out.println("原型.companion == 克隆体.companion ? "
                + (prototype.getCompanion() == shallowClone.getCompanion()));
        // 修改克隆体的 companion 名称，原型也会受影响
        shallowClone.getCompanion().setName("Bella-被修改");
        System.out.println("修改克隆体的companion后，原型: " + prototype);
        System.out.println("修改克隆体的companion后，克隆体: " + shallowClone);

        // ==================== 3. 深拷贝演示 ====================
        System.out.println("\n====== 深拷贝（Deep Clone） ======");
        // 还原 companion 名称
        friend.setName("Bella");
        Sheep deepClone = prototype.deepClone();
        deepClone.setName("Dolly-深拷贝");
        System.out.println("克隆体: " + deepClone);

        // 验证：深拷贝的引用字段 companion 是全新的独立对象
        System.out.println("原型.companion == 克隆体.companion ? "
                + (prototype.getCompanion() == deepClone.getCompanion()));
        // 修改克隆体的 companion 名称，原型不受影响
        deepClone.getCompanion().setName("Bella-深拷贝独立修改");
        System.out.println("修改克隆体的companion后，原型: " + prototype);
        System.out.println("修改克隆体的companion后，克隆体: " + deepClone);

        // ==================== 4. 原型模式优势总结 ====================
        System.out.println("\n====== 总结 ======");
        System.out.println("1. 原型模式通过拷贝已有对象创建新对象，避免重复的初始化开销");
        System.out.println("2. 浅拷贝性能高但引用类型字段共享，深拷贝完全独立但性能稍低");
        System.out.println("3. 适用于对象创建成本高、需要快照、或避免复杂构造体系的场景");
    }
}
