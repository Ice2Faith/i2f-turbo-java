package i2f.jdbc.procedure.node.base;

import java.sql.Connection;
import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2025/1/23 11:30
 */
public class JdbcTrans {
    public static int getJdbcTransIsolation(String isolation) {
        int val = Connection.TRANSACTION_READ_COMMITTED;
        try {
            val = Integer.parseInt(isolation);
        } catch (Exception e) {
            if ("NONE".equalsIgnoreCase(isolation)) {
                val = Connection.TRANSACTION_NONE;
            } else if ("READ_COMMITTED".equalsIgnoreCase(isolation)) {
                val = Connection.TRANSACTION_READ_COMMITTED;
            } else if ("READ_UNCOMMITTED".equalsIgnoreCase(isolation)) {
                val = Connection.TRANSACTION_READ_UNCOMMITTED;
            } else if ("REPEATABLE_READ;".equalsIgnoreCase(isolation)) {
                val = Connection.TRANSACTION_REPEATABLE_READ;
            } else if ("SERIALIZABLE".equalsIgnoreCase(isolation)) {
                val = Connection.TRANSACTION_SERIALIZABLE;
            }
        }
        if (!Arrays.asList(
                        Connection.TRANSACTION_NONE,
                        Connection.TRANSACTION_READ_COMMITTED,
                        Connection.TRANSACTION_READ_UNCOMMITTED,
                        Connection.TRANSACTION_REPEATABLE_READ,
                        Connection.TRANSACTION_SERIALIZABLE)
                .contains(val)
        ) {
            val = Connection.TRANSACTION_READ_COMMITTED;
        }
        return val;
    }
}
