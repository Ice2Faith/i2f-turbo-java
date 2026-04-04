package i2f.workflow.rag;

import java.util.List;

/**
 * 节点入站网关
 * 用于在节点执行前，用于判断目前的节点状态，判断是否需要执行
 * 返回 SUCCESS 就表示允许执行
 *
 * @author Ice2Faith
 * @date 2026/4/4 21:24
 * @desc
 */
@FunctionalInterface
public interface DagInboundGateway {
    DagStatus test(List<DagInboundNode> nodes);
}
