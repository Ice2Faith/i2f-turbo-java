package i2f.design.pattern.behavioral.visitor.visitor;

import i2f.design.pattern.behavioral.visitor.visitor.impl.AnnualReportVisitor;
import i2f.design.pattern.behavioral.visitor.visitor.impl.SalaryCalculatorVisitor;
import i2f.design.pattern.behavioral.visitor.emplyee.Employee;
import i2f.design.pattern.behavioral.visitor.emplyee.impl.ExecutiveEmployee;
import i2f.design.pattern.behavioral.visitor.emplyee.impl.ManagerEmployee;
import i2f.design.pattern.behavioral.visitor.emplyee.impl.StaffEmployee;

/**
 * 访问者模式 —— 员工访问者（Visitor：EmployeeVisitor）
 *
 * <p><b>角色：</b>抽象访问者（Abstract Visitor）</p>
 *
 * <p><b>模式说明：</b>声明针对每种具体元素类型的 visit 方法。
 * 方法名称相同（visit），但参数类型不同（方法重载），
 * 这是实现"双分派"第二次分派的关键。</p>
 *
 * <p><b>设计意图：</b>每当需要为对象结构添加新操作时，
 * 只需新增一个实现此接口的具体访问者类，
 * 而无需修改任何元素类（符合开闭原则）。</p>
 *
 * <p><b>方法签名：</b></p>
 * <pre>
 *  visit(StaffEmployee)      → 处理普通员工的逻辑
 *  visit(ManagerEmployee)    → 处理经理的逻辑
 *  visit(ExecutiveEmployee)  → 处理高管的逻辑
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see SalaryCalculatorVisitor
 * @see AnnualReportVisitor
 * @see Employee
 */
public interface EmployeeVisitor {

    /**
     * 访问普通员工。
     *
     * @param employee 普通员工实例
     */
    void visit(StaffEmployee employee);

    /**
     * 访问经理。
     *
     * @param employee 经理实例
     */
    void visit(ManagerEmployee employee);

    /**
     * 访问高管。
     *
     * @param employee 高管实例
     */
    void visit(ExecutiveEmployee employee);
}
