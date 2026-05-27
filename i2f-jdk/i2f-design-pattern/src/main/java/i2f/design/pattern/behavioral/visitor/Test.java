package i2f.design.pattern.behavioral.visitor;

import i2f.design.pattern.behavioral.visitor.emplyee.Employee;
import i2f.design.pattern.behavioral.visitor.emplyee.impl.ExecutiveEmployee;
import i2f.design.pattern.behavioral.visitor.emplyee.impl.ManagerEmployee;
import i2f.design.pattern.behavioral.visitor.emplyee.impl.StaffEmployee;
import i2f.design.pattern.behavioral.visitor.visitor.EmployeeVisitor;
import i2f.design.pattern.behavioral.visitor.visitor.impl.AnnualReportVisitor;
import i2f.design.pattern.behavioral.visitor.visitor.impl.SalaryCalculatorVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * 访问者模式 —— 调用演示
 *
 * <p>演示访问者模式的核心机制：客户端通过 {@link Employee#accept(EmployeeVisitor)} 方法
 * 将访问者传入对象结构，访问者针对不同类型的元素执行不同的操作，
 * 实现"在不修改元素类的前提下添加新操作"。</p>
 *
 * <p><b>双分派（Double Dispatch）演示：</b></p>
 * <pre>
 *  第一次分派：employee.accept(visitor)
 *    → 根据 employee 的具体类型（StaffEmployee/ManagerEmployee/ExecutiveEmployee）
 *      决定调用哪个 accept 实现
 *
 *  第二次分派：visitor.visit(this)
 *    → 根据 visitor 的具体类型（SalaryCalculatorVisitor/AnnualReportVisitor）
 *      决定调用哪个 visit 方法
 *
 *  结果：在运行时同时确定了 element 和 visitor 的具体类型
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 */
public class Test {
    public static void main(String[] args) {

        // ==================== 1. 创建对象结构（员工列表） ====================
        System.out.println("====== 访问者模式（Visitor）演示 ======");
        System.out.println("场景：公司人事系统——对不同类型的员工执行不同操作\n");

        List<Employee> employees = new ArrayList<>();

        // 添加普通员工
        employees.add(new StaffEmployee("张三", 8000, 20, 50));
        employees.add(new StaffEmployee("李四", 7500, 15, 50));

        // 添加经理
        employees.add(new ManagerEmployee("王经理", 15000, 8, 5000, 3000));

        // 添加高管
        employees.add(new ExecutiveEmployee("赵总", 30000, 2.0, 50000, 1000000));

        System.out.println("公司员工列表：");
        for (Employee emp : employees) {
            System.out.println("  " + emp);
        }
        System.out.println();

        // ==================== 2. 使用薪资计算器访问者 ====================
        System.out.println("────── 操作 1：计算全员薪资（SalaryCalculatorVisitor） ──────");
        SalaryCalculatorVisitor salaryVisitor = new SalaryCalculatorVisitor();

        // 遍历员工列表，每个员工接受访问者
        for (Employee emp : employees) {
            emp.accept(salaryVisitor);  // 双分派：emp 的具体类型 + visitor 的具体类型
        }

        System.out.println();
        System.out.printf("全员薪资总计：¥%.2f%n", salaryVisitor.getTotalSalary());
        System.out.println();

        // ==================== 3. 使用年度报告生成器访问者 ====================
        System.out.println("────── 操作 2：生成年度报告（AnnualReportVisitor） ──────");
        AnnualReportVisitor reportVisitor = new AnnualReportVisitor();

        // 同一个员工列表，使用不同的访问者执行完全不同的操作
        for (Employee emp : employees) {
            emp.accept(reportVisitor);  // 双分派：emp 的具体类型 + visitor 的具体类型
            System.out.println();
        }

        // ==================== 4. 验证开闭原则——新增操作无需修改元素类 ====================
        System.out.println("====== 验证：开闭原则（对扩展开放，对修改关闭） ======");
        System.out.println("场景：新增一个\"税务计算访问者\"，完全不需要修改任何员工类\n");

        // 模拟新增的税务计算访问者（匿名内部类演示）
        EmployeeVisitor taxVisitor = new EmployeeVisitor() {
            @Override
            public void visit(StaffEmployee employee) {
                double totalPay = employee.getBaseSalary() + employee.calculateOvertimePay();
                double tax = totalPay * 0.1;  // 假设税率 10%
                System.out.printf("  [普通员工] %s - 应纳税所得额：¥%.2f, 个人所得税：¥%.2f%n",
                        employee.getName(), totalPay, tax);
            }

            @Override
            public void visit(ManagerEmployee employee) {
                double totalPay = employee.getBaseSalary() + employee.getTeamBonus() + employee.getManagementAllowance();
                double tax = totalPay * 0.15;  // 假设税率 15%
                System.out.printf("  [经理] %s - 应纳税所得额：¥%.2f, 个人所得税：¥%.2f%n",
                        employee.getName(), totalPay, tax);
            }

            @Override
            public void visit(ExecutiveEmployee employee) {
                double totalPay = employee.getBaseSalary() + employee.calculateProfitSharing() + employee.getStockOptions();
                double tax = totalPay * 0.25;  // 假设税率 25%
                System.out.printf("  [高管] %s - 应纳税所得额：¥%.2f, 个人所得税：¥%.2f%n",
                        employee.getName(), totalPay, tax);
            }
        };

        System.out.println("────── 操作 3：计算个人所得税（TaxVisitor - 新增加的操作） ──────");
        for (Employee emp : employees) {
            emp.accept(taxVisitor);
        }
        System.out.println();

        // ==================== 5. 模式优势总结 ====================
        System.out.println("====== 访问者模式优势总结 ======");
        System.out.println("1. 遵循开闭原则：新增操作只需新增访问者类，无需修改已有元素类");
        System.out.println("2. 遵循单一职责：每个访问者只负责一种操作，逻辑集中易维护");
        System.out.println("3. 双分派机制：在运行时同时确定元素和访问者的具体类型，实现真正多态");
        System.out.println("4. 适用于\"对象结构稳定、操作频繁变化\"的场景");
        System.out.println("5. 访问者可以累积状态（如 SalaryCalculatorVisitor 累计总薪资）");
        
        System.out.println();
        System.out.println("====== 访问者模式局限 ======");
        System.out.println("1. 增加新的元素类很困难（需要修改所有访问者接口和实现）");
        System.out.println("2. 破坏封装（访问者需要访问元素的内部状态）");
        System.out.println("3. 适用于元素类型固定、操作类型多变的场景");
    }
}
