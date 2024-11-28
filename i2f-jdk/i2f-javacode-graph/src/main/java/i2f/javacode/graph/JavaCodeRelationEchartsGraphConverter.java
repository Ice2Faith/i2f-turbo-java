package i2f.javacode.graph;

import i2f.javacode.graph.data.JavaCodeMeta;
import i2f.javacode.graph.data.JavaCodeRelation;
import i2f.javacode.graph.data.JavaCodeRelationGraph;
import i2f.javacode.graph.data.JavaNodeType;
import i2f.javacode.graph.data.echcarts.EchartsGraphCategories;
import i2f.javacode.graph.data.echcarts.EchartsGraphLinks;
import i2f.javacode.graph.data.echcarts.EchartsGraphNodes;
import i2f.javacode.graph.data.echcarts.JavaCodeEchartsGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/11/27 21:30
 * @desc
 */
public class JavaCodeRelationEchartsGraphConverter {
    public static JavaCodeEchartsGraph convert(JavaCodeRelationGraph relationMap) {
        JavaCodeEchartsGraph ret = new JavaCodeEchartsGraph();
        int index = 0;
        List<EchartsGraphCategories> categories = new ArrayList<>();
        Map<JavaNodeType, Integer> categoriesIndexMap = new HashMap<>();
        index = 0;
        for (JavaNodeType item : JavaNodeType.values()) {
            EchartsGraphCategories vo = new EchartsGraphCategories();
            vo.setName(item.name());
            categories.add(vo);
            categoriesIndexMap.put(item, index);
            index++;
        }

        Map<String, Integer> nodeIndexMap = new HashMap<>();
        List<EchartsGraphNodes> nodes = new ArrayList<>();
        index = 0;
        for (Map.Entry<String, JavaCodeMeta> entry : relationMap.getNodeMap().entrySet()) {
            JavaCodeMeta meta = entry.getValue();
            EchartsGraphNodes vo = new EchartsGraphNodes();
            vo.setCategory(categoriesIndexMap.get(meta.getNodeType()));
            vo.setId(entry.getKey());
            vo.setName(meta.getName());
            vo.setSymbolSize(20);
            vo.setValue(0);
            vo.setX(0);
            vo.setY(0);
            nodes.add(vo);
            nodeIndexMap.put(entry.getKey(), index);
            index++;
        }

        List<EchartsGraphLinks> links = new ArrayList<>();
        for (JavaCodeRelation relation : relationMap.getRelations()) {
            EchartsGraphLinks vo = new EchartsGraphLinks();
            vo.setSource(nodeIndexMap.get(relation.getStartSignature()));
            vo.setTarget(nodeIndexMap.get(relation.getEndSignature()));
            vo.setValue(relation.getRelationType().name());
            links.add(vo);
        }

        ret.setCategories(categories);
        ret.setNodes(nodes);
        ret.setLinks(links);
        return ret;
    }

}
