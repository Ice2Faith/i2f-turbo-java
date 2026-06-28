package i2f.design.pattern.behavioral.visitor.visitor.impl;

import i2f.design.pattern.behavioral.visitor.emplyee.impl.ExecutiveEmployee;
import i2f.design.pattern.behavioral.visitor.emplyee.impl.ManagerEmployee;
import i2f.design.pattern.behavioral.visitor.emplyee.impl.StaffEmployee;
import i2f.design.pattern.behavioral.visitor.visitor.EmployeeVisitor;

/**
 * 访问者模式 —— 年度报告生成器访问者（ConcreteVisitor：AnnualReportVisitor）
 *
 * <p><b>角色：</b>具体访问者（Concrete Visitor）</p>
 *
 * <p><b>模式说明：</b>演示如何在不修改员工类的前提下，
 * 添加一个全新的操作——生成年度报告。
 * 这体现了访问者模式的核心优势：<b>"对象结构很少改变，但常常需要增加新的操作"</b>。</p>
 *
 * <p><b>业务功能：</b>为不同类型的员工生成不同的年度评估报告，
 * 报告内容因员工类型而异。</p>
 *
 * <p><b>扩展性：</b>未来如果还需要添加"绩效考核访问者"、"税务计算访问者"等，
 * 只需实现 EmployeeVisitor 接口即可，零侵入现有代码。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @see EmployeeVisitor
 * @see SalaryCalculatorVisitor
 */
public class AnnualReportVisitor implements EmployeeVisitor {

    /**
     * 访问普通员工——生成其年度评估报告。
     *
     * @param employee 普通员工实例
     */
    @Override
    public void visit(StaffEmployee employee) {
        System.out.println("  ═══════════════════════════════════════");
        System.out.println("  普通员工年度评估报告");
        System.out.println("  ═══════════════════════════════════════");
        System.out.println("  姓名：" + employee.getName());
        System.out.println("  岗位：普通员工");
        System.out.println("  基本工资：¥" + String.format("%.2f", employee.getBaseSalary()));
        System.out.println("  加班时长：" + employee.getOvertimeHours() + " 小时");
        System.out.println("  加班费率：¥" + String.format("%.2f", employee.getHourlyOvertimeRate()) + "/小时");
        System.out.println("  加班费总额：¥" + String.format("%.2f", employee.calculateOvertimePay()));
        System.out.println("  评估建议：关注工作效率，减少不必要的加班");
        System.out.println("  ═══════════════════════════════════════");
    }

    /**
     * 访问经理——生成其年度评估报告。
     *
     * @param employee 经理实例
     */
    @Override
    public void visit(ManagerEmployee employee) {
        System.out.println("  ═══════════════════════════════════════");
        System.out.println("  经理年度评估报告");
        System.out.println("  ═══════════════════════════════════════");
        System.out.println("  姓名：" + employee.getName());
        System.out.println("  岗位：经理");
        System.out.println("  基本工资：¥" + String.format("%.2f", employee.getBaseSalary()));
        System.out.println("  团队规模：" + employee.getTeamSize() + " 人");
        System.out.println("  团队绩效奖金：¥" + String.format("%.2f", employee.getTeamBonus()));
        System.out.println("  管理津贴：¥" + String.format("%.2f", employee.getManagementAllowance()));
        System.out.println("  评估建议：加强团队建设，提升团队整体绩效");
        System.out.println("  ═══════════════════════════════════════");
    }

    /**
     * 访问高管——生成其年度评估报告。
     *
     * @param employee 高管实例
     */
    @Override
    public void visit(ExecutiveEmployee employee) {
        System.out.println("  ═══════════════════════════════════════");
        System.out.println("  高管年度评估报告");
        System.out.println("  ═══════════════════════════════════════");
        System.out.println("  姓名：" + employee.getName());
        System.out.println("  岗位：高管");
        System.out.println("  基本工资：¥" + String.format("%.2f", employee.getBaseSalary()));
        System.out.println("  公司年度利润：¥" + String.format("%.2f", employee.getCompanyProfit()));
        System.out.println("  分红比例：" + employee.getProfitSharingPercentage() + "%");
        System.out.println("  年度分红：¥" + String.format("%.2f", employee.calculateProfitSharing()));
        System.out.println("  股票期权：¥" + String.format("%.2f", employee.getStockOptions()));
        System.out.println("  评估建议：聚焦公司战略发展，优化资源配置");
        System.out.println("  ═══════════════════════════════════════");
    }
}
