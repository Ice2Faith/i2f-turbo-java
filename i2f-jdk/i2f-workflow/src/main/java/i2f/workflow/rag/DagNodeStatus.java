package i2f.workflow.rag;

/**
 * @author Ice2Faith
 * @date 2026/4/4 21:04
 * @desc
 */
public enum DagNodeStatus {
    PENDING,
    WAITING,
    CONTINUE,
    SUCCESS,
    FAILURE,
    SKIP
}
