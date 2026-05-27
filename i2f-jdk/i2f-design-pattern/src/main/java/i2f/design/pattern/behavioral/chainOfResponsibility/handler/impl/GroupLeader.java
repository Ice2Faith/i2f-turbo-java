package i2f.design.pattern.behavioral.chainOfResponsibility.handler.impl;

import i2f.design.pattern.behavioral.chainOfResponsibility.handler.ApprovalHandler;
import i2f.design.pattern.behavioral.chainOfResponsibility.request.ApprovalRequest;

/**
 * 责任链模式 —— 组长审批者（Concrete Handler：GroupLeader）
 *
 * <p><b>角色：</b>具体处理者（Concrete Handler）</p>
 *
 * <p><b>说明：</b>组长是审批链中的第一级处理者，有权审批 ≤ 3 天的请假申请。
 * 如果请假天数超过 3 天，则将请求传递给链中的下一个处理者（部门经理）。</p>
 *
 * <p><b>开闭原则体现：</b>如果未来需要新增"项目经理"审批层级，
 * 只需新增 {@code ProjectManagerHandler} 类并插入到责任链中，
 * 无需修改现有任何处理者代码。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 11:00
 * @see ApprovalHandler
 * @see ApprovalRequest
 */
public class GroupLeader extends ApprovalHandler {

    /**
     * 组长可审批的最大请假天数。
     */
    private static final int MAX_LEAVE_DAYS = 3;

    /**
     * 处理审批请求。
     *
     * <p>如果请假天数 ≤ 3 天，组长直接审批通过；
     * 否则将请求传递给下一个处理者。</p>
     *
     * @param request 审批请求
     * @return true 表示请求已被处理，false 表示请求已传递给下一个处理者
     */
    @Override
    public boolean handle(ApprovalRequest request) {
        System.out.println("  📋 组长审批：" + getHandlerName());

        if (request.getLeaveDays() <= MAX_LEAVE_DAYS) {
            System.out.println("  ✅ 审批通过（请假 " + request.getLeaveDays() + " 天 ≤ " + MAX_LEAVE_DAYS + " 天，组长权限范围内）");
            System.out.println("  📝 审批意见：同意，注意休息！");
            return true;
        }

        System.out.println("  ⏭️  超出权限（请假 " + request.getLeaveDays() + " 天 > " + MAX_LEAVE_DAYS + " 天），传递给上级处理");
        return handleNext(request);
    }

    @Override
    public String getHandlerName() {
        return "组长 - 张三";
    }
}