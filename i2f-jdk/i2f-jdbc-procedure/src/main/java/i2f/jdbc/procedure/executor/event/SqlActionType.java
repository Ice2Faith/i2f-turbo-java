package i2f.jdbc.procedure.executor.event;

/**
 * @author Ice2Faith
 * @date 2026/2/6 15:58
 * @desc
 */
public enum SqlActionType {
    QUERY_LIST,
    QUERY_COLUMNS,
    QUERY_OBJECT,
    QUERY_ROW,
    UPDATE,
    QUERY_PAGE,
    TRANS_BEGIN,
    TRANS_COMMIT,
    TRANS_ROLLBACK,
    TRANS_NONE
}
