package i2f.workflow.rag;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/4/4 21:23
 * @desc
 */
public class DagInboundGateways {
    public static DagStatus allSuccess(List<DagInboundNode> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            return DagStatus.SUCCESS;
        }
        for (DagInboundNode inboundNode : nodes) {
            DagStatus edgeStatus = inboundNode.getEdgeStatus();
            if (edgeStatus == DagStatus.PENDING) {
                return DagStatus.PENDING;
            }
            if (edgeStatus == DagStatus.FAILURE) {
                return DagStatus.FAILURE;
            }
        }
        return DagStatus.SUCCESS;
    }

    public static DagStatus anySuccess(List<DagInboundNode> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            return DagStatus.SUCCESS;
        }
        for (DagInboundNode inboundNode : nodes) {
            DagStatus edgeStatus = inboundNode.getEdgeStatus();
            if (edgeStatus == DagStatus.PENDING) {
                return DagStatus.PENDING;
            }
            if (edgeStatus == DagStatus.SUCCESS) {
                return DagStatus.SUCCESS;
            }
        }
        return DagStatus.FAILURE;
    }

    public static DagInboundGateway leastSuccessCount(int count) {
        return (nodes) -> {
            if (nodes == null || nodes.isEmpty()) {
                return DagStatus.SUCCESS;
            }
            boolean hasPending = false;
            int cnt = 0;
            for (DagInboundNode inboundNode : nodes) {
                DagStatus edgeStatus = inboundNode.getEdgeStatus();
                if (edgeStatus == DagStatus.PENDING) {
                    hasPending = true;
                }
                if (edgeStatus == DagStatus.SUCCESS) {
                    cnt++;
                    if (cnt >= count) {
                        return DagStatus.SUCCESS;
                    }
                }
            }
            if (hasPending) {
                return DagStatus.PENDING;
            }
            return DagStatus.FAILURE;
        };
    }
}
