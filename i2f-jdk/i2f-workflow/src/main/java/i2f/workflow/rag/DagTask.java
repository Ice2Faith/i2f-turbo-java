package i2f.workflow.rag;

import java.util.List;

/**
 * 任务执行节点
 *
 * @author Ice2Faith
 * @date 2026/4/4 21:24
 * @desc
 */
@FunctionalInterface
public interface DagTask<T> {
    T call(List<DagInboundNode> nodes) throws Throwable;
}
