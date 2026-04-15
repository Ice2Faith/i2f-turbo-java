package i2f.workflow.rag;

/**
 * @author Ice2Faith
 * @date 2026/4/4 21:04
 * @desc
 */
public enum DagEdgeStatus {
    PENDING,
    SUCCESS,
    FAILURE,
    SKIP;

    public static DagEdgeStatus of(DagNodeStatus status) {
        if (status == DagNodeStatus.PENDING) {
            return DagEdgeStatus.PENDING;
        }
        if (status == DagNodeStatus.SUCCESS) {
            return DagEdgeStatus.SUCCESS;
        }
        if (status == DagNodeStatus.FAILURE) {
            return DagEdgeStatus.FAILURE;
        }
        if (status == DagNodeStatus.SKIP) {
            return DagEdgeStatus.SKIP;
        }
        if (status == DagNodeStatus.WAITING) {
            return DagEdgeStatus.SUCCESS;
        }
        return DagEdgeStatus.SUCCESS;
    }
}
