package i2f.jdbc.procedure.node.impl;

import i2f.bindsql.BindSql;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.data.NamingOutputParameter;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.reporter.IGrammarReporter;

import java.sql.JDBCType;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class SqlCallNode extends AbstractExecutorNode {
    public static final String TAG_NAME = TagConsts.SQL_CALL;

    public static final Map<String,JDBCType> jdbcTypeMapping= Collections.unmodifiableMap(new HashMap<String,JDBCType>(){
        {
            put(FeatureConsts.STRING,JDBCType.VARCHAR);
            put(FeatureConsts.INT,JDBCType.INTEGER);
            put(FeatureConsts.DOUBLE,JDBCType.NUMERIC);
            put(FeatureConsts.FLOAT,JDBCType.NUMERIC);
            put(FeatureConsts.LONG,JDBCType.BIGINT);
            put(FeatureConsts.SHORT,JDBCType.SMALLINT);
            put(FeatureConsts.CHAR,JDBCType.CHAR);
            put(FeatureConsts.BYTE,JDBCType.INTEGER);
            put(FeatureConsts.BOOLEAN,JDBCType.BOOLEAN);
            put(FeatureConsts.DATE,JDBCType.TIMESTAMP);
        }
    });

    @Override
    public String tag() {
        return TAG_NAME;
    }

    @Override
    public void reportGrammar(IGrammarReporter reporter, XmlNode node, Consumer<String> warnPoster) {
        String refid = node.getTagAttrMap().get(AttrConsts.REFID);
        if (refid == null || refid.isEmpty()) {
            warnPoster.accept(TAG_NAME + " missing attribute " + AttrConsts.REFID);
        }
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        String datasource = (String) executor.attrValue(AttrConsts.DATASOURCE, FeatureConsts.STRING, node, context);
        String refid = (String) executor.attrValue(AttrConsts.REFID, FeatureConsts.STRING, node, context);
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);

        BindSql bql=BindSql.of(refid+"(");
        boolean isFirst=true;

        for (Map.Entry<String, String> entry : node.getTagAttrMap().entrySet()) {
            String name = entry.getKey();
            if (AttrConsts.REFID.equals(name)) {
                continue;
            }
            if (AttrConsts.PARAMS.equals(name)) {
                continue;
            }
            if (AttrConsts.PARAMS_SHARE.equals(name)) {
                continue;
            }
            if (AttrConsts.RESULT.equals(name)) {
                continue;
            }
            String script = entry.getValue();
            Object val = executor.attrValue(name, FeatureConsts.VISIT, node, context);

            if(!isFirst){
                bql=bql.add(",");
            }

            List<String> features = node.getAttrFeatureMap().get(name);
            boolean isOutput=false;
            boolean isOutputIn=false;
            JDBCType sqlType=null;

            if(features!=null){
                if(features.contains(FeatureConsts.OUT)){
                    isOutput=true;
                }
                if(features.contains(FeatureConsts.IN)){
                    isOutputIn=true;
                }
            }

            if(isOutput){
                if(sqlType==null) {
                    for (String feature : features) {
                        JDBCType jdbcType = jdbcTypeMapping.get(feature);
                        if (jdbcType != null) {
                            sqlType = jdbcType;
                            break;
                        }
                    }
                }
                if(sqlType==null){
                    JDBCType[] values = JDBCType.values();
                    for (String feature : features) {
                        for (JDBCType value : values) {
                            if(value.name().equalsIgnoreCase(feature)){
                                sqlType=value;
                                break;
                            }
                        }
                    }
                }
                if(sqlType==null){
                    sqlType=JDBCType.VARCHAR;
                }

                if(isOutputIn){
                    bql=bql.val(NamingOutputParameter.of(name,sqlType,val));
                }else{
                    bql=bql.val(NamingOutputParameter.of(name,sqlType));
                }
            }else{
                bql=bql.val(val);
            }

            isFirst=false;
        }

        bql=bql.add(")");

        bql.setSql(JdbcResolver.callSql(bql.getSql()));

        if (executor.isDebug()) {
            bql = bql.concat(getTrackingComment(node));
        }
        Map<String, Object> map = executor.sqlCall(datasource, bql,context);
        if (executor.isDebug()) {
            executor.logger().logDebug("sql call is : " + map.size() + "! at " + getNodeLocation(node));
        }
        if (result != null) {
            Object val = executor.resultValue(map, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
            executor.visitSet(context, result, val);
        }
    }

}
