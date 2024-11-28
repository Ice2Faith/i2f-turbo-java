package i2f.javacode.graph.data;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/11/27 21:49
 * @desc
 */
@Data
public class JavaCodeRelationGraph {
    protected Map<String, JavaCodeMeta> nodeMap = new HashMap<>();
    protected List<JavaCodeRelation> relations = new ArrayList<>();
}
