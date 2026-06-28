package i2f.design.pattern.behavioral.visitor.emplyee.impl;

import i2f.design.pattern.behavioral.visitor.visitor.EmployeeVisitor;
import i2f.design.pattern.behavioral.visitor.emplyee.Employee;

/**
 * 访问者模式 —— 高管（ConcreteElement：ExecutiveEmployee）
 *
 * <p><b>角色：</b>具体元素（Concrete Element）</p>
 *
 * <p><b>模式说明：</b>实现 accept 方法，调用访问者的 visit(ExecutiveEmployee) 方法，
 * 完成"双分派"的第一步——确定元素类型为高管。</p>
 *
 * <p><b>业务特性：</b>高管的薪资结构 = 基本工资 + 年度分红 + 股票期权</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see Employee
 * @see StaffEmployee
 * @see ManagerEmployee
 */
public class ExecutiveEmployee extends Employee {

    /**
     * 年度分红比例（%）。
     */
    private double profitSharingPercentage;

    /**
     * 股票期权价值。
     */
    private double stockOptions;

    /**
     * 公司年度利润（用于计算分红）。
     */
    private double companyProfit;

    public ExecutiveEmployee(String name, double baseSalary, double profitSharingPercentage, 
                            double stockOptions, double companyProfit) {
        super(name, "高管", baseSalary);
        this.profitSharingPercentage = profitSharingPercentage;
        this.stockOptions = stockOptions;
        this.companyProfit = companyProfit;
    }

    /**
     * 接受访问者——调用访问者的 visit(ExecutiveEmployee) 方法。
     *
     * @param visitor 访问者实例
     */
    @Override
    public void accept(EmployeeVisitor visitor) {
        visitor.visit(this);
    }

    public double getProfitSharingPercentage() {
        return profitSharingPercentage;
    }

    public double getStockOptions() {
        return stockOptions;
    }

    public double getCompanyProfit() {
        return companyProfit;
    }

    /**
     * 计算年度分红。
     *
     * @return 分红金额
     */
    public double calculateProfitSharing() {
        return companyProfit * profitSharingPercentage / 100;
    }

    @Override
    public String toString() {
        return String.format("%s [高管] - 基本工资：¥%.2f, 分红比例：%.1f%% (¥%.2f), 股票期权：¥%.2f",
                name, baseSalary, profitSharingPercentage, calculateProfitSharing(), stockOptions);
    }
}
