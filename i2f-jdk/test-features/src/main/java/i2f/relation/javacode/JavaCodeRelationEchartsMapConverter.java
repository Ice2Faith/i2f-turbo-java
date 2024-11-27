package i2f.relation.javacode;

import i2f.relation.javacode.data.JavaCodeMeta;
import i2f.relation.javacode.data.JavaCodeRelation;
import i2f.relation.javacode.data.JavaCodeRelationMap;
import i2f.relation.javacode.data.JavaNodeType;
import i2f.relation.javacode.data.echcarts.EchartsMapCategories;
import i2f.relation.javacode.data.echcarts.EchartsMapLinks;
import i2f.relation.javacode.data.echcarts.EchartsMapNodes;
import i2f.relation.javacode.data.echcarts.JavaCodeEchartsMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/11/27 21:30
 * @desc
 */
public class JavaCodeRelationEchartsMapConverter {
    public static JavaCodeEchartsMap convert(JavaCodeRelationMap relationMap) {
        JavaCodeEchartsMap ret = new JavaCodeEchartsMap();
        int index = 0;
        List<EchartsMapCategories> categories = new ArrayList<>();
        Map<JavaNodeType, Integer> categoriesIndexMap = new HashMap<>();
        index = 0;
        for (JavaNodeType item : JavaNodeType.values()) {
            EchartsMapCategories vo = new EchartsMapCategories();
            vo.setName(item.name());
            categories.add(vo);
            categoriesIndexMap.put(item, index);
            index++;
        }

        Map<String, Integer> nodeIndexMap = new HashMap<>();
        List<EchartsMapNodes> nodes = new ArrayList<>();
        index = 0;
        for (Map.Entry<String, JavaCodeMeta> entry : relationMap.getNodeMap().entrySet()) {
            JavaCodeMeta meta = entry.getValue();
            EchartsMapNodes vo = new EchartsMapNodes();
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

        List<EchartsMapLinks> links = new ArrayList<>();
        for (JavaCodeRelation relation : relationMap.getRelations()) {
            EchartsMapLinks vo = new EchartsMapLinks();
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
