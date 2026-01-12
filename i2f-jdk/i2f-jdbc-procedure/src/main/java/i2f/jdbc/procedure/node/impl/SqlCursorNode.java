package i2f.jdbc.procedure.node.impl;

import i2f.bindsql.BindSql;
import i2f.clock.SystemClock;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.cursor.JdbcCursor;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.base.SqlDialect;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.node.event.XmlExecUseTimeEvent;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.BreakSignalException;
import i2f.jdbc.procedure.signal.impl.ContinueSignalException;
import i2f.page.ApiPage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class SqlCursorNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.SQL_CURSOR;

    @Override
    public String tag() {
        return TAG_NAME;
    }


    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        List<XmlNode> children = node.getChildren();
        if (children == null || children.isEmpty()) {
            warnPoster.accept(TAG_NAME + " missing child element");
            return;
        }
        XmlNode queryNode = null;
        XmlNode bodyNode = null;
        for (XmlNode item : children) {
            if (TagConsts.SQL_QUERY_LIST.equals(item.getTagName())) {
                queryNode = item;
            }
            if (TagConsts.LANG_BODY.equals(item.getTagName())) {
                bodyNode = item;
            }
        }

        if (queryNode == null) {
            warnPoster.accept(TAG_NAME + "missing cursor query node " + SqlQueryListNode.TAG_NAME + " child element");
        }

        if (bodyNode == null) {
            warnPoster.accept(TAG_NAME + "missing cursor body node " + LangBodyNode.TAG_NAME + " child element");
        }
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        List<XmlNode> children = node.getChildren();
        if (children == null || children.isEmpty()) {
            return;
        }
        XmlNode queryNode = null;
        XmlNode bodyNode = null;
        for (XmlNode item : children) {
            if (TagConsts.SQL_QUERY_LIST.equals(item.getTagName())) {
                queryNode = item;
            }
            if (TagConsts.LANG_BODY.equals(item.getTagName())) {
                bodyNode = item;
            }
        }

        if (queryNode == null) {
            throw new IllegalStateException("missing cursor query node!");
        }

        if (bodyNode == null) {
            throw new IllegalStateException("missing cursor body node!");
        }

        Integer batchSize = executor.convertAs(executor.attrValue(AttrConsts.BATCH_SIZE, FeatureConsts.INT, node, context), Integer.class);
        String item = node.getTagAttrMap().get(AttrConsts.ITEM);
        boolean acceptBatch = executor.toBoolean(executor.attrValue(AttrConsts.ACCEPT_BATCH, FeatureConsts.BOOLEAN, node, context));
        if (item == null || item.isEmpty()) {
            item = AttrConsts.ITEM;
        }
        boolean useCursor = executor.toBoolean(executor.attrValue(AttrConsts.USE_CURSOR, FeatureConsts.BOOLEAN, node, context));


        String datasource = (String) executor.attrValue(AttrConsts.DATASOURCE, FeatureConsts.STRING, queryNode, context);
        BindSql bql = SqlDialect.getSqlDialectList(datasource, queryNode, context, executor);
        String resultTypeName = (String) executor.attrValue(AttrConsts.RESULT_TYPE, FeatureConsts.STRING, queryNode, context);
        Class<?> resultType = executor.loadClass(resultTypeName);
        if (resultType == null) {
            resultType = Map.class;
        }


        if (executor.isDebug()) {
            bql = bql.concat(getTrackingComment(node));
        }

        Map<String, Object> pointContext = new HashMap<>();
        String snapshotTraceId = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
        String location = getNodeLocation(node);
        boolean isDebugMode = executor.isDebug();
        pointContext.put(POINT_KEY_LOCATION, location);
        pointContext.put(POINT_KEY_IS_DEBUG_MODE, isDebugMode);
        pointContext.put(POINT_KEY_SNAPSHOT_TRACE_ID, snapshotTraceId);

        int loopIndex = 0;
        boolean loopIsFirst = true;

        int pageIndex = 0;
        if (batchSize == null || batchSize <= 0) {
            batchSize = 2000;
        }
        Map<String, Object> bakParams = new LinkedHashMap<>();
        bakParams.put(item, executor.visit(item, context));
        JdbcCursor<?> cursor = null;
        try {
            if (useCursor) {
                Connection conn = executor.getConnection(datasource, context);
                try {
                    int cursorFetchSize = batchSize;
                    cursor = JdbcResolver.cursor(conn, bql, resultType, (nn, ql) -> {
                        return JdbcResolver.buildCursorStatement(nn, ql, cursorFetchSize);
                    });
                } catch (SQLException e) {
                    throw new IllegalStateException(e.getMessage(), e);
                }
            }
            while (true) {
                List<?> list = null;
                if (useCursor) {
                    try {
                        list = cursor.nextCount(batchSize);
                        if (list.isEmpty()) {
                            if (executor.isDebug()) {
                                executor.logger().logDebug("no data found! at " + getNodeLocation(node));
                            }
                            break;
                        }
                    } catch (SQLException e) {
                        throw new IllegalStateException(e.getMessage(), e);
                    }
                } else {
                    list = executor.sqlQueryPage(datasource, bql, context, resultType, new ApiPage(pageIndex, batchSize));
                    if (list.isEmpty()) {
                        if (executor.isDebug()) {
                            executor.logger().logDebug("no data found! at " + getNodeLocation(node));
                        }
                        break;
                    }
                }

                if (executor.isDebug()) {
                    executor.logger().logDebug("found batch data! at " + getNodeLocation(node));
                }

                if (acceptBatch) {
                    long bts = SystemClock.currentTimeMillis();

                    pointContext.put(POINT_KEY_BEGIN_TS, bts);
                    pointContext.put(POINT_KEY_LOOP_IS_FIRST, loopIsFirst);
                    pointContext.put(POINT_KEY_LOOP_INDEX, loopIndex);
                    pointContext.put(POINT_KEY_LOCATION, location + "@" + loopIndex);

                    pointContext.put(POINT_KEY_LOOP_VALUE, list);

                    loopIndex++;
                    loopIsFirst = false;
                    try {
                        executor.visitSet(context, item, list);
                        executor.execAsProcedure(bodyNode, context, false, false);
                    } catch (ContinueSignalException e) {
                        continue;
                    } catch (BreakSignalException e) {
                        break;
                    } finally {
                        long ets = SystemClock.currentTimeMillis();
                        long useTs = ets - bts;
                        XmlExecUseTimeEvent event = new XmlExecUseTimeEvent();
                        event.setExecutorNode(this);
                        event.setPointContext(pointContext);
                        event.setNode(node);
                        event.setContext(context);
                        event.setExecutor(executor);
                        executor.sendEvent(event);
                        event.setUseTs(useTs);
                        executor.publishEvent(event);
                    }
                } else {
                    boolean breakSignal = false;
                    int count = 0;
                    for (Object obj : list) {
                        long bts = SystemClock.currentTimeMillis();

                        pointContext.put(POINT_KEY_BEGIN_TS, bts);
                        pointContext.put(POINT_KEY_LOOP_IS_FIRST, loopIsFirst);
                        pointContext.put(POINT_KEY_LOOP_INDEX, loopIndex);
                        pointContext.put(POINT_KEY_LOCATION, location + "@" + loopIndex);

                        pointContext.put(POINT_KEY_LOOP_VALUE, obj);

                        loopIndex++;
                        loopIsFirst = false;

                        count++;
                        try {
                            executor.visitSet(context, item, obj);
                            executor.execAsProcedure(bodyNode, context, false, false);
                        } catch (ContinueSignalException e) {
                            continue;
                        } catch (BreakSignalException e) {
                            breakSignal = true;
                            break;
                        } finally {
                            long ets = SystemClock.currentTimeMillis();
                            long useTs = ets - bts;
                            XmlExecUseTimeEvent event = new XmlExecUseTimeEvent();
                            event.setExecutorNode(this);
                            event.setPointContext(pointContext);
                            event.setNode(node);
                            event.setContext(context);
                            event.setExecutor(executor);
                            executor.sendEvent(event);
                            event.setUseTs(useTs);
                            executor.publishEvent(event);
                        }
                    }

                    if (breakSignal) {
                        break;
                    }

                    if (count < batchSize) {
                        break;
                    }
                }

                pageIndex++;
            }
        } finally {
            if (acceptBatch) {
                executor.visitDelete(context, item);
            }
            executor.visitDelete(context, item);
            if (cursor != null) {
                try {
                    cursor.dispose();
                } catch (SQLException e) {
                    throw new IllegalStateException(e.getMessage(), e);
                }
            }
            for (Map.Entry<String, Object> entry : bakParams.entrySet()) {
                executor.visitSet(context, entry.getKey(), entry.getValue());
            }
        }


    }
}
