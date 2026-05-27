package i2f.design.pattern.behavioral.chainOfResponsibility.handler.impl;

import i2f.design.pattern.behavioral.chainOfResponsibility.handler.ApprovalHandler;
import i2f.design.pattern.behavioral.chainOfResponsibility.request.ApprovalRequest;

/**
 * 责任链模式 —— 部门经理审批者（Concrete Handler：DepartmentManager）
 *
 * <p><b>角色：</b>具体处理者（Concrete Handler）</p>
 *
 * <p><b>说明：</b>部门经理是审批链中的第二级处理者，有权审批 ≤ 7 天的请假申请。
 * 如果请假天数超过 7 天，则将请求传递给链中的下一个处理者（总经理）。</p>
 *
 * <p><b>职责分离体现：</b>每个处理者只关注自己权限范围内的请求，
 * 超出权限的请求自动传递给上级，符合单一职责原则。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 11:00
 * @see ApprovalHandler
 * @see ApprovalRequest
 */
public class DepartmentManager extends ApprovalHandler {

    /**
     * 部门经理可审批的最大请假天数。
     */
    private static final int MAX_LEAVE_DAYS = 7;

    /**
     * 处理审批请求。
     *
     * <p>如果请假天数 ≤ 7 天，部门经理直接审批通过；
     * 否则将请求传递给下一个处理者。</p>
     *
     * @param request 审批请求
     * @return true 表示请求已被处理，false 表示请求已传递给下一个处理者
     */
    @Override
    public boolean handle(ApprovalRequest request) {
        System.out.println("  📋 部门经理审批：" + getHandlerName());

        if (request.getLeaveDays() <= MAX_LEAVE_DAYS) {
            System.out.println("  ✅ 审批通过（请假 " + request.getLeaveDays() + " 天 ≤ " + MAX_LEAVE_DAYS + " 天，部门经理权限范围内）");
            System.out.println("  📝 审批意见：同意，安排好工作交接！");
            return true;
        }

        System.out.println("  ⏭️  超出权限（请假 " + request.getLeaveDays() + " 天 > " + MAX_LEAVE_DAYS + " 天），传递给上级处理");
        return handleNext(request);
    }

    @Override
    public String getHandlerName() {
        return "部门经理 - 李四";
    }
}