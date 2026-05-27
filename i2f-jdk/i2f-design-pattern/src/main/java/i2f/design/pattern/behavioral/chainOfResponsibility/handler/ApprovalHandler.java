package i2f.design.pattern.behavioral.chainOfResponsibility.handler;

import i2f.design.pattern.behavioral.chainOfResponsibility.request.ApprovalRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 责任链模式 —— 审批处理器（Handler：ApprovalHandler）
 *
 * <p><b>角色：</b>抽象处理者（Abstract Handler）</p>
 *
 * <p><b>模式说明：</b>定义处理请求的接口，并持有指向下一个处理者的引用。
 * 每个具体处理者决定是否处理该请求，如果不能处理，则将请求传递给链中的下一个处理者。
 * 这就是责任链模式的核心：<b>"请求沿着链传递，直到有一个对象处理它为止"</b>。</p>
 *
 * <p><b>命名立意：</b>以"请假审批流程"为场景——
 * 员工提交请假申请后，请求会沿着审批链依次传递：
 * 组长（GroupLeader）→ 部门经理（DepartmentManager）→ 总经理（GeneralManager）。
 * 每个审批者根据请假天数判断是否能处理，不能处理则传递给上级。</p>
 *
 * <p><b>与工厂方法的区别：</b></p>
 * <pre>
 *  工厂方法（Factory Method）              责任链（Chain of Responsibility）
 *  ─────────────────────────────────────   ─────────────────────────────────────
 *  关注对象的创建                            关注请求的处理
 *  一个工厂创建一种产品                      多个处理者依次处理同一请求
 *  客户端主动调用工厂方法                    请求自动在链中传递
 * </pre>
 *
 * <p><b>模式结构：</b></p>
 * <pre>
 *  Handler（抽象处理者）
 *    └─ nextHandler: ApprovalHandler        ← 下一个处理者
 *    └─ setNext(handler): void              ← 设置下一个处理者
 *    └─ handle(request): boolean            ← 处理请求（抽象方法）
 *
 *  ConcreteHandler（具体处理者）
 *    ├─ GroupLeader         → 处理 ≤ 3 天的请假
 *    ├─ DepartmentManager   → 处理 ≤ 7 天的请假
 *    └─ GeneralManager      → 处理 ≤ 30 天的请假
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 11:00
 * @see ApprovalRequest
 */
@Data
@NoArgsConstructor
public abstract class ApprovalHandler {

    /**
     * 下一个处理者（链中的下一个节点）。
     */
    private ApprovalHandler nextHandler;

    /**
     * 设置链中的下一个处理者。
     *
     * <p>支持链式调用，便于构建责任链。</p>
     *
     * @param nextHandler 下一个处理者
     * @return 当前处理者（支持链式调用）
     */
    public ApprovalHandler setNext(ApprovalHandler nextHandler) {
        this.nextHandler = nextHandler;
        return this;
    }

    /**
     * 处理审批请求。
     *
     * <p>具体处理者实现此方法，决定是否处理该请求。
     * 如果不能处理，应调用 {@link #handleNext(ApprovalRequest)} 将请求传递给下一个处理者。</p>
     *
     * @param request 审批请求
     * @return true 表示请求已被处理，false 表示请求未被处理（已传递给下一个处理者）
     */
    public abstract boolean handle(ApprovalRequest request);

    /**
     * 将请求传递给链中的下一个处理者。
     *
     * <p>如果当前处理者无法处理请求，调用此方法将请求继续传递。
     * 如果链已到末尾（nextHandler 为 null），则请求未被处理。</p>
     *
     * @param request 审批请求
     * @return true 表示请求已被后续处理者处理，false 表示链中无更多处理者
     */
    protected boolean handleNext(ApprovalRequest request) {
        if (nextHandler != null) {
            return nextHandler.handle(request);
        }
        System.out.println("  ⚠️  请求未被处理（责任链已到末尾）");
        return false;
    }

    /**
     * 获取处理者名称。
     *
     * @return 处理者名称
     */
    public abstract String getHandlerName();
}