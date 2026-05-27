/**
 * 模板方法模式（Template Method）
 * <p>
 * 定义一个操作中的算法的骨架，而将一些步骤延迟到子类中，子类可以不改变算法的结构即可重定义某些步骤。
 * 分类：行为型模式
 * </p>
 *
 * <p><b>场景说明：</b>以"制作饮品"为例——制作咖啡和制作茶的流程是固定的
 * （烧水→冲泡→倒入杯子→添加调料），但每一步的具体实现不同。
 * 模板方法模式将这些固定流程定义在抽象父类中，具体步骤由子类实现。</p>
 *
 * <p><b>模式结构：</b></p>
 * <pre>
 *  AbstractClass（抽象类）
 *    └─ makeDrink(): void                   ← 模板方法（固定算法骨架）
 *    ├─ boilWater(): void                   ← 具体方法（所有子类共用）
 *    ├─ brew(): void                        ← 抽象方法（子类必须实现）
 *    ├─ pourInCup(): void                   ← 抽象方法（子类必须实现）
 *    └─ addCondiments(): void               ← 抽象方法（子类必须实现）
 *
 *  ConcreteClass（具体类）
 *    ├─ Coffee → 实现冲泡、倒杯、添加调料的具体步骤
 *    └─ Tea    → 实现冲泡、倒杯、添加调料的具体步骤
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 */
package i2f.design.pattern.behavioral.templateMethod;
