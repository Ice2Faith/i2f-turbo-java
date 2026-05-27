/**
 * 访问者模式（Visitor）
 * <p>
 * 表示一个作用于某对象结构中各元素的操作，在不改变各元素的类的前提下定义作用于这些元素的新操作。
 * 分类：行为型模式
 * </p>
 *
 * <h3>模式说明</h3>
 * <p>访问者模式的核心思想是：<b>将数据结构与作用于数据上的操作解耦</b>。
 * 当对象结构中包含多种类型的对象，且需要对这些对象执行不同的、不相关的操作时，
 * 访问者模式可以在不修改元素类的前提下轻松添加新操作。</p>
 *
 * <h3>核心机制：双分派（Double Dispatch）</h3>
 * <pre>
 * 第一次分派：element.accept(visitor)
 *   → 根据 element 的具体类型决定调用哪个 accept 实现
 *
 * 第二次分派：visitor.visit(this)
 *   → 根据 visitor 的具体类型决定调用哪个 visit 方法
 *
 * 结果：在运行时同时确定了 element 和 visitor 的具体类型
 * </pre>
 *
 * <h3>模式结构</h3>
 * <pre>
 * Visitor（抽象访问者）
 *   └─ visit(ConcreteElementA): void
 *   └─ visit(ConcreteElementB): void
 *
 * ConcreteVisitor（具体访问者）
 *   ├─ SalaryCalculatorVisitor（薪资计算器）
 *   └─ AnnualReportVisitor（年度报告生成器）
 *
 * Element（抽象元素）
 *   └─ accept(visitor: Visitor): void
 *
 * ConcreteElement（具体元素）
 *   ├─ StaffEmployee（普通员工）
 *   ├─ ManagerEmployee（经理）
 *   └─ ExecutiveEmployee（高管）
 *
 * ObjectStructure（对象结构）
 *   └─ List&lt;Employee&gt;（员工列表）
 * </pre>
 *
 * <h3>适用场景</h3>
 * <ul>
 *   <li>对象结构中包含多种类型的对象，希望对这些对象施加不同的操作</li>
 *   <li>对象结构很少改变，但常常需要增加新的操作</li>
 *   <li>编译器 AST 处理、语法分析、财务报表系统等</li>
 * </ul>
 *
 * <h3>典型案例</h3>
 * <ul>
 *   <li>JDK：{@link java.nio.file.FileVisitor}（遍历文件树）、{@link javax.lang.model.element.ElementVisitor}（注解处理器 APT）</li>
 *   <li>Spring：{@link org.springframework.beans.factory.config.BeanDefinitionVisitor}（访问并替换 BeanDefinition 中的占位符字符串）</li>
 *   <li>ASM 字节码框架：{@code ClassVisitor}、{@code MethodVisitor}、{@code FieldVisitor}</li>
 *   <li>编译器：JavaC AST 访问者</li>
 * </ul>
 *
 * <h3>模式优势</h3>
 * <ul>
 *   <li>遵循开闭原则：新增操作只需新增访问者类，无需修改已有元素类</li>
 *   <li>遵循单一职责：每个访问者只负责一种操作，逻辑集中易维护</li>
 *   <li>访问者可以累积状态（如累计总薪资、统计总数等）</li>
 * </ul>
 *
 * <h3>模式局限</h3>
 * <ul>
 *   <li>增加新的元素类很困难（需要修改所有访问者接口和实现）</li>
 *   <li>破坏封装（访问者需要访问元素的内部状态）</li>
 *   <li>仅适用于元素类型固定、操作类型多变的场景</li>
 * </ul>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see i2f.design.pattern.behavioral.visitor.Test
 */
package i2f.design.pattern.behavioral.visitor;
