package i2f.relation.javacode.test;

import i2f.convert.obj.ObjectConvertor;
import i2f.relation.javacode.JavaCodeNodeResolver;
import i2f.relation.javacode.JavaCodeRelationConverter;
import i2f.relation.javacode.JavaCodeRelationEchartsMapConverter;
import i2f.relation.javacode.data.JavaCodeNode;
import i2f.relation.javacode.data.JavaCodeRelationMap;
import i2f.relation.javacode.data.echcarts.JavaCodeEchartsMap;
import i2f.serialize.str.json.impl.Json2;

import java.util.Arrays;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/11/27 21:47
 * @desc
 */
public class TestJavaCode {
    public static void main(String[] args) {
        System.out.println("====================");
        List<JavaCodeNode> nodes = JavaCodeNodeResolver.parse(Arrays.asList(ObjectConvertor.class));
//        System.out.println(Json2.toJson(nodes));

        System.out.println("====================");
        JavaCodeRelationMap map = JavaCodeRelationConverter.convert(nodes);
//        System.out.println(Json2.toJson(map));

        System.out.println("====================");
        JavaCodeEchartsMap echarts = JavaCodeRelationEchartsMapConverter.convert(map);
        System.out.println(Json2.toJson(echarts));
    }
}
