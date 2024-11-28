package i2f.javacode.graph.test;

import i2f.javacode.graph.JavaCodeNodeResolver;
import i2f.javacode.graph.JavaCodeRelationEchartsGraphConverter;
import i2f.javacode.graph.JavaCodeRelationGraphConverter;
import i2f.javacode.graph.data.JavaCodeNode;
import i2f.javacode.graph.data.JavaCodeRelationGraph;
import i2f.javacode.graph.data.echcarts.JavaCodeEchartsGraph;
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
        List<JavaCodeNode> nodes = JavaCodeNodeResolver.parse(Arrays.asList(JavaCodeNodeResolver.class, JavaCodeRelationEchartsGraphConverter.class, JavaCodeRelationGraphConverter.class));

        System.out.println("====================");
        JavaCodeRelationGraph map = JavaCodeRelationGraphConverter.convert(nodes);
//        System.out.println(Json2.toJson(map));

        System.out.println("====================");
        JavaCodeEchartsGraph echarts = JavaCodeRelationEchartsGraphConverter.convert(map);
        System.out.println(Json2.toJson(echarts));
        // 此结果，可使用这个页面进行预览：i2f-turbo-web/tools/echarts/echarts-graph.html
    }
}
