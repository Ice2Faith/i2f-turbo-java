package i2f.design.pattern.behavioral.visitor.emplyee.impl;

import i2f.design.pattern.behavioral.visitor.visitor.EmployeeVisitor;
import i2f.design.pattern.behavioral.visitor.emplyee.Employee;

/**
 * 访问者模式 —— 普通员工（ConcreteElement：StaffEmployee）
 *
 * <p><b>角色：</b>具体元素（Concrete Element）</p>
 *
 * <p><b>模式说明：</b>实现 accept 方法，调用访问者的 visit(StaffEmployee) 方法，
 * 完成"双分派"的第一步——确定元素类型为普通员工。</p>
 *
 * <p><b>业务特性：</b>普通员工的薪资结构 = 基本工资 + 加班费</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see Employee
 * @see ManagerEmployee
 * @see ExecutiveEmployee
 */
public class StaffEmployee extends Employee {

    /**
     * 加班小时数。
     */
    private int overtimeHours;

    /**
     * 每小时加班费。
     */
    private double hourlyOvertimeRate;

    public StaffEmployee(String name, double baseSalary, int overtimeHours, double hourlyOvertimeRate) {
        super(name, "普通员工", baseSalary);
        this.overtimeHours = overtimeHours;
        this.hourlyOvertimeRate = hourlyOvertimeRate;
    }

    /**
     * 接受访问者——调用访问者的 visit(StaffEmployee) 方法。
     *
     * <p>这就是"双分派"的核心：this 的编译时类型是 StaffEmployee，
     * 因此会精确匹配到 EmployeeVisitor.visit(StaffEmployee) 方法。</p>
     *
     * @param visitor 访问者实例
     */
    @Override
    public void accept(EmployeeVisitor visitor) {
        visitor.visit(this);
    }

    public int getOvertimeHours() {
        return overtimeHours;
    }

    public double getHourlyOvertimeRate() {
        return hourlyOvertimeRate;
    }

    /**
     * 计算加班费。
     *
     * @return 加班费总额
     */
    public double calculateOvertimePay() {
        return overtimeHours * hourlyOvertimeRate;
    }

    @Override
    public String toString() {
        return String.format("%s [普通员工] - 基本工资：¥%.2f, 加班：%dh × ¥%.2f/h = ¥%.2f",
                name, baseSalary, overtimeHours, hourlyOvertimeRate, calculateOvertimePay());
    }
}
