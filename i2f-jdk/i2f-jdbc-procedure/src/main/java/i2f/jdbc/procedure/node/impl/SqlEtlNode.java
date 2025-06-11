package i2f.jdbc.procedure.node.impl;

import i2f.bindsql.BindSql;
import i2f.convert.obj.ObjectConvertor;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.data.QueryColumn;
import i2f.jdbc.data.QueryResult;
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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;
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
        List<Map.Entry<XmlNode,List<String>>> transformNodeAdditionalList=new ArrayList<>();
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
                List<String> itemAdditionalList=new ArrayList<>();
                Map<String, String> attrMap = item.getTagAttrMap();
                String[] additionalAttrs={AttrConsts.EXCLUDE,AttrConsts.INCLUDE,AttrConsts.EXTERNAL};
                for (String attr : additionalAttrs) {
                    if(attrMap.containsKey(attr)){
                        boolean ok = executor.toBoolean(executor.attrValue(attr, FeatureConsts.BOOLEAN, item, context));
                        if(ok){
                           itemAdditionalList.add(attr);
                        }
                    }
                }

                if(itemAdditionalList.isEmpty()) {
                    transformNodeList.add(item);
                }else{
                    transformNodeAdditionalList.add(new AbstractMap.SimpleEntry<>(item,itemAdditionalList));
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
        if(itemName==null || itemName.isEmpty()){
            itemName="item";
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
        Object bakItem=executor.visit(itemName,context);

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
            Map<String,XmlNode> targetExternalNodeMap=new LinkedHashMap<>();
            String loadSql = "";


            if (executor.isDebug()) {
                bql = bql.concat(getTrackingComment(node));
            }

            int pageIndex = 0;
            int commitCount = 0;
            while (true) {
                List<?> list = executor.sqlQueryPage(extraDatasource, bql, context, resultType, new ApiPage(pageIndex, readBatchSize));
                if (list.isEmpty()) {
                    if (executor.isDebug()) {
                        executor.logDebug("no data found! at " + getNodeLocation(node));
                    }
                    break;
                }

                if (executor.isDebug()) {
                    executor.logDebug("found batch data! at " + getNodeLocation(node));
                }

                if (pageIndex == 0) {

                    if (transformNodeList.isEmpty()) {

                        Object row = list.get(0);
                        if (row instanceof Map) {
                            Map<String, Object> map = (Map<String, Object>) row;

                            String loadMetaSql = "select * from " + loadTable + " where 1=2";
                            QueryResult loadResult = JdbcResolver.query(loadConn, loadMetaSql, new ArrayList<>());
                            List<QueryColumn> loadColumns = loadResult.getColumns();

                            for (QueryColumn loadColumn : loadColumns) {
                                for (String extraName : map.keySet()) {
                                    String loadName = loadColumn.getName();
                                    if (extraName.equalsIgnoreCase(loadName)) {

                                        XmlNode xmlNode = new XmlNode();
                                        xmlNode.setTagName(TagConsts.ETL_TRANSFORM);
                                        xmlNode.setNodeType(XmlNode.NodeType.ELEMENT);
                                        xmlNode.setChildren(new ArrayList<>());
                                        xmlNode.setTagAttrMap(new HashMap<>());
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
                                                || Types.LONGVARCHAR == type
                                                || Types.LONGNVARCHAR == type
                                                || Types.NCHAR == type
                                                || Types.NVARCHAR == type) {
                                            javaType = String.class.getName();
                                        } else if (Types.INTEGER == type
                                                || Types.SMALLINT == type
                                                || Types.TINYINT == type) {
                                            javaType = Integer.class.getName();
                                        } else if (Types.BIGINT == type) {
                                            javaType = Long.class.getName();
                                        } else if (Types.DOUBLE == type
                                                || Types.FLOAT == type
                                                || Types.REAL == type
                                                || Types.NUMERIC == type
                                                || Types.DECIMAL == type) {
                                            javaType = Double.class.getName();
                                        }
                                        if (javaType != null) {
                                            xmlNode.getTagAttrMap().put(AttrConsts.TYPE, javaType);
                                        }
                                        targetNodeMap.put(loadName, xmlNode);
                                        break;
                                    }
                                }
                            }
                        }
                    }


                    if (!transformNodeList.isEmpty()) {
                        for (XmlNode item : transformNodeList) {
                            String target = executor.convertAs(executor.attrValue(AttrConsts.TARGET,FeatureConsts.STRING,item,context),String.class);
                            if (target != null && !target.isEmpty()) {
                                String column = item.getTagAttrMap().get(AttrConsts.SOURCE);
                                List<String> features = item.getAttrFeatureMap().get(AttrConsts.SOURCE);
                                targetMap.put(target, new AbstractMap.SimpleEntry<>(column, features));

                                String typeName = item.getTagAttrMap().get(AttrConsts.TYPE);
                                Class<?> targetType = executor.loadClass(typeName);
                                targetTypeMap.put(target, targetType);
                            }
                            targetNodeMap.put(target, item);
                        }
                    }

                    if(!transformNodeAdditionalList.isEmpty()){
                        for (Map.Entry<XmlNode, List<String>> entry : transformNodeAdditionalList) {
                            List<String> attrs = entry.getValue();
                            if(attrs.contains(AttrConsts.EXCLUDE)){
                                XmlNode item=entry.getKey();
                                String target = executor.convertAs(executor.attrValue(AttrConsts.TARGET,FeatureConsts.STRING,item,context),String.class);
                                if (target != null && !target.isEmpty()) {
                                    targetMap.remove(target);

                                    targetTypeMap.remove(target);
                                }
                                targetNodeMap.remove(target);
                            }
                        }
                    }

                    if(!transformNodeAdditionalList.isEmpty()){
                        for (Map.Entry<XmlNode, List<String>> entry : transformNodeAdditionalList) {
                            List<String> attrs = entry.getValue();
                            if(attrs.contains(AttrConsts.INCLUDE)
                            ||attrs.contains(AttrConsts.EXTERNAL)){
                                XmlNode item=entry.getKey();
                                String target = executor.convertAs(executor.attrValue(AttrConsts.TARGET,FeatureConsts.STRING,item,context),String.class);
                                if (target != null && !target.isEmpty()) {
                                    String column = item.getTagAttrMap().get(AttrConsts.SOURCE);
                                    List<String> features = item.getAttrFeatureMap().get(AttrConsts.SOURCE);
                                    targetMap.put(target, new AbstractMap.SimpleEntry<>(column, features));

                                    String typeName = item.getTagAttrMap().get(AttrConsts.TYPE);
                                    Class<?> targetType = executor.loadClass(typeName);
                                    targetTypeMap.put(target, targetType);
                                }
                                targetNodeMap.put(target, item);
                                if(attrs.contains(AttrConsts.EXTERNAL)){
                                    targetExternalNodeMap.put(target,item);
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

                    loadSql = loadSqlBuilder.toString();
                }

                int currentCount = 0;
                List<List<Object>> loadArgs = new ArrayList<>();
                for (Object obj : list) {
                    List<Object> elems = new ArrayList<>();
                    for (Map.Entry<String, Map.Entry<String, List<String>>> entry : targetMap.entrySet()) {
                        Map.Entry<String, List<String>> value = entry.getValue();
                        Object val = null;
                        XmlNode externalNode = targetExternalNodeMap.get(entry.getKey());
                        if(externalNode==null){
                            val=executor.visit(value.getKey(), obj);
                        }else{
                            executor.visitSet(context,itemName,obj);
                            val=executor.attrValue(AttrConsts.SOURCE, FeatureConsts.EVAL, externalNode, context);
                        }

                        XmlNode definedNode = targetNodeMap.get(entry.getKey());
                        if (definedNode != null) {
                            val = executor.resultValue(val, value.getValue(), definedNode, context);
                        }
                        Class<?> targetType = targetTypeMap.get(entry.getKey());
                        if (targetType != null) {
                            val = ObjectConvertor.tryConvertAsType(val, targetType);
                        }
                        elems.add(val);
                    }
                    loadArgs.add(elems);
                    commitCount++;
                    currentCount++;
                }


                JdbcResolver.batchByListableValues(loadConn, loadSql, loadArgs.iterator(), writeBatchSize);

                if (commitCount >= commitSize) {
                    executor.sqlTransCommit(loadDatasource, context);
                    commitCount = 0;
                }

                if (currentCount < readBatchSize) {
                    break;
                }

                pageIndex++;
            }

            executor.sqlTransCommit(loadDatasource, context);

            if (afterNode != null) {
                executor.execAsProcedure(afterNode, context, false, false);
            }

            executor.sqlTransCommit(loadDatasource, context);
        } catch (SQLException e) {
            executor.sqlTransRollback(loadDatasource, context);
            throw new ThrowSignalException(e.getMessage(), e);
        } finally {
            // 还原堆栈
            executor.visitSet(context,itemName,bakItem);

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
}
