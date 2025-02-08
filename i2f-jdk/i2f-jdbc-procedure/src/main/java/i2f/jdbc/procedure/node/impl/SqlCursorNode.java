package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.node.base.SqlDialect;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.BreakSignalException;
import i2f.jdbc.procedure.signal.impl.ContinueSignalException;

import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class SqlCursorNode implements ExecutorNode {
    public static final String TAG_NAME = "sql-cursor";

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
        XmlNode bodyNode = null;
        for (XmlNode item : children) {
            if (SqlQueryListNode.TAG_NAME.equals(item.getTagName())) {
                queryNode = item;
            }
            if ("lang-body".equals(item.getTagName())) {
                bodyNode = item;
            }
        }

        if (queryNode == null) {
            throw new IllegalStateException("missing cursor query node!");
        }

        if (bodyNode == null) {
            throw new IllegalStateException("missing cursor body node!");
        }

        Integer batchSize = (Integer) executor.attrValue(AttrConsts.BATCH_SIZE, FeatureConsts.INT, node, context);
        String item = node.getTagAttrMap().get(AttrConsts.ITEM);
        boolean acceptBatch = (boolean) executor.attrValue(AttrConsts.ACCEPT_BATCH, FeatureConsts.BOOLEAN, node, context);
        if (item == null || item.isEmpty()) {
            item = AttrConsts.ITEM;
        }

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

        int pageIndex = 0;
        if (batchSize == null || batchSize <= 0) {
            batchSize = 2000;
        }
        Map<String, Object> bakParams = new LinkedHashMap<>();
        bakParams.put(item, executor.visit(item, context.getParams()));
        while (true) {
            List<?> list = executor.sqlQueryPage(datasource, dialectScriptList, context.getParams(), resultType, pageIndex, batchSize);
            if (list.isEmpty()) {
                break;
            }

            if (acceptBatch) {
                try {
                    executor.setParamsObject(context.getParams(), item, list);
                    executor.execAsProcedure(bodyNode, context, false, false);
                } catch (ContinueSignalException e) {
                    continue;
                } catch (BreakSignalException e) {
                    break;
                }
            } else {
                boolean breakSignal = false;
                int count = 0;
                for (Object obj : list) {
                    count++;
                    try {
                        executor.setParamsObject(context.getParams(), item, obj);
                        executor.execAsProcedure(bodyNode, context, false, false);
                    } catch (ContinueSignalException e) {
                        continue;
                    } catch (BreakSignalException e) {
                        breakSignal = true;
                        break;
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

        for (Map.Entry<String, Object> entry : bakParams.entrySet()) {
            executor.setParamsObject(context.getParams(), entry.getKey(), entry.getValue());
        }
    }
}
