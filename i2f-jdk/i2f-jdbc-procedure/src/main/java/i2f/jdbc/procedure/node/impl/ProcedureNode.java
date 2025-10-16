package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 10:37
 */
public class ProcedureNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.PROCEDURE;
    public static final ProcedureNode INSTANCE = new ProcedureNode();

    @Override
    public String tag() {
        return TAG_NAME;
    }


    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        String id = node.getTagAttrMap().get(AttrConsts.ID);
        if (id == null || id.isEmpty()) {
            warnPoster.accept(TAG_NAME + " missing attribute " + AttrConsts.ID);
        }
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        List<XmlNode> children = node.getChildren();
        for (int i = 0; i < children.size(); i++) {
            XmlNode item = children.get(i);
            if (item == null) {
                continue;
            }
            executor.exec(item, context, false, false);
        }
    }
}
