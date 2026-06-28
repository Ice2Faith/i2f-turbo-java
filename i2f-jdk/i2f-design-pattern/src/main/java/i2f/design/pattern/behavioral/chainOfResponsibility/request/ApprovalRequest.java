package i2f.design.pattern.behavioral.chainOfResponsibility.request;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 责任链模式 —— 审批请求（Request：ApprovalRequest）
 *
 * <p><b>角色：</b>请求对象（Request Object）</p>
 *
 * <p><b>说明：</b>封装需要在责任链中传递的请求数据。
 * 在请假审批场景中，包含申请人姓名、请假天数和请假事由。</p>
 *
 * <p><b>命名立意：</b>以"请假申请"为请求对象——
 * 员工提交请假申请后，该请求对象会沿着审批链依次传递，
 * 每个审批者根据请假天数判断是否有权限处理。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 11:00
 */
@Data
@NoArgsConstructor
public class ApprovalRequest {

    /**
     * 申请人姓名。
     */
    private String applicantName;

    /**
     * 请假天数。
     */
    private int leaveDays;

    /**
     * 请假事由。
     */
    private String reason;

    /**
     * 构造审批请求。
     *
     * @param applicantName 申请人姓名
     * @param leaveDays     请假天数
     * @param reason        请假事由
     */
    public ApprovalRequest(String applicantName, int leaveDays, String reason) {
        this.applicantName = applicantName;
        this.leaveDays = leaveDays;
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "请假申请{" +
                "申请人='" + applicantName + '\'' +
                ", 请假天数=" + leaveDays +
                ", 事由='" + reason + '\'' +
                '}';
    }
}