package i2f.design.pattern.behavioral.chainOfResponsibility;

import i2f.design.pattern.behavioral.chainOfResponsibility.handler.ApprovalHandler;
import i2f.design.pattern.behavioral.chainOfResponsibility.handler.impl.DepartmentManager;
import i2f.design.pattern.behavioral.chainOfResponsibility.handler.impl.GeneralManager;
import i2f.design.pattern.behavioral.chainOfResponsibility.handler.impl.GroupLeader;
import i2f.design.pattern.behavioral.chainOfResponsibility.request.ApprovalRequest;

/**
 * 责任链模式 —— 调用演示
 *
 * <p>演示责任链模式的核心机制：请求沿着处理者链依次传递，
 * 直到有一个处理者能够处理该请求为止。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 11:00
 */
public class Test {
    public static void main(String[] args) {

        // ==================== 1. 构建责任链 ====================
        System.out.println("====== 责任链模式（Chain of Responsibility）演示 ======");
        System.out.println("场景：员工请假审批流程（组长 → 部门经理 → 总经理）");
        System.out.println("      请求沿着审批链传递，直到有权限的处理者审批通过\n");

        // 创建处理者
        ApprovalHandler groupLeader = new GroupLeader();
        ApprovalHandler deptManager = new DepartmentManager();
        ApprovalHandler generalManager = new GeneralManager();

        // 构建责任链：组长 → 部门经理 → 总经理
        groupLeader.setNext(deptManager).setNext(generalManager);

        // ==================== 2. 测试 1：组长权限范围内（2 天）====================
        System.out.println("────── 测试 1：请假 2 天（组长可审批） ──────");
        ApprovalRequest request1 = new ApprovalRequest("员工小明", 2, "个人事务");
        System.out.println("提交请求：" + request1);
        boolean handled1 = groupLeader.handle(request1);
        System.out.println("处理结果：" + (handled1 ? "✅ 已处理" : "❌ 未处理"));
        System.out.println();

        // ==================== 3. 测试 2：部门经理权限范围内（5 天）====================
        System.out.println("────── 测试 2：请假 5 天（需部门经理审批） ──────");
        ApprovalRequest request2 = new ApprovalRequest("员工小红", 5, "探亲访友");
        System.out.println("提交请求：" + request2);
        boolean handled2 = groupLeader.handle(request2);
        System.out.println("处理结果：" + (handled2 ? "✅ 已处理" : "❌ 未处理"));
        System.out.println();

        // ==================== 4. 测试 3：总经理权限范围内（15 天）====================
        System.out.println("────── 测试 3：请假 15 天（需总经理审批） ──────");
        ApprovalRequest request3 = new ApprovalRequest("员工小李", 15, "出国旅游");
        System.out.println("提交请求：" + request3);
        boolean handled3 = groupLeader.handle(request3);
        System.out.println("处理结果：" + (handled3 ? "✅ 已处理" : "❌ 未处理"));
        System.out.println();

        // ==================== 5. 测试 4：超出所有处理者权限（45 天）====================
        System.out.println("────── 测试 4：请假 45 天（超出最大权限） ──────");
        ApprovalRequest request4 = new ApprovalRequest("员工小王", 45, "长期休假");
        System.out.println("提交请求：" + request4);
        boolean handled4 = groupLeader.handle(request4);
        System.out.println("处理结果：" + (handled4 ? "✅ 已处理" : "❌ 未处理"));
        System.out.println();

        // ==================== 6. 验证责任链动态组装 ====================
        System.out.println("====== 验证：责任链可动态组装 ======");
        System.out.println("场景：临时调整审批流程（跳过部门经理，组长直接上报总经理）\n");

        // 重新构建责任链：组长 → 总经理（跳过部门经理）
        ApprovalHandler shortChainLeader = new GroupLeader();
        ApprovalHandler shortChainGeneral = new GeneralManager();
        shortChainLeader.setNext(shortChainGeneral);

        ApprovalRequest request5 = new ApprovalRequest("员工小张", 10, "紧急事务");
        System.out.println("提交请求：" + request5);
        boolean handled5 = shortChainLeader.handle(request5);
        System.out.println("处理结果：" + (handled5 ? "✅ 已处理" : "❌ 未处理"));
        System.out.println();

        // ==================== 7. 模式优势总结 ====================
        System.out.println("====== 责任链模式优势总结 ======");
        System.out.println("1. 降低耦合度：请求发送者无需知道哪个处理者会处理请求");
        System.out.println("2. 增强灵活性：可动态调整责任链的顺序和组成");
        System.out.println("3. 符合开闭原则：新增处理者只需新增类并插入链中，无需修改已有代码");
        System.out.println("4. 符合单一职责：每个处理者只关注自己权限范围内的请求");
        System.out.println("5. 简化对象交互：对象只需持有下一个处理者的引用，无需知道整个链的结构");
    }
}