package i2f.jdbc.procedure.node.impl;

import i2f.ai.std.ChatAi;
import i2f.ai.std.ChatAiProvider;
import i2f.context.std.INamingContext;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangAiNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.LANG_AI;

    @Override
    public String tag() {
        return TAG_NAME;
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        Object value = executor.attrValue(AttrConsts.VALUE, FeatureConsts.VISIT, node, context);
        String type = (String) executor.attrValue(AttrConsts.TYPE, FeatureConsts.STRING, node, context);

        if (value == null) {
            value = "";
        }
        String question = String.valueOf(value);
        if (question == null || question.isEmpty()) {
            question = node.getTextBody();
        }

        INamingContext namingContext = executor.getNamingContext();
        if (namingContext == null) {
            throw new ThrowSignalException("missing naming context to find chat ai providers");
        }
        List<ChatAiProvider> providers = namingContext.getBeans(ChatAiProvider.class);
        if (providers == null || providers.isEmpty()) {
            throw new ThrowSignalException("not found any chat ai providers");
        }
        ChatAiProvider provider = null;
        for (ChatAiProvider item : providers) {
            if (Objects.equals(item.name(), type)) {
                provider = item;
                break;
            }
        }
        if (provider == null) {
            provider = providers.get(0);
        }

        ChatAi chatAi = provider.getChatAi();
        if (chatAi == null) {
            throw new ThrowSignalException("provider cannot got an chat ai");
        }
        String answer = chatAi.chat(question);

        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        if (result != null) {
            Object val = executor.resultValue(answer, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
            executor.visitSet(context, result, val);
        }

    }
}
