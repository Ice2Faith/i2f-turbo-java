package i2f.jdbc.procedure.context;

import i2f.jdbc.procedure.annotations.JdbcProcedure;
import i2f.jdbc.procedure.caller.JdbcProcedureJavaCallerMapSupplier;
import i2f.jdbc.procedure.caller.JdbcProcedureNodeMapSupplier;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureJavaCaller;
import i2f.jdbc.procedure.parser.JdbcProcedureParser;
import i2f.jdbc.procedure.parser.data.XmlNode;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2025/3/5 20:50
 * @desc
 */
@Data
public class ProcedureContext extends CacheObjectRefresherSupplier<Map<String, ProcedureMeta>, ConcurrentHashMap<String, ProcedureMeta>> {
    protected JdbcProcedureNodeMapSupplier nodeSupplier;
    protected JdbcProcedureJavaCallerMapSupplier javaCallerSupplier;

    public ProcedureContext() {
        super(new ConcurrentHashMap<>(), "procedure-context-refresher");
    }

    public ProcedureContext(JdbcProcedureNodeMapSupplier nodeSupplier, JdbcProcedureJavaCallerMapSupplier javaCallerSupplier) {
        super(new ConcurrentHashMap<>(), "procedure-context-refresher");
        this.nodeSupplier = nodeSupplier;
        this.javaCallerSupplier = javaCallerSupplier;
    }

    public void registry(ProcedureMeta meta) {
        if (meta == null) {
            return;
        }
        String name = meta.getName();
        if (name == null) {
            return;
        }
        cache.put(name, meta);
    }

    public void registry(String key, XmlNode value) {
        ProcedureMeta meta = ofMeta(key, value);
        registry(meta);
    }

    public void registry(String key, JdbcProcedureJavaCaller value) {
        ProcedureMeta meta = ofMeta(key, value);
        registry(meta);
    }

    @Override
    public boolean isMissingCache() {
        return cache == null || cache.isEmpty();
    }

    @Override
    public Map<String, ProcedureMeta> wrapGet(ConcurrentHashMap<String, ProcedureMeta> ret) {
        return new HashMap<>(getCache());
    }

    public ProcedureMeta ofMeta(XmlNode value) {
        return ofMeta(null, value);
    }

    public ProcedureMeta ofMeta(String key, XmlNode value) {
        if (value == null) {
            return null;
        }
        if (key == null) {
            key = value.getTagAttrMap().get(AttrConsts.ID);
        }
        if (key == null || key.isEmpty()) {
            return null;
        }
        ProcedureMeta meta = new ProcedureMeta();
        meta.setType(ProcedureMeta.Type.XML);
        meta.setName(key);
        meta.setTarget(value);
        meta.setArguments(new ArrayList<>(value.getTagAttrMap().keySet()));
        meta.setArgumentFeatures(value.getAttrFeatureMap());
        return meta;
    }

    public ProcedureMeta ofMeta(JdbcProcedureJavaCaller value) {
        return ofMeta(null, value);
    }

    public ProcedureMeta ofMeta(String key, JdbcProcedureJavaCaller value) {
        if (value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        JdbcProcedure ann = clazz.getDeclaredAnnotation(JdbcProcedure.class);
        if (ann == null) {
            return null;
        }
        if (key == null) {
            key = ann.value();
        }
        if (key == null || key.isEmpty()) {
            return null;
        }
        ProcedureMeta meta = new ProcedureMeta();
        meta.setType(ProcedureMeta.Type.JAVA);
        meta.setName(key);
        meta.setTarget(value);
        meta.setArguments(new ArrayList<>());
        meta.setArgumentFeatures(new HashMap<>());

        String[] arguments = ann.arguments();
        for (String argument : arguments) {
            Map.Entry<String, List<String>> attrFeatures = JdbcProcedureParser.parseAttrFeatures(argument);
            String name = attrFeatures.getKey();
            meta.getArguments().add(name);
            meta.getArgumentFeatures().put(name, attrFeatures.getValue());
        }
        return meta;
    }

    @Override
    public void refresh() {
        Map<String, ProcedureMeta> metaMap = new HashMap<>();
        Map<String, XmlNode> nodeMap = nodeSupplier.getNodeMap();
        for (Map.Entry<String, XmlNode> entry : nodeMap.entrySet()) {
            String key = entry.getKey();
            XmlNode value = entry.getValue();
            ProcedureMeta meta = ofMeta(key, value);
            if (meta != null) {
                metaMap.put(meta.getName(), meta);
            }
        }
        Map<String, JdbcProcedureJavaCaller> javaCallerMap = javaCallerSupplier.getJavaCallerMap();
        for (Map.Entry<String, JdbcProcedureJavaCaller> entry : javaCallerMap.entrySet()) {
            String key = entry.getKey();
            JdbcProcedureJavaCaller value = entry.getValue();
            ProcedureMeta meta = ofMeta(key, value);
            if (meta != null) {
                metaMap.put(meta.getName(), meta);
            }
        }

        cache.putAll(metaMap);
    }
}
