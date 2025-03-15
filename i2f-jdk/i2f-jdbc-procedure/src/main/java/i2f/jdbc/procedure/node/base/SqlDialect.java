package i2f.jdbc.procedure.node.base;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/23 11:22
 */
public class SqlDialect {
    public static final String TAG_NAME = "sql-dialect";

    public static List<Map.Entry<String, String>> getSqlDialectList(XmlNode node, Map<String,Object> context, JdbcProcedureExecutor executor) {
        List<Map.Entry<String, String>> dialectScriptList = new ArrayList<>();
        List<XmlNode> children = node.getChildren();
        if (children != null) {
            for (XmlNode item : children) {
                if (TAG_NAME.equals(item.getTagName())) {
                    String databases = item.getTagAttrMap().get(AttrConsts.DATABASES);
                    String script = (String) executor.attrValue(AttrConsts.SCRIPT, FeatureConsts.VISIT, item, context);
                    if (script == null || script.isEmpty()) {
                        script = item.getTagBody();
                    }
                    dialectScriptList.add(new AbstractMap.SimpleEntry<>(databases, script));
                }
            }
        }
        return dialectScriptList;
    }
}
