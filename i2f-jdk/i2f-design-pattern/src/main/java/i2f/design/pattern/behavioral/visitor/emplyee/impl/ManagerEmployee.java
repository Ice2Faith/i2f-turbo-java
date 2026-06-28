package i2f.design.pattern.behavioral.visitor.emplyee.impl;

import i2f.design.pattern.behavioral.visitor.visitor.EmployeeVisitor;
import i2f.design.pattern.behavioral.visitor.emplyee.Employee;

/**
 * 访问者模式 —— 经理（ConcreteElement：ManagerEmployee）
 *
 * <p><b>角色：</b>具体元素（Concrete Element）</p>
 *
 * <p><b>模式说明：</b>实现 accept 方法，调用访问者的 visit(ManagerEmployee) 方法，
 * 完成"双分派"的第一步——确定元素类型为经理。</p>
 *
 * <p><b>业务特性：</b>经理的薪资结构 = 基本工资 + 团队绩效奖金 + 管理津贴</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see Employee
 * @see StaffEmployee
 * @see ExecutiveEmployee
 */
public class ManagerEmployee extends Employee {

    /**
     * 团队规模（人数）。
     */
    private int teamSize;

    /**
     * 团队绩效奖金。
     */
    private double teamBonus;

    /**
     * 管理津贴。
     */
    private double managementAllowance;

    public ManagerEmployee(String name, double baseSalary, int teamSize, double teamBonus, double managementAllowance) {
        super(name, "经理", baseSalary);
        this.teamSize = teamSize;
        this.teamBonus = teamBonus;
        this.managementAllowance = managementAllowance;
    }

    /**
     * 接受访问者——调用访问者的 visit(ManagerEmployee) 方法。
     *
     * @param visitor 访问者实例
     */
    @Override
    public void accept(EmployeeVisitor visitor) {
        visitor.visit(this);
    }

    public int getTeamSize() {
        return teamSize;
    }

    public double getTeamBonus() {
        return teamBonus;
    }

    public double getManagementAllowance() {
        return managementAllowance;
    }

    @Override
    public String toString() {
        return String.format("%s [经理] - 基本工资：¥%.2f, 团队：%d人, 团队奖金：¥%.2f, 管理津贴：¥%.2f",
                name, baseSalary, teamSize, teamBonus, managementAllowance);
    }
}
