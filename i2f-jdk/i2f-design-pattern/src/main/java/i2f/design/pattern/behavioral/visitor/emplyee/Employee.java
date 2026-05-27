package i2f.design.pattern.behavioral.visitor.emplyee;

import i2f.design.pattern.behavioral.visitor.visitor.EmployeeVisitor;
import i2f.design.pattern.behavioral.visitor.emplyee.impl.ExecutiveEmployee;
import i2f.design.pattern.behavioral.visitor.emplyee.impl.ManagerEmployee;
import i2f.design.pattern.behavioral.visitor.emplyee.impl.StaffEmployee;

/**
 * 访问者模式 —— 员工（Element：Employee）
 *
 * <p><b>角色：</b>抽象元素（Abstract Element）</p>
 *
 * <p><b>模式说明：</b>声明接受访问者的方法 {@link #accept(EmployeeVisitor)}，
 * 这是访问者模式的入口点。每个具体元素类必须实现此方法，
 * 调用访问者对应的 visit 方法并将自身（this）传入，实现"双分派"机制。</p>
 *
 * <p><b>命名立意：</b>以"公司员工"为场景——公司有不同类型的员工（普通员工、经理、高管），
 * 每种员工的薪资结构、绩效评估方式不同。当需要对这些员工执行不同操作时
 * （如计算总工资、生成年度报告），使用访问者模式可以在不修改员工类的前提下
 * 轻松添加新操作。</p>
 *
 * <p><b>双分派（Double Dispatch）：</b></p>
 * <pre>
 *  第一次分派：element.accept(visitor)  →  根据 element 的具体类型决定调用哪个 accept
 *  第二次分派：visitor.visit(this)      →  根据 visitor 的具体类型决定调用哪个 visit
 *
 *  结果：在运行时同时确定了 element 和 visitor 的具体类型，实现了真正的多态。
 * </pre>
 *
 * <p><b>类层次结构：</b></p>
 * <pre>
 *  抽象元素              具体元素
 *  ─────────────────   ─────────────────────────────
 *  Employee             StaffEmployee（普通员工）
 *                       ManagerEmployee（经理）
 *                       ExecutiveEmployee（高管）
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see StaffEmployee
 * @see ManagerEmployee
 * @see ExecutiveEmployee
 * @see EmployeeVisitor
 */
public abstract class Employee {

    /**
     * 员工姓名。
     */
    protected String name;

    /**
     * 员工职位。
     */
    protected String position;

    /**
     * 基本工资。
     */
    protected double baseSalary;

    public Employee(String name, String position, double baseSalary) {
        this.name = name;
        this.position = position;
        this.baseSalary = baseSalary;
    }

    /**
     * 接受访问者——访问者模式的入口方法。
     *
     * <p>具体元素实现此方法时，调用访问者的对应 visit 方法，
     * 并将自身（this）传入。这就是"双分派"的核心机制：</p>
     * <ul>
     *   <li>第一次分派：根据元素的具体类型决定调用哪个 accept 方法</li>
     *   <li>第二次分派：根据访问者的具体类型决定调用哪个 visit 方法</li>
     * </ul>
     *
     * @param visitor 访问者实例
     */
    public abstract void accept(EmployeeVisitor visitor);

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    @Override
    public String toString() {
        return String.format("%s [%s] - 基本工资：¥%.2f", name, position, baseSalary);
    }
}
