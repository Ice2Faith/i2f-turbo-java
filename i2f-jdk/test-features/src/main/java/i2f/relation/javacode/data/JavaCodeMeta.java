package i2f.relation.javacode.data;

import lombok.Data;

import java.lang.reflect.Type;

/**
 * @author Ice2Faith
 * @date 2024/11/27 21:54
 * @desc
 */
@Data
public class JavaCodeMeta {
    protected JavaNodeType nodeType;
    protected String signature;
    protected String name;
    protected Type type;

    public JavaCodeMeta copyMeta() {
        return copyMeta(this);
    }

    public static <T extends JavaCodeMeta> JavaCodeMeta copyMeta(T val) {
        JavaCodeMeta copy = new JavaCodeMeta();
        copy.nodeType = val.nodeType;
        copy.signature = val.signature;
        copy.name = val.name;
        copy.type = val.type;
        return copy;
    }
}
