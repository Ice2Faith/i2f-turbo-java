package i2f.javacode.graph.data;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/11/27 19:14
 * @desc
 */
@Data
public class JavaCodeNode extends JavaCodeMeta {
    protected JavaCodeNode realType;
    protected List<JavaCodeNode> fields = new ArrayList<>();
    protected List<JavaCodeNode> methods = new ArrayList<>();
    protected List<JavaCodeNode> constructors = new ArrayList<>();
    protected JavaCodeNode superClass;
    protected List<JavaCodeNode> interfaces = new ArrayList<>();
    protected List<JavaCodeNode> annotations = new ArrayList<>();
    protected List<JavaCodeNode> parameters = new ArrayList<>();
    protected JavaCodeNode returnType;
    protected List<JavaCodeNode> exceptions = new ArrayList<>();
    protected List<JavaCodeNode> genericParameters = new ArrayList<>();
}
