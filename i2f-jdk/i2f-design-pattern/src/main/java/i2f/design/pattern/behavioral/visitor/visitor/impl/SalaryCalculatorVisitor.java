package i2f.design.pattern.behavioral.visitor.visitor.impl;

import i2f.design.pattern.behavioral.visitor.emplyee.impl.ExecutiveEmployee;
import i2f.design.pattern.behavioral.visitor.emplyee.impl.ManagerEmployee;
import i2f.design.pattern.behavioral.visitor.emplyee.impl.StaffEmployee;
import i2f.design.pattern.behavioral.visitor.visitor.EmployeeVisitor;

/**
 * 访问者模式 —— 薪资计算器访问者（ConcreteVisitor：SalaryCalculatorVisitor）
 *
 * <p><b>角色：</b>具体访问者（Concrete Visitor）</p>
 *
 * <p><b>模式说明：</b>实现对每种员工类型的薪资计算逻辑。
 * 每种员工的薪资结构不同，访问者可以针对不同类型执行不同的计算规则，
 * 而无需在员工类中硬编码这些计算逻辑。</p>
 *
 * <p><b>业务规则：</b></p>
 * <ul>
 *   <li>普通员工：基本工资 + 加班费</li>
 *   <li>经理：基本工资 + 团队绩效奖金 + 管理津贴</li>
 *   <li>高管：基本工资 + 年度分红 + 股票期权</li>
 * </ul>
 *
 * <p><b>优势体现：</b>如果未来需要调整薪资计算规则，
 * 只需修改此访问者类或新增一个薪资计算访问者，
 * 完全不需要修改任何员工类。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see EmployeeVisitor
 * @see AnnualReportVisitor
 */
public class SalaryCalculatorVisitor implements EmployeeVisitor {

    /**
     * 累计总薪资。
     */
    private double totalSalary = 0;

    /**
     * 访问普通员工——计算其总薪资。
     *
     * <p>计算规则：基本工资 + 加班费</p>
     *
     * @param employee 普通员工实例
     */
    @Override
    public void visit(StaffEmployee employee) {
        double overtimePay = employee.getOvertimeHours() * employee.getHourlyOvertimeRate();
        double totalPay = employee.getBaseSalary() + overtimePay;
        totalSalary += totalPay;

        System.out.printf("  [普通员工] %s - 基本工资：¥%.2f + 加班费：¥%.2f = ¥%.2f%n",
                employee.getName(), employee.getBaseSalary(), overtimePay, totalPay);
    }

    /**
     * 访问经理——计算其总薪资。
     *
     * <p>计算规则：基本工资 + 团队绩效奖金 + 管理津贴</p>
     *
     * @param employee 经理实例
     */
    @Override
    public void visit(ManagerEmployee employee) {
        double totalPay = employee.getBaseSalary() + employee.getTeamBonus() + employee.getManagementAllowance();
        totalSalary += totalPay;

        System.out.printf("  [经理] %s - 基本工资：¥%.2f + 团队奖金：¥%.2f + 管理津贴：¥%.2f = ¥%.2f%n",
                employee.getName(), employee.getBaseSalary(), employee.getTeamBonus(),
                employee.getManagementAllowance(), totalPay);
    }

    /**
     * 访问高管——计算其总薪资。
     *
     * <p>计算规则：基本工资 + 年度分红 + 股票期权</p>
     *
     * @param employee 高管实例
     */
    @Override
    public void visit(ExecutiveEmployee employee) {
        double profitSharing = employee.getCompanyProfit() * employee.getProfitSharingPercentage() / 100;
        double totalPay = employee.getBaseSalary() + profitSharing + employee.getStockOptions();
        totalSalary += totalPay;

        System.out.printf("  [高管] %s - 基本工资：¥%.2f + 年度分红：¥%.2f + 股票期权：¥%.2f = ¥%.2f%n",
                employee.getName(), employee.getBaseSalary(), profitSharing,
                employee.getStockOptions(), totalPay);
    }

    /**
     * 获取累计的总薪资。
     *
     * @return 所有员工薪资总和
     */
    public double getTotalSalary() {
        return totalSalary;
    }
}
