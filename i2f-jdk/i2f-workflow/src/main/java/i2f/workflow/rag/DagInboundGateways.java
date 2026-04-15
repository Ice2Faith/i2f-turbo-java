package i2f.workflow.rag;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/4/4 21:23
 * @desc
 */
public class DagInboundGateways {
    public static DagNodeStatus allSuccess(List<DagInboundNode> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            return DagNodeStatus.SUCCESS;
        }
        for (DagInboundNode inboundNode : nodes) {
            DagEdgeStatus edgeStatus = inboundNode.getEdgeStatus();
            if (edgeStatus == DagEdgeStatus.PENDING) {
                return DagNodeStatus.PENDING;
            }
            if (edgeStatus == DagEdgeStatus.FAILURE || edgeStatus == DagEdgeStatus.SKIP) {
                return DagNodeStatus.FAILURE;
            }
        }
        return DagNodeStatus.SUCCESS;
    }

    public static DagNodeStatus anySuccess(List<DagInboundNode> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            return DagNodeStatus.SUCCESS;
        }
        for (DagInboundNode inboundNode : nodes) {
            DagEdgeStatus edgeStatus = inboundNode.getEdgeStatus();
            if (edgeStatus == DagEdgeStatus.PENDING) {
                return DagNodeStatus.PENDING;
            }
            if (edgeStatus == DagEdgeStatus.SKIP) {
                continue;
            }
            if (edgeStatus == DagEdgeStatus.SUCCESS) {
                return DagNodeStatus.SUCCESS;
            }
        }
        return DagNodeStatus.FAILURE;
    }

    public static DagInboundGateway leastSuccessCount(int count) {
        return (nodes) -> {
            if (nodes == null || nodes.isEmpty()) {
                return DagNodeStatus.SUCCESS;
            }
            boolean hasPending = false;
            int cnt = 0;
            for (DagInboundNode inboundNode : nodes) {
                DagEdgeStatus edgeStatus = inboundNode.getEdgeStatus();
                if (edgeStatus == DagEdgeStatus.PENDING) {
                    hasPending = true;
                }
                if (edgeStatus == DagEdgeStatus.SKIP) {
                    continue;
                }
                if (edgeStatus == DagEdgeStatus.SUCCESS) {
                    cnt++;
                    if (cnt >= count) {
                        return DagNodeStatus.SUCCESS;
                    }
                }
            }
            if (hasPending) {
                return DagNodeStatus.PENDING;
            }
            return DagNodeStatus.FAILURE;
        };
    }
}
