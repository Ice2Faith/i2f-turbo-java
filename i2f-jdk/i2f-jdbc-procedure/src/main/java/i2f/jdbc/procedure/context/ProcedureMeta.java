package i2f.jdbc.procedure.context;

import i2f.jdbc.procedure.annotations.JdbcProcedure;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.executor.JdbcProcedureJavaCaller;
import i2f.jdbc.procedure.parser.JdbcProcedureParser;
import i2f.jdbc.procedure.parser.data.XmlNode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * @author Ice2Faith
 * @date 2025/3/5 20:45
 * @desc
 */
@Data
@NoArgsConstructor
public class ProcedureMeta {
    protected Type type;
    protected String name;
    protected Object target;
    protected List<String> arguments = new ArrayList<>();
    protected Map<String, List<String>> argumentFeatures = new LinkedHashMap<>();

    public static ProcedureMeta ofMeta(XmlNode value) {
        return ofMeta(null, value);
    }

    public static ProcedureMeta ofMeta(String key, XmlNode value) {
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
        meta.setType(Type.XML);
        meta.setName(key);
        meta.setTarget(value);
        meta.setArguments(new ArrayList<>());
        meta.setArgumentFeatures(new HashMap<>());
        for (String attr : value.getTagAttrMap().keySet()) {
            if (AttrConsts.ID.equals(attr)) {
                continue;
            }
            meta.getArguments().add(attr);
            meta.getArgumentFeatures().put(attr, new ArrayList<>(value.getAttrFeatureMap().get(attr)));
        }
        meta.setArguments(new ArrayList<>(value.getTagAttrMap().keySet()));
        meta.setArgumentFeatures(value.getAttrFeatureMap());
        return meta;
    }

    public static ProcedureMeta ofMeta(JdbcProcedureJavaCaller value) {
        return ofMeta(null, value);
    }

    public static ProcedureMeta ofMeta(String key, JdbcProcedureJavaCaller value) {
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
        meta.setType(Type.JAVA);
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

    public static enum Type {
        XML,
        JAVA
    }

}
