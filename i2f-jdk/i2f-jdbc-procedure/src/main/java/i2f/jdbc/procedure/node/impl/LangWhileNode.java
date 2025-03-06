package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.BreakSignalException;
import i2f.jdbc.procedure.signal.impl.ContinueSignalException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangWhileNode extends AbstractExecutorNode {
    public static final String TAG_NAME = "lang-while";

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        String test = node.getTagAttrMap().get(AttrConsts.TEST);
        if(test==null || test.isEmpty()){
            warnPoster.accept(TAG_NAME+" missing attribute "+AttrConsts.TEST);
        }
    }

    @Override
    public void execInner(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        String script = node.getTagAttrMap().get(AttrConsts.TEST);
        String firstName = node.getTagAttrMap().get(AttrConsts.FIRST);
        String indexName = node.getTagAttrMap().get(AttrConsts.INDEX);
        if (firstName == null || firstName.isEmpty()) {
            firstName = AttrConsts.FIRST;
        }
        if (indexName == null || indexName.isEmpty()) {
            indexName = AttrConsts.INDEX;
        }
        // 备份堆栈
        Map<String, Object> bakParams = new LinkedHashMap<>();
        bakParams.put(firstName, context.getParams().get(firstName));
        bakParams.put(indexName, context.getParams().get(indexName));

        boolean isFirst = true;
        int index = 0;
        while ((boolean) executor.attrValue(AttrConsts.TEST, AttrConsts.TEST, node, context)) {
            context.getParams().put(firstName, isFirst);
            context.getParams().put(indexName, index);
            isFirst = false;
            index++;
            try {
                executor.execAsProcedure(node, context, false, false);
            } catch (ContinueSignalException e) {
                continue;
            } catch (BreakSignalException e) {
                break;
            }
        }
        // 还原堆栈
        context.getParams().put(firstName, bakParams.get(firstName));
        context.getParams().put(indexName, bakParams.get(indexName));
    }

}
