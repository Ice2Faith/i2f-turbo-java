package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.JdbcResolver;
import i2f.jdbc.data.QueryColumn;
import i2f.jdbc.data.QueryResult;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.ParamsConsts;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.node.base.SqlDialect;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.BreakSignalException;
import i2f.jdbc.procedure.signal.impl.ContinueSignalException;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;
import i2f.reflect.vistor.Visitor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class SqlEtlNode implements ExecutorNode {
    public static final String TAG_NAME = "sql-etl";

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        List<XmlNode> children = node.getChildren();
        if (children == null || children.isEmpty()) {
            return;
        }
        XmlNode queryNode = null;
        List<XmlNode> transformNodeList=new ArrayList<>();
        XmlNode loadNode=null;
        XmlNode beforeNode=null;
        XmlNode afterNode=null;
        for (XmlNode item : children) {
            if (SqlQueryListNode.TAG_NAME.equals(item.getTagName())) {
                queryNode = item;
            }
            if ("etl-transform".equals(item.getTagName())) {
                transformNodeList.add(item);
            }
            if("etl-load".equals(item.getTagName())){
                loadNode=item;
            }
            if("etl-before".equals(item.getTagName())){
                beforeNode=item;
            }
            if("etl-after".equals(item.getTagName())){
                afterNode=item;
            }
        }

        if (queryNode == null) {
            throw new IllegalStateException("missing elt query node!");
        }

        if (loadNode == null) {
            throw new IllegalStateException("missing etl load node!");
        }

        Map<String,XmlNode> targetNodeMap=new LinkedHashMap<>();
        Map<String,Map.Entry<String,List<String>>> targetMap=new LinkedHashMap<>();
        if(!transformNodeList.isEmpty()){
            for (XmlNode item : transformNodeList) {
                String target = item.getTagAttrMap().get(AttrConsts.TARGET);
                if(target!=null && !target.isEmpty()){
                    String column = item.getTagAttrMap().get(AttrConsts.SOURCE);
                    List<String> features = item.getAttrFeatureMap().get(AttrConsts.SOURCE);
                    targetMap.put(target,new AbstractMap.SimpleEntry<>(column,features));
                }
                targetNodeMap.put(target,item);
            }
        }

        if(targetMap.isEmpty()){
            throw new IllegalStateException("missing etl transform node!");
        }


        Integer readBatchSize = (Integer) executor.attrValue(AttrConsts.READ_BATCH_SIZE, FeatureConsts.INT, node, context);
        Integer writeBatchSize = (Integer) executor.attrValue(AttrConsts.WRITE_BATCH_SIZE, FeatureConsts.INT, node, context);
        Boolean beforeTruncate = (Boolean) executor.attrValue(AttrConsts.BEFORE_TRUNCATE, FeatureConsts.BOOLEAN, node, context);
        Integer commitSize = (Integer) executor.attrValue(AttrConsts.COMMIT_SIZE, FeatureConsts.INT, node, context);
        if(readBatchSize==null){
            readBatchSize=2000;
        }
        if(writeBatchSize==null){
            writeBatchSize=500;
        }
        if(beforeTruncate==null){
            beforeTruncate=false;
        }
        if(commitSize==null){
            commitSize=writeBatchSize;
        }

        String loadDatasource = (String) executor.attrValue(AttrConsts.DATASOURCE, FeatureConsts.STRING, loadNode, context);
        String loadTable = (String) executor.attrValue(AttrConsts.TABLE, FeatureConsts.STRING, loadNode, context);


        List<Map.Entry<String, String>> dialectScriptList = SqlDialect.getSqlDialectList(queryNode, context, executor);
        String datasource = (String) executor.attrValue(AttrConsts.DATASOURCE, FeatureConsts.STRING, queryNode, context);
        String script = (String) executor.attrValue(AttrConsts.SCRIPT, FeatureConsts.VISIT, queryNode, context);
        String resultTypeName = (String) executor.attrValue(AttrConsts.RESULT_TYPE, FeatureConsts.STRING, queryNode, context);
        Class<?> resultType = executor.loadClass(resultTypeName);
        if (resultType == null) {
            resultType = Map.class;
        }
        if (script == null || script.isEmpty()) {
            script = queryNode.getTagBody();
        }
        if (dialectScriptList.isEmpty()) {
            dialectScriptList.add(new AbstractMap.SimpleEntry<>(null, script));
        }

        StringBuilder loadSqlBuilder=new StringBuilder();
        loadSqlBuilder.append("insert into ").append(loadTable).append(" ( ");
        boolean isFirst=true;
        for (Map.Entry<String, Map.Entry<String, List<String>>> entry : targetMap.entrySet()) {
            if(!isFirst){
                loadSqlBuilder.append(", ");
            }
            loadSqlBuilder.append(entry.getKey());
            isFirst=false;
        }
        loadSqlBuilder.append(" ) ");
        loadSqlBuilder.append(" values ").append(" ( ");
        isFirst=true;
        for (Map.Entry<String, Map.Entry<String, List<String>>> entry : targetMap.entrySet()) {
            if(!isFirst){
                loadSqlBuilder.append(", ");
            }
            loadSqlBuilder.append("?");
            isFirst=false;
        }
        loadSqlBuilder.append(" ) ");

        Map<String, Connection> bakConn=context.paramsComputeIfAbsent(ParamsConsts.CONNECTIONS,(key->new HashMap<>()));
        context.paramsSet(ParamsConsts.CONNECTIONS,new HashMap<>());
        try {
            executor.sqlTransBegin(loadDatasource, Connection.TRANSACTION_READ_COMMITTED, context.getParams());
            Map<String, Connection> conns = context.paramsGet(ParamsConsts.CONNECTIONS);
            Connection loadConn = conns.get(loadDatasource);

            if (beforeNode != null) {
                executor.execAsProcedure(beforeNode, context, false, false);
            }

            if(beforeTruncate){
                JdbcResolver.update(loadConn,"truncate table "+loadTable,new ArrayList<>());
            }

            String loadSql = loadSqlBuilder.toString();

            int pageIndex = 0;
            int commitCount = 0;
            while (true) {
                List<?> list = executor.sqlQueryPage(datasource, dialectScriptList, context.getParams(), resultType, pageIndex, readBatchSize);
                if (list.isEmpty()) {
                    break;
                }
                int currentCount=0;
                List<List<Object>> loadArgs = new ArrayList<>();
                for (Object obj : list) {
                    List<Object> elems = new ArrayList<>();
                    for (Map.Entry<String, Map.Entry<String, List<String>>> entry : targetMap.entrySet()) {
                        Map.Entry<String, List<String>> value = entry.getValue();
                        Object val = Visitor.visit(value.getKey(), obj).get();
                        boolean isAttrValue=false;
                        if(val==null){
                            val=executor.attrValue(AttrConsts.SOURCE,FeatureConsts.EVAL,targetNodeMap.get(entry.getKey()),context);
                            isAttrValue=true;
                        }
                        if(!isAttrValue) {
                            val = executor.resultValue(val, value.getValue(), targetNodeMap.get(entry.getKey()), context);
                        }
                        elems.add(val);
                    }
                    loadArgs.add(elems);
                    commitCount++;
                    currentCount++;
                }


                JdbcResolver.batchByListableValues(loadConn, loadSql, loadArgs.iterator(), writeBatchSize);

                if (commitCount >= commitSize) {
                    executor.sqlTransCommit(loadDatasource, context.getParams());
                    commitCount=0;
                }

                if(currentCount<readBatchSize){
                    break;
                }

                pageIndex++;
            }

            executor.sqlTransCommit(loadDatasource, context.getParams());

            if (afterNode != null) {
                executor.execAsProcedure(afterNode, context, false, false);
            }

            executor.sqlTransCommit(loadDatasource, context.getParams());
        }catch (SQLException e){
            executor.sqlTransRollback(loadDatasource,context.getParams());
            throw new ThrowSignalException(e.getMessage(),e);
        }finally {
            Map<String, Connection> conns = context.paramsGet(ParamsConsts.CONNECTIONS);
            for (Map.Entry<String, Connection> entry : conns.entrySet()) {
                Connection conn = entry.getValue();
                if(conn!=null){
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

            context.paramsSet(ParamsConsts.CONNECTIONS,bakConn);
        }

    }
}
