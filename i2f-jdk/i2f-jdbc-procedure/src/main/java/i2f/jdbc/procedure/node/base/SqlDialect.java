package i2f.jdbc.procedure.node.base;

import i2f.bindsql.BindSql;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
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
    public static final String TAG_NAME = TagConsts.SQL_DIALECT;

    public static BindSql getSqlDialectList(String datasource, XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        if (datasource == null) {
            datasource = (String) executor.attrValue(AttrConsts.DATASOURCE, FeatureConsts.STRING, node, context);
        }
        List<Map.Entry<String, Object>> dialectScriptList = new ArrayList<>();
        List<XmlNode> children = node.getChildren();
        if (children != null) {
            for (XmlNode item : children) {
                if (TAG_NAME.equals(item.getTagName())) {
                    String databases = item.getTagAttrMap().get(AttrConsts.DATABASES);
                    Object scriptObj = executor.attrValue(AttrConsts.SCRIPT, FeatureConsts.VISIT, node, context);
                    if (scriptObj != null) {
                        if (scriptObj instanceof BindSql) {
                            BindSql bindSql = (BindSql) scriptObj;
                            dialectScriptList.add(new AbstractMap.SimpleEntry<>(databases, bindSql));
                        } else {
                            String script = String.valueOf(scriptObj);
                            dialectScriptList.add(new AbstractMap.SimpleEntry<>(databases, script));
                        }
                    }
                    if (dialectScriptList.isEmpty()) {
                        dialectScriptList.add(new AbstractMap.SimpleEntry<>(databases, node.getTagBody()));
                    }
                }
            }
        }

        if (dialectScriptList.isEmpty()) {
            Object scriptObj = executor.attrValue(AttrConsts.SCRIPT, FeatureConsts.VISIT, node, context);
            if (scriptObj != null) {
                if (scriptObj instanceof BindSql) {
                    BindSql bindSql = (BindSql) scriptObj;
                    dialectScriptList.add(new AbstractMap.SimpleEntry<>(null, bindSql));
                } else {
                    String script = String.valueOf(scriptObj);
                    dialectScriptList.add(new AbstractMap.SimpleEntry<>(null, script));
                }
            }
            if (dialectScriptList.isEmpty()) {
                dialectScriptList.add(new AbstractMap.SimpleEntry<>(null, node.getTagBody()));
            }
        }


        BindSql bql = executor.sqlScript(datasource, dialectScriptList, context);

        return bql;
    }
}
