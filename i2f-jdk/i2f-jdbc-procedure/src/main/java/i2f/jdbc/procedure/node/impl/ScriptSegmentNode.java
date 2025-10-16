package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.ParamsConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.Map;
import java.util.function.Consumer;


/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class ScriptSegmentNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.SCRIPT_SEGMENT;

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
        String id = node.getTagAttrMap().get(AttrConsts.ID);
        if (id != null && !id.isEmpty()) {
            ProcedureMeta meta = ProcedureMeta.ofMeta(node);
            executor.getContext().registry(id, meta);
            Map<String, ProcedureMeta> metas = executor.visitAs(ParamsConsts.GLOBAL_METAS, context);
            metas.put(id, meta);
        }
    }

}
