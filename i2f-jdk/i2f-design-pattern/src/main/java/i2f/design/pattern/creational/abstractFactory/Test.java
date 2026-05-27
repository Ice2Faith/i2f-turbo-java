package i2f.design.pattern.creational.abstractFactory;

import i2f.design.pattern.creational.abstractFactory.chair.Chair;
import i2f.design.pattern.creational.abstractFactory.factory.FurnitureFactory;
import i2f.design.pattern.creational.abstractFactory.factory.impl.ClassicFurnitureFactory;
import i2f.design.pattern.creational.abstractFactory.factory.impl.ModernFurnitureFactory;
import i2f.design.pattern.creational.abstractFactory.table.Table;

/**
 * 抽象工厂模式 —— 调用演示
 *
 * <p>演示抽象工厂模式的核心机制：客户端面向抽象工厂（{@link FurnitureFactory}）编程，
 * 由具体工厂子类创建风格一致的一整套家具（椅子 + 桌子），实现产品族的统一管理与风格保证。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 */
public class Test {
    public static void main(String[] args) {

        // ==================== 1. 抽象工厂核心演示 ====================
        System.out.println("====== 抽象工厂模式（Abstract Factory）演示 ======");
        System.out.println("场景：家具工厂（Abstract Factory）通过工厂接口创建一整套配套家具");
        System.out.println("      现代工厂创建现代风格的椅子 + 桌子");
        System.out.println("      古典工厂创建古典风格的椅子 + 桌子\n");

        // ==================== 2. 现代家具工厂 —— 创建现代风格产品族 ====================
        System.out.println("────── 现代风格家具 ──────");
        FurnitureFactory modernFactory = new ModernFurnitureFactory();
        modernFactory.furnishRoom("现代客厅");

        System.out.println();

        // ==================== 3. 古典家具工厂 —— 创建古典风格产品族 ====================
        System.out.println("────── 古典风格家具 ──────");
        FurnitureFactory classicFactory = new ClassicFurnitureFactory();
        classicFactory.furnishRoom("古典书房");

        System.out.println();

        // ==================== 4. 面向抽象编程演示 ====================
        System.out.println("====== 面向抽象编程 —— 客户端无需知道具体工厂类型 ======");
        System.out.println("通过统一接口调度不同的家具工厂：\n");

        FurnitureFactory[] factories = {new ModernFurnitureFactory(), new ClassicFurnitureFactory()};
        String[] roomNames = {"简约卧室", "中式茶室"};

        for (int i = 0; i < factories.length; i++) {
            System.out.println("布置任务 " + (i + 1) + "：");
            factories[i].furnishRoom(roomNames[i]);
            System.out.println();
        }

        // ==================== 5. 验证产品族一致性 ====================
        System.out.println("====== 验证：同一工厂创建的产品族风格一致 ======");
        Chair modernChair = modernFactory.createChair();
        Table modernTable = modernFactory.createTable();
        System.out.println("现代工厂 → 椅子风格: " + modernChair.getStyle());
        System.out.println("现代工厂 → 桌子风格: " + modernTable.getStyle());
        System.out.println("风格一致？ " + modernChair.getStyle().equals(modernTable.getStyle()));

        System.out.println();

        Chair classicChair = classicFactory.createChair();
        Table classicTable = classicFactory.createTable();
        System.out.println("古典工厂 → 椅子风格: " + classicChair.getStyle());
        System.out.println("古典工厂 → 桌子风格: " + classicTable.getStyle());
        System.out.println("风格一致？ " + classicChair.getStyle().equals(classicTable.getStyle()));

        System.out.println();

        // ==================== 6. 验证工厂每次创建新实例 ====================
        System.out.println("====== 验证：工厂每次调用创建全新实例 ======");
        Chair c1 = modernFactory.createChair();
        Chair c2 = modernFactory.createChair();
        System.out.println("c1: " + c1);
        System.out.println("c2: " + c2);
        System.out.println("c1 == c2 ? " + (c1 == c2));

        System.out.println();

        // ==================== 7. 模式优势总结 ====================
        System.out.println("====== 抽象工厂模式优势总结 ======");
        System.out.println("1. 保证产品族一致性：同一工厂创建的椅子与桌子风格必然协调");
        System.out.println("2. 客户端面向抽象编程：无需依赖具体产品类，降低耦合");
        System.out.println("3. 产品族易于切换：更换工厂实例即可切换整套家具风格");
        System.out.println("4. 新增产品族遵循开闭原则：新增风格只需新增工厂子类，无需修改已有代码");
        System.out.println("5. 与工厂方法的区别：工厂方法关注单个产品的创建，抽象工厂关注整个产品族的一致性");
    }
}
