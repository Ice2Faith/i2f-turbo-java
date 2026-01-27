package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.ParamsConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.base.JdbcTrans;
import i2f.jdbc.procedure.node.base.MatchException;
import i2f.jdbc.procedure.node.base.Propagation;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.ControlSignalException;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class SqlTransactionalNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.SQL_TRANSACTIONAL;

    @Override
    public String tag() {
        return TAG_NAME;
    }


    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        String datasources = (String) executor.attrValue(AttrConsts.DATASOURCES, FeatureConsts.STRING, node, context);
        String propagationStr = (String) executor.attrValue(AttrConsts.PROPAGATION, FeatureConsts.STRING, node, context);
        String isolationStr = (String) executor.attrValue(AttrConsts.ISOLATION, FeatureConsts.STRING, node, context);
        boolean readOnly = executor.toBoolean(executor.attrValue(AttrConsts.READ_ONLY, FeatureConsts.BOOLEAN, node, context));
        String rollbackFor = (String) executor.attrValue(AttrConsts.ROLLBACK_FOR, FeatureConsts.STRING, node, context);
        String noRollbackFor = (String) executor.attrValue(AttrConsts.NO_ROLLBACK_FOR, FeatureConsts.STRING, node, context);

        Propagation propagation = JdbcTrans.getJdbcTransPropagation(propagationStr);
        int isolation = JdbcTrans.getJdbcTransIsolation(isolationStr);

        if (datasources == null) {
            datasources = "";
        }
        datasources = datasources.trim();
        if (datasources.isEmpty()) {
            datasources = ParamsConsts.DEFAULT_DATASOURCE;
        }
        if (rollbackFor == null) {
            rollbackFor = "";
        }
        rollbackFor = rollbackFor.trim();
        List<Class<?>> rollbackClasses = new ArrayList<>();
        if (rollbackFor != null) {
            String[] arr = rollbackFor.split("[,;\\|]");
            for (String item : arr) {
                item = item.trim();
                try {
                    Class<?> clazz = executor.loadClass(item);
                    if (clazz != null) {
                        rollbackClasses.add(clazz);
                    }
                } catch (Throwable e) {

                }
            }
        }
        if (noRollbackFor == null) {
            noRollbackFor = "";
        }
        noRollbackFor = noRollbackFor.trim();
        List<Class<?>> noRollbackClasses = new ArrayList<>();
        if (noRollbackFor != null) {
            String[] arr = noRollbackFor.split("[,;\\|]");
            for (String item : arr) {
                item = item.trim();
                try {
                    Class<?> clazz = executor.loadClass(item);
                    if (clazz != null) {
                        noRollbackClasses.add(clazz);
                    }
                } catch (Throwable e) {

                }
            }
        }

        Set<String> datasourceArr = new LinkedHashSet<>(Arrays.asList(datasources.split(",|;")));
        Map<String, Connection> oldConnectMap = executor.visitAs(ParamsConsts.CONNECTIONS, context);
        Map<String, Boolean> oldReadOnyMap = new HashMap<>();
        Map<String, Integer> oldIsolationMap = new HashMap<>();
        Map<String, Boolean> oldAutoCommitMap = new HashMap<>();


        Map<String, Connection> workConnectMap = new HashMap<>();
        executor.visitSet(context, ParamsConsts.CONNECTIONS, workConnectMap);
        try {
            for (Map.Entry<String, Connection> entry : oldConnectMap.entrySet()) {
                oldReadOnyMap.put(entry.getKey(), entry.getValue().isReadOnly());
                oldIsolationMap.put(entry.getKey(), entry.getValue().getTransactionIsolation());
                oldAutoCommitMap.put(entry.getKey(), entry.getValue().getAutoCommit());
            }

            for (String item : datasourceArr) {
                item = item.trim();
                if (item.isEmpty()) {
                    continue;
                }

                if (Propagation.REQUIRED == propagation) {
                    // 支持当前事务，如果当前没有事务，就新建一个事务。这是最常见的选择
                    Connection conn = oldConnectMap.get(item);
                    if (conn == null) {
                        conn = executor.getConnection(item, context);
                    }
                    if (conn.getAutoCommit()) {
                        conn.setAutoCommit(false);
                    }
                    workConnectMap.put(item, conn);
                } else if (Propagation.SUPPORTS == propagation) {
                    // 支持当前事务，如果当前没有事务，就以非事务方式执行
                    // do nothing

                } else if (Propagation.MANDATORY == propagation) {
                    // 支持当前事务，如果当前没有事务，就抛出异常
                    Connection conn = oldConnectMap.get(item);
                    boolean isNew = false;
                    if (conn == null) {
                        conn = executor.getConnection(item, context);
                        isNew = true;
                    }
                    workConnectMap.put(item, conn);
                    if (isNew) {
                        conn.setAutoCommit(false);
                    } else {
                        if (conn.getAutoCommit()) {
                            throw new ThrowSignalException("connection propagation is " + propagation + ", but connection not open transaction");
                        }
                    }
                } else if (Propagation.REQUIRES_NEW == propagation) {
                    // 新建事务，如果当前存在事务，把当前事务挂起
                    Connection conn = executor.getConnection(item, context);
                    conn.setAutoCommit(false);
                    workConnectMap.put(item, conn);
                } else if (Propagation.NOT_SUPPORTED == propagation) {
                    // 以非事务方式执行操作，如果当前存在事务，就把当前事务挂起
                    Connection conn = oldConnectMap.get(item);
                    if (conn != null) {
                        if (!conn.getAutoCommit()) {
                            conn = executor.getConnection(item, context);
                        }
                        workConnectMap.put(item, conn);
                    }
                } else if (Propagation.NEVER == propagation) {
                    // 以非事务方式执行，如果当前存在事务，则抛出异常
                    Connection conn = oldConnectMap.get(item);
                    if (conn != null) {
                        workConnectMap.put(item, conn);
                        if (!conn.getAutoCommit()) {
                            throw new ThrowSignalException("connection propagation is " + propagation + ", but connection has open transaction");
                        }
                    }
                } else if (Propagation.NESTED == propagation) {
                    // 支持当前事务，如果当前事务存在，则执行一个嵌套事务，如果当前没有事务，就新建一个事务
                    Connection conn = oldConnectMap.get(item);
                    if (conn == null) {
                        conn = executor.getConnection(item, context);
                    }
                    if (conn.getAutoCommit()) {
                        conn.setAutoCommit(false);
                    }
                    workConnectMap.put(item, conn);
                }

                Connection conn = workConnectMap.get(item);
                conn.setTransactionIsolation(isolation);
                conn.setReadOnly(readOnly);
            }

            executor.execAsProcedure(node, context, false, false);

            for (Map.Entry<String, Connection> work : workConnectMap.entrySet()) {
                String key = work.getKey();
                Connection conn = work.getValue();
                if (!conn.getAutoCommit()) {
                    conn.commit();
                }
            }
        } catch (Throwable e) {
            if (e instanceof ControlSignalException) {
                for (Map.Entry<String, Connection> work : workConnectMap.entrySet()) {
                    String key = work.getKey();
                    Connection conn = work.getValue();
                    try {
                        if (!conn.getAutoCommit()) {
                            conn.commit();
                        }
                    } catch (SQLException ex) {

                    }
                }
                return;
            }


            Map.Entry<Throwable, Boolean> noRollBackMatched = MatchException.matchException(e, node.getAttrFeatureMap().get(AttrConsts.NO_ROLLBACK_FOR), noRollbackClasses);
            if (noRollBackMatched.getValue()) {
                for (Map.Entry<String, Connection> work : workConnectMap.entrySet()) {
                    String key = work.getKey();
                    Connection conn = work.getValue();
                    try {
                        if (!conn.getAutoCommit()) {
                            conn.commit();
                        }
                    } catch (SQLException ex) {

                    }
                }
                return;
            }
            Map.Entry<Throwable, Boolean> rollBackMatched = MatchException.matchException(e, node.getAttrFeatureMap().get(AttrConsts.ROLLBACK_FOR), rollbackClasses);
            if (rollBackMatched.getValue()) {
                for (Map.Entry<String, Connection> work : workConnectMap.entrySet()) {
                    String key = work.getKey();
                    Connection conn = work.getValue();
                    try {
                        if (!conn.getAutoCommit()) {
                            conn.rollback();
                        }
                    } catch (SQLException ex) {

                    }
                }
                return;
            }
        } finally {
            for (Map.Entry<String, Connection> work : workConnectMap.entrySet()) {
                String key = work.getKey();
                Connection conn = work.getValue();
                Connection old = oldConnectMap.get(key);
                if (old == null || old == conn) {
                    executor.closeConnection(conn, key, false, null);
                }
            }


            for (Map.Entry<String, Connection> entry : oldConnectMap.entrySet()) {
                try {
                    entry.getValue().setReadOnly(oldReadOnyMap.get(entry.getKey()));
                    entry.getValue().setTransactionIsolation(oldIsolationMap.get(entry.getKey()));
                    entry.getValue().setAutoCommit(oldAutoCommitMap.get(entry.getKey()));
                } catch (SQLException e) {

                }
            }

            executor.visitSet(context, ParamsConsts.CONNECTIONS, oldConnectMap);
        }
    }
}
