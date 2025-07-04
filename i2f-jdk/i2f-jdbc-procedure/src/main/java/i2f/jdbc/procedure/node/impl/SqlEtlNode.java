package i2f.jdbc.procedure.node.impl;

import i2f.bindsql.BindSql;
import i2f.convert.obj.ObjectConvertor;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.cursor.JdbcCursor;
import i2f.jdbc.data.QueryColumn;
import i2f.jdbc.data.QueryResult;
import i2f.jdbc.data.TypedArgument;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.ParamsConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.base.SqlDialect;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;
import i2f.page.ApiPage;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Date;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class SqlEtlNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.SQL_ETL;

    @Override
    public boolean support(XmlNode node) {
        if (XmlNode.NodeType.ELEMENT != node.getNodeType()) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        List<XmlNode> children = node.getChildren();
        if (children == null || children.isEmpty()) {
            warnPoster.accept(TAG_NAME + " missing child element");
            return;
        }
        XmlNode queryNode = null;
        XmlNode extraNode = null;
        List<XmlNode> transformNodeList = new ArrayList<>();
        XmlNode loadNode = null;
        XmlNode beforeNode = null;
        XmlNode afterNode = null;
        for (XmlNode item : children) {
            if (TagConsts.SQL_QUERY_LIST.equals(item.getTagName())) {
                queryNode = item;
            }
            if (TagConsts.ETL_EXTRA.equals(item.getTagName())) {
                extraNode = item;
            }
            if (TagConsts.ETL_TRANSFORM.equals(item.getTagName())) {
                transformNodeList.add(item);
            }
            if (TagConsts.ETL_LOAD.equals(item.getTagName())) {
                loadNode = item;
            }
            if (TagConsts.ETL_BEFORE.equals(item.getTagName())) {
                beforeNode = item;
            }
            if (TagConsts.ETL_AFTER.equals(item.getTagName())) {
                afterNode = item;
            }
        }

        if (queryNode == null && extraNode == null) {
            warnPoster.accept(TAG_NAME + " missing elt query node " + SqlQueryListNode.TAG_NAME + "/etl-extra child element");
        }

        if (loadNode == null) {
            warnPoster.accept(TAG_NAME + " missing etl load node etl-load child element");
        }
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        List<XmlNode> children = node.getChildren();
        if (children == null || children.isEmpty()) {
            return;
        }
        XmlNode queryNode = null;
        XmlNode extraNode = null;
        List<XmlNode> transformNodeList = new ArrayList<>();
        List<Map.Entry<XmlNode, List<String>>> transformNodeAdditionalList = new ArrayList<>();
        XmlNode loadNode = null;
        XmlNode beforeNode = null;
        XmlNode afterNode = null;
        for (XmlNode item : children) {
            if (TagConsts.SQL_QUERY_LIST.equals(item.getTagName())) {
                queryNode = item;
            }
            if (TagConsts.ETL_EXTRA.equals(item.getTagName())) {
                extraNode = item;
            }
            if (TagConsts.ETL_TRANSFORM.equals(item.getTagName())) {
                List<String> itemAdditionalList = new ArrayList<>();
                Map<String, String> attrMap = item.getTagAttrMap();
                String[] additionalAttrs = {AttrConsts.EXCLUDE, AttrConsts.INCLUDE, AttrConsts.EXTERNAL};
                for (String attr : additionalAttrs) {
                    if (attrMap.containsKey(attr)) {
                        boolean ok = executor.toBoolean(executor.attrValue(attr, FeatureConsts.BOOLEAN, item, context));
                        if (ok) {
                            itemAdditionalList.add(attr);
                        }
                    }
                }

                if (itemAdditionalList.isEmpty()) {
                    transformNodeList.add(item);
                } else {
                    transformNodeAdditionalList.add(new AbstractMap.SimpleEntry<>(item, itemAdditionalList));
                }
            }
            if (TagConsts.ETL_LOAD.equals(item.getTagName())) {
                loadNode = item;
            }
            if (TagConsts.ETL_BEFORE.equals(item.getTagName())) {
                beforeNode = item;
            }
            if (TagConsts.ETL_AFTER.equals(item.getTagName())) {
                afterNode = item;
            }
        }

        if (queryNode == null && extraNode == null) {
            throw new IllegalStateException("missing elt query node!");
        }

        if (loadNode == null) {
            throw new IllegalStateException("missing etl load node!");
        }


        Integer readBatchSize = executor.convertAs(executor.attrValue(AttrConsts.READ_BATCH_SIZE, FeatureConsts.INT, node, context), Integer.class);
        Integer writeBatchSize = executor.convertAs(executor.attrValue(AttrConsts.WRITE_BATCH_SIZE, FeatureConsts.INT, node, context), Integer.class);
        boolean beforeTruncate = executor.toBoolean(executor.attrValue(AttrConsts.BEFORE_TRUNCATE, FeatureConsts.BOOLEAN, node, context));
        boolean useCursor = executor.toBoolean(executor.attrValue(AttrConsts.USE_CURSOR, FeatureConsts.BOOLEAN, node, context));
        boolean sync = executor.toBoolean(executor.attrValue(AttrConsts.SYNC, FeatureConsts.BOOLEAN, node, context));
        Integer commitSize = executor.convertAs(executor.attrValue(AttrConsts.COMMIT_SIZE, FeatureConsts.INT, node, context), Integer.class);
        String itemName = executor.convertAs(executor.attrValue(AttrConsts.ITEM, FeatureConsts.STRING, node, context), String.class);
        if (readBatchSize == null) {
            readBatchSize = 2000;
        }
        if (writeBatchSize == null) {
            writeBatchSize = 500;
        }

        if (commitSize == null) {
            commitSize = writeBatchSize;
        }
        if (itemName == null || itemName.isEmpty()) {
            itemName = "item";
        }

        String loadDatasource = (String) executor.attrValue(AttrConsts.DATASOURCE, FeatureConsts.STRING, loadNode, context);
        String loadTable = (String) executor.attrValue(AttrConsts.TABLE, FeatureConsts.STRING, loadNode, context);


        Class<?> resultType = Map.class;
        String extraDatasource = null;
        BindSql bql = null;
        if (queryNode != null) {
            extraDatasource = (String) executor.attrValue(AttrConsts.DATASOURCE, FeatureConsts.STRING, queryNode, context);
            bql = SqlDialect.getSqlDialectList(extraDatasource, queryNode, context, executor);
            String resultTypeName = (String) executor.attrValue(AttrConsts.RESULT_TYPE, FeatureConsts.STRING, queryNode, context);
            resultType = executor.loadClass(resultTypeName);
            if (resultType == null) {
                resultType = Map.class;
            }

        } else if (extraNode != null) {
            extraDatasource = (String) executor.attrValue(AttrConsts.DATASOURCE, FeatureConsts.STRING, extraNode, context);
            String extraTable = (String) executor.attrValue(AttrConsts.TABLE, FeatureConsts.STRING, extraNode, context);
            String script = "select * from " + extraTable + " ";
            bql = new BindSql(script);
        }


        Map<String, Connection> bakConn = (Map<String, Connection>) context.computeIfAbsent(ParamsConsts.CONNECTIONS, (key -> new HashMap<>()));
        executor.visitSet(context, ParamsConsts.CONNECTIONS, new HashMap<>());

        // 备份堆栈
        Object bakItem = executor.visit(itemName, context);

        AtomicReference<JdbcCursor<?>> taskCursor = new AtomicReference<>();

        try {
            executor.sqlTransNone(extraDatasource, context);
            executor.sqlTransBegin(loadDatasource, Connection.TRANSACTION_READ_COMMITTED, context);
            Connection loadConn = executor.getConnection(loadDatasource, context);

            if (beforeNode != null) {
                executor.execAsProcedure(beforeNode, context, false, false);
            }

            if (beforeTruncate) {
                JdbcResolver.update(loadConn, "truncate table " + loadTable, new ArrayList<>());
            }

            Map<String, XmlNode> targetNodeMap = new LinkedHashMap<>();
            Map<String, Map.Entry<String, List<String>>> targetMap = new LinkedHashMap<>();
            Map<String, Class<?>> targetTypeMap = new LinkedHashMap<>();
            Map<String, JDBCType> targetJdbcTypeMap = new LinkedHashMap<>();
            Map<String, XmlNode> targetExternalNodeMap = new LinkedHashMap<>();
            AtomicReference<String> loadSql = new AtomicReference<>("");


            if (executor.isDebug()) {
                bql = bql.concat(getTrackingComment(node));
                executor.logDebug("etl extra: \n" + bql);
            }

            CountDownLatch latchPrepared = new CountDownLatch(1);
            CountDownLatch latchReadFinish = new CountDownLatch(1);
            CountDownLatch latchAllDone = new CountDownLatch(2);
            AtomicInteger pageIndex = new AtomicInteger(0);
            LinkedBlockingQueue<List<Object>> loadArgs = new LinkedBlockingQueue<>();
            AtomicReference<Throwable> throwReadTask = new AtomicReference<>();
            AtomicReference<Throwable> throwWriteTask = new AtomicReference<>();

            String taskExtraDatasource = extraDatasource;
            BindSql taskBql = bql;
            Class<?> taskResultType = resultType;
            int taskReadBatchSize = readBatchSize;
            String taskItemName = itemName;
            int taskWriteBatchSize = writeBatchSize;
            int taskCommitSize = commitSize;


            if (useCursor) {
                Connection conn = executor.getConnection(taskExtraDatasource, context);
                try {
                    JdbcCursor<?> cursor = JdbcResolver.cursor(conn, taskBql, taskResultType, (nn, ql) -> {
                        return JdbcResolver.buildCursorStatement(nn, ql, taskReadBatchSize);
                    });
                    taskCursor.set(cursor);
                } catch (SQLException e) {
                    throw new IllegalStateException(e.getMessage(), e);
                }
            }

            BiPredicate<Collection<List<Object>>, AtomicReference<Throwable>> delegateReadOnce = (collection, thrRef) -> {
                try {
                    List<?> list = null;
                    JdbcCursor<?> cursor = taskCursor.get();
                    if (useCursor) {
                        if (!cursor.hasRow()) {
                            if (executor.isDebug()) {
                                executor.logDebug("etl-read no data found! at " + getNodeLocation(node));
                            }
                            return false;
                        }
                        list = cursor.nextCount(taskReadBatchSize);
                    } else {
                        list = executor.sqlQueryPage(taskExtraDatasource, taskBql, context, taskResultType, new ApiPage(pageIndex.get(), taskReadBatchSize));
                        if (list.isEmpty()) {
                            if (executor.isDebug()) {
                                executor.logDebug("etl-read no data found! at " + getNodeLocation(node));
                            }
                            return false;
                        }
                    }

                    if (executor.isDebug()) {
                        executor.logDebug("etl-read found batch data! at " + getNodeLocation(node));
                    }

                    if (pageIndex.get() == 0) {
                        if (executor.isDebug()) {
                            executor.logDebug("etl-read at page 0! at " + getNodeLocation(node));
                        }
                        if (transformNodeList.isEmpty()) {
                            Map<String, XmlNode> autoMapping = new LinkedHashMap<>();
                            Object row = list.get(0);
                            if (row instanceof Map) {
                                Map<String, Object> map = (Map<String, Object>) row;

                                String loadMetaSql = "select * from " + loadTable + " where 1=2";
                                QueryResult loadResult = JdbcResolver.query(loadConn, loadMetaSql, new ArrayList<>());
                                List<QueryColumn> loadColumns = loadResult.getColumns();


                                for (QueryColumn loadColumn : loadColumns) {
                                    String loadName = loadColumn.getName();
                                    for (String extraName : map.keySet()) {
                                        if (extraName.equalsIgnoreCase(loadName)) {

                                            XmlNode xmlNode = new XmlNode();
                                            xmlNode.setTagName(TagConsts.ETL_TRANSFORM);
                                            xmlNode.setNodeType(XmlNode.NodeType.ELEMENT);
                                            xmlNode.setChildren(new ArrayList<>());
                                            xmlNode.setTagAttrMap(new LinkedHashMap<>());
                                            xmlNode.setAttrFeatureMap(new LinkedHashMap<>());
                                            xmlNode.getTagAttrMap().put(AttrConsts.SOURCE, extraName);
                                            xmlNode.getTagAttrMap().put(AttrConsts.TARGET, loadName);
                                            String javaType = null;
                                            int type = loadColumn.getType();
                                            if (Types.TIME == type
                                                    || Types.DATE == type
                                                    || Types.TIMESTAMP == type
                                                    || Types.TIME_WITH_TIMEZONE == type
                                                    || Types.TIMESTAMP_WITH_TIMEZONE == type) {
                                                javaType = Date.class.getName();
                                            } else if (Types.VARCHAR == type
                                                    || Types.CHAR == type
                                                    || Types.NCHAR == type
                                                    || Types.NVARCHAR == type) {
                                                javaType = String.class.getName();
                                            } else if (Types.LONGVARCHAR == type
                                                    || Types.LONGNVARCHAR == type) {
                                                javaType = Clob.class.getName();
                                            } else if (Types.INTEGER == type
                                                    || Types.SMALLINT == type
                                                    || Types.TINYINT == type) {
                                                javaType = Integer.class.getName();
                                            } else if (Types.BIGINT == type) {
                                                javaType = Long.class.getName();
                                            } else if (Types.DOUBLE == type
                                                    || Types.FLOAT == type) {
                                                javaType = Double.class.getName();
                                            } else if (Types.REAL == type
                                                    || Types.NUMERIC == type
                                                    || Types.DECIMAL == type) {
                                                javaType = BigDecimal.class.getName();
                                            } else if (Types.CLOB == type
                                                    || Types.NCLOB == type) {
                                                javaType = Clob.class.getName();
                                            } else if (Types.BLOB == type) {
                                                javaType = Blob.class.getName();
                                            }

                                            if (javaType != null) {
                                                xmlNode.getTagAttrMap().put(AttrConsts.TYPE, javaType);
                                            }
                                            JDBCType jdbcType = null;
                                            try {
                                                jdbcType = JDBCType.valueOf(type);
                                            } catch (Exception e) {

                                            }
                                            if (jdbcType != null) {
                                                xmlNode.getTagAttrMap().put(AttrConsts.JDBC_TYPE, jdbcType.name());
                                            }
                                            autoMapping.put(loadName, xmlNode);
                                            break;
                                        }
                                    }
                                }
                            }
                            for (Map.Entry<String, XmlNode> entry : autoMapping.entrySet()) {
                                transformNodeList.add(entry.getValue());
                            }
                        }


                        if (!transformNodeList.isEmpty()) {
                            for (XmlNode item : transformNodeList) {
                                String target = executor.convertAs(executor.attrValue(AttrConsts.TARGET, FeatureConsts.STRING, item, context), String.class);
                                if (target != null && !target.isEmpty()) {
                                    String column = item.getTagAttrMap().get(AttrConsts.SOURCE);
                                    List<String> features = item.getAttrFeatureMap().get(AttrConsts.SOURCE);
                                    targetMap.put(target, new AbstractMap.SimpleEntry<>(column, features));

                                    String typeName = item.getTagAttrMap().get(AttrConsts.TYPE);
                                    Class<?> targetType = executor.loadClass(typeName);
                                    targetTypeMap.put(target, targetType);

                                    String jdbcTypeName = item.getTagAttrMap().get(AttrConsts.JDBC_TYPE);
                                    JDBCType[] jdbcTypeArr = JDBCType.class.getEnumConstants();
                                    for (JDBCType jdbcType : jdbcTypeArr) {
                                        if (jdbcType.name().equalsIgnoreCase(jdbcTypeName)) {
                                            targetJdbcTypeMap.put(target, jdbcType);
                                        }
                                    }
                                }
                                targetNodeMap.put(target, item);
                            }
                        }

                        if (!transformNodeAdditionalList.isEmpty()) {
                            for (Map.Entry<XmlNode, List<String>> entry : transformNodeAdditionalList) {
                                List<String> attrs = entry.getValue();
                                if (attrs.contains(AttrConsts.EXCLUDE)) {
                                    XmlNode item = entry.getKey();
                                    String target = executor.convertAs(executor.attrValue(AttrConsts.TARGET, FeatureConsts.STRING, item, context), String.class);
                                    if (target != null && !target.isEmpty()) {
                                        targetMap.remove(target);

                                        targetTypeMap.remove(target);
                                        targetJdbcTypeMap.remove(target);
                                    }
                                    targetNodeMap.remove(target);
                                }
                            }
                        }

                        if (!transformNodeAdditionalList.isEmpty()) {
                            for (Map.Entry<XmlNode, List<String>> entry : transformNodeAdditionalList) {
                                List<String> attrs = entry.getValue();
                                if (attrs.contains(AttrConsts.INCLUDE)
                                        || attrs.contains(AttrConsts.EXTERNAL)) {
                                    XmlNode item = entry.getKey();
                                    String target = executor.convertAs(executor.attrValue(AttrConsts.TARGET, FeatureConsts.STRING, item, context), String.class);
                                    if (target != null && !target.isEmpty()) {
                                        String column = item.getTagAttrMap().get(AttrConsts.SOURCE);
                                        List<String> features = item.getAttrFeatureMap().get(AttrConsts.SOURCE);
                                        targetMap.put(target, new AbstractMap.SimpleEntry<>(column, features));

                                        String typeName = item.getTagAttrMap().get(AttrConsts.TYPE);
                                        Class<?> targetType = executor.loadClass(typeName);
                                        targetTypeMap.put(target, targetType);

                                        String jdbcTypeName = item.getTagAttrMap().get(AttrConsts.JDBC_TYPE);
                                        JDBCType[] jdbcTypeArr = JDBCType.class.getEnumConstants();
                                        for (JDBCType jdbcType : jdbcTypeArr) {
                                            if (jdbcType.name().equalsIgnoreCase(jdbcTypeName)) {
                                                targetJdbcTypeMap.put(target, jdbcType);
                                            }
                                        }
                                    }
                                    targetNodeMap.put(target, item);
                                    if (attrs.contains(AttrConsts.EXTERNAL)) {
                                        targetExternalNodeMap.put(target, item);
                                    }
                                }
                            }
                        }


                        if (targetMap.isEmpty()) {
                            throw new SQLException("no target column transform found!");
                        }

                        StringBuilder loadSqlBuilder = new StringBuilder();
                        loadSqlBuilder.append("insert into ").append(loadTable).append(" ( ");
                        boolean isFirst = true;
                        for (Map.Entry<String, Map.Entry<String, List<String>>> entry : targetMap.entrySet()) {
                            if (!isFirst) {
                                loadSqlBuilder.append(", ");
                            }
                            loadSqlBuilder.append(entry.getKey());
                            isFirst = false;
                        }
                        loadSqlBuilder.append(" ) ");
                        loadSqlBuilder.append(" values ").append(" ( ");
                        isFirst = true;
                        for (Map.Entry<String, Map.Entry<String, List<String>>> entry : targetMap.entrySet()) {
                            if (!isFirst) {
                                loadSqlBuilder.append(", ");
                            }
                            loadSqlBuilder.append("?");
                            isFirst = false;
                        }
                        loadSqlBuilder.append(" ) ");

                        loadSql.set(loadSqlBuilder.toString());
                        if (executor.isDebug()) {
                            executor.logDebug("etl load: \n" + loadSql.get());
                        }

                        latchPrepared.countDown();
                    }

                    if (executor.isDebug()) {
                        executor.logDebug("etl-read convert data, at " + getNodeLocation(node));
                    }

                    int currentCount = 0;
                    for (Object obj : list) {
                        List<Object> elems = new ArrayList<>();
                        for (Map.Entry<String, Map.Entry<String, List<String>>> entry : targetMap.entrySet()) {
                            Map.Entry<String, List<String>> value = entry.getValue();
                            Object val = null;
                            XmlNode externalNode = targetExternalNodeMap.get(entry.getKey());
                            if (externalNode == null) {
                                val = executor.visit(value.getKey(), obj);
                            } else {
                                executor.visitSet(context, taskItemName, obj);
                                val = executor.attrValue(AttrConsts.SOURCE, FeatureConsts.EVAL, externalNode, context);
                            }

                            XmlNode definedNode = targetNodeMap.get(entry.getKey());
                            if (definedNode != null) {
                                val = executor.resultValue(val, definedNode.getAttrFeatureMap().get(AttrConsts.TARGET), definedNode, context);
                            }
                            Class<?> targetType = targetTypeMap.get(entry.getKey());
                            if (targetType != null) {
                                val = ObjectConvertor.tryConvertAsType(val, targetType);
                            }
                            JDBCType jdbcType = targetJdbcTypeMap.get(entry.getKey());
                            if (jdbcType != null) {
                                val = TypedArgument.of(jdbcType, val);
                            }
                            elems.add(val);
                        }
                        if (collection instanceof BlockingQueue) {
                            BlockingQueue<List<Object>> queue = (BlockingQueue<List<Object>>) collection;
                            queue.put(elems);
                        } else {
                            collection.add(elems);
                        }
                        currentCount++;
                    }

                    if (currentCount < taskReadBatchSize) {
                        if (executor.isDebug()) {
                            executor.logDebug("etl-read lower than read batch size! at " + getNodeLocation(node));
                        }
                        return false;
                    }


                    pageIndex.incrementAndGet();
                    if (executor.isDebug()) {
                        executor.logDebug("etl-read next page " + pageIndex.get() + " loop! at " + getNodeLocation(node));
                    }
                    return true;
                } catch (Throwable e) {
                    thrRef.set(e);
                    return false;
                }
            };


            if (sync) {
                List<List<Object>> onceArgs = new ArrayList<>(taskReadBatchSize);
                int commitCount = 0;
                while (true) {

                    boolean hasMoreData = delegateReadOnce.test(onceArgs, throwReadTask);
                    if (throwReadTask.get() != null) {
                        throw throwReadTask.get();
                    }
                    if (executor.isDebug()) {
                        executor.logDebug("etl-write write once batch " + onceArgs.size() + "! at " + getNodeLocation(node));
                    }
                    JdbcResolver.batchByListableValues(loadConn, loadSql.get(), onceArgs.iterator(), taskWriteBatchSize);
                    commitCount += onceArgs.size();
                    onceArgs.clear();

                    if (commitCount >= commitSize) {
                        if (executor.isDebug()) {
                            executor.logDebug("etl-write commit once! at " + getNodeLocation(node));
                        }
                        executor.sqlTransCommit(loadDatasource, context);
                        commitCount = 0;
                    }

                    if (!hasMoreData) {
                        break;
                    }
                }
            } else {
                Runnable readTask = () -> {
                    try {
                        while (true) {
                            double missWriteSleepMs = 1;
                            while (loadArgs.size() >= taskReadBatchSize * 2) {
                                if (executor.isDebug()) {
                                    executor.logDebug("etl-read await " + ((long) missWriteSleepMs) + "(ms) writer consumer! at " + getNodeLocation(node));
                                }
                                try {
                                    Thread.sleep((long) missWriteSleepMs);
                                } catch (Exception e) {

                                }
                                missWriteSleepMs = Math.min(missWriteSleepMs * 1.1, 90);
                            }

                            boolean hasMoreData = delegateReadOnce.test(loadArgs, throwReadTask);
                            if (throwReadTask.get() != null) {
                                throw throwReadTask.get();
                            }
                            if (!hasMoreData) {
                                break;
                            }
                        }
                    } catch (Throwable e) {
                        throwReadTask.set(e);
                        executor.logError("etl read task error: " + e.getMessage(), e);
                    } finally {
                        latchPrepared.countDown();
                        latchReadFinish.countDown();
                        latchAllDone.countDown();
                    }

                };

                Runnable writeTask = () -> {
                    try {
                        if (executor.isDebug()) {
                            executor.logDebug("etl-write await reader prepared! at " + getNodeLocation(node));
                        }

                        latchPrepared.await();
                        if (throwReadTask.get() != null) {
                            if (executor.isDebug()) {
                                executor.logDebug("etl-write found reader prepare error! at " + getNodeLocation(node));
                            }
                            return;
                        }
                        int commitCount = 0;

                        List<List<Object>> onceArgs = new ArrayList<>(taskWriteBatchSize);
                        int onceCount = 0;
                        boolean hasData = true;
                        while (hasData) {

                            if (latchReadFinish.getCount() <= 0 && loadArgs.isEmpty()) {
                                if (executor.isDebug()) {
                                    executor.logDebug("etl-write no more data! at " + getNodeLocation(node));
                                }
                                hasData = false;
                                break;
                            }

                            double missDataSleepMs = 1;
                            while (onceCount < taskWriteBatchSize) {
                                if (latchReadFinish.getCount() <= 0 && loadArgs.isEmpty()) {
                                    if (executor.isDebug()) {
                                        executor.logDebug("etl-write no more data, process last once! at " + getNodeLocation(node));
                                    }
                                    hasData = false;
                                    break;
                                }
                                List<Object> row = null;
                                int tryCount = 10;
                                while (tryCount > 0) {
                                    row = loadArgs.poll();
                                    if (row != null) {
                                        break;
                                    }
                                    tryCount--;
                                }
                                if (row == null) {
                                    if (executor.isDebug()) {
                                        executor.logDebug("etl-write await " + ((long) missDataSleepMs) + "(ms) reader producer! at " + getNodeLocation(node));
                                    }
                                    try {
                                        Thread.sleep((long) missDataSleepMs);
                                    } catch (Exception e) {

                                    }
                                    missDataSleepMs = Math.min(missDataSleepMs * 1.1, 90);
                                    continue;
                                }
                                onceArgs.add(row);
                                onceCount++;
                                missDataSleepMs = 1;
                            }


                            if (onceCount > 0) {
                                if (executor.isDebug()) {
                                    executor.logDebug("etl-write write once " + onceArgs.size() + "! at " + getNodeLocation(node));
                                }
                                JdbcResolver.batchByListableValues(loadConn, loadSql.get(), onceArgs.iterator(), taskWriteBatchSize);
                                commitCount += onceCount;
                                onceArgs.clear();
                                onceCount = 0;
                            }

                            if (commitCount >= taskCommitSize) {
                                if (executor.isDebug()) {
                                    executor.logDebug("etl-write commit once! at " + getNodeLocation(node));
                                }
                                executor.sqlTransCommit(loadDatasource, context);
                                commitCount = 0;
                            }

                        }
                    } catch (Throwable e) {
                        throwWriteTask.set(e);
                        executor.logError("etl write task error: " + e.getMessage(), e);
                    } finally {
                        latchAllDone.countDown();
                    }
                };

                String hcode = String.format("%x", latchAllDone.hashCode());

                newThread("read-" + hcode, readTask).start();
                newThread("write-" + hcode, writeTask).start();

                try {
                    latchAllDone.await();
                } catch (InterruptedException e) {

                }

                if (throwReadTask.get() != null) {
                    throw throwReadTask.get();
                }
                if (throwWriteTask.get() != null) {
                    throw throwWriteTask.get();
                }
            }

            executor.sqlTransCommit(loadDatasource, context);

            if (afterNode != null) {
                executor.execAsProcedure(afterNode, context, false, false);
            }

            executor.sqlTransCommit(loadDatasource, context);
        } catch (Throwable e) {
            executor.sqlTransRollback(loadDatasource, context);
            throw new ThrowSignalException(e.getMessage(), e);
        } finally {
            // 还原堆栈
            executor.visitSet(context, itemName, bakItem);

            JdbcCursor<?> cursor = taskCursor.get();
            if (cursor != null) {
                try {
                    cursor.dispose();
                } catch (SQLException e) {
                    throw new IllegalStateException(e.getMessage(), e);
                }
            }

            Map<String, Connection> conns = (Map<String, Connection>) context.get(ParamsConsts.CONNECTIONS);
            for (Map.Entry<String, Connection> entry : conns.entrySet()) {
                Connection conn = entry.getValue();
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        executor.logWarn(() -> e.getMessage(), e);
                        e.printStackTrace();
                    }
                }
            }

            executor.visitSet(context, ParamsConsts.CONNECTIONS, bakConn);
        }

    }

    public Thread newThread(String name, Runnable task) {
        Thread thread = new Thread(task);
        thread.setName(TAG_NAME + "-" + name);
        return thread;
    }
}
