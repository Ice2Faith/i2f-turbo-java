package i2f.javacode.graph;

import i2f.javacode.graph.data.JavaCodeNode;
import i2f.javacode.graph.data.JavaCodeRelation;
import i2f.javacode.graph.data.JavaCodeRelationGraph;
import i2f.javacode.graph.data.JavaRelationType;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/11/27 21:30
 * @desc
 */
public class JavaCodeRelationGraphConverter {
    protected JavaCodeRelationGraph relationMap = new JavaCodeRelationGraph();

    public static JavaCodeRelationGraph convert(List<JavaCodeNode> nodes) {
        JavaCodeRelationGraphConverter converter = new JavaCodeRelationGraphConverter();
        converter.resolve(nodes);
        return converter.relationMap;
    }

    public void resolve(List<JavaCodeNode> nodes) {
        for (JavaCodeNode node : nodes) {
            resolve(node);
        }
    }

    public void resolve(JavaCodeNode node) {
        if (relationMap.getNodeMap().containsKey(node.getSignature())) {
            return;
        }
        relationMap.getNodeMap().put(node.getSignature(), node.copyMeta());
        JavaCodeNode realType = node.getRealType();
        if (realType != null) {
            JavaCodeRelation relation = new JavaCodeRelation();
            relation.setStartSignature(node.getSignature());
            relation.setEndSignature(realType.getSignature());
            relation.setRelationType(JavaRelationType.REAL);
            relationMap.getRelations().add(relation);
            resolve(realType);
        }
        List<JavaCodeNode> fields = node.getFields();
        if (fields != null) {
            for (JavaCodeNode field : fields) {
                JavaCodeRelation relation = new JavaCodeRelation();
                relation.setStartSignature(node.getSignature());
                relation.setEndSignature(field.getSignature());
                relation.setRelationType(JavaRelationType.FIELD);
                relationMap.getRelations().add(relation);
                resolve(field);
            }
        }
        List<JavaCodeNode> methods = node.getMethods();
        if (methods != null) {
            for (JavaCodeNode method : methods) {
                JavaCodeRelation relation = new JavaCodeRelation();
                relation.setStartSignature(node.getSignature());
                relation.setEndSignature(method.getSignature());
                relation.setRelationType(JavaRelationType.METHOD);
                relationMap.getRelations().add(relation);
                resolve(method);
            }
        }
        List<JavaCodeNode> constructors = node.getConstructors();
        if (constructors != null) {
            for (JavaCodeNode constructor : constructors) {
                JavaCodeRelation relation = new JavaCodeRelation();
                relation.setStartSignature(node.getSignature());
                relation.setEndSignature(constructor.getSignature());
                relation.setRelationType(JavaRelationType.CONSTRUCTOR);
                relationMap.getRelations().add(relation);
                resolve(constructor);
            }
        }
        JavaCodeNode superClass = node.getSuperClass();
        if (superClass != null) {
            JavaCodeRelation relation = new JavaCodeRelation();
            relation.setStartSignature(node.getSignature());
            relation.setEndSignature(superClass.getSignature());
            relation.setRelationType(JavaRelationType.SUPER);
            relationMap.getRelations().add(relation);
            resolve(superClass);
        }
        List<JavaCodeNode> interfaces = node.getInterfaces();
        if (interfaces != null) {
            for (JavaCodeNode inter : interfaces) {
                JavaCodeRelation relation = new JavaCodeRelation();
                relation.setStartSignature(node.getSignature());
                relation.setEndSignature(inter.getSignature());
                relation.setRelationType(JavaRelationType.INTERFACE);
                relationMap.getRelations().add(relation);
                resolve(inter);
            }
        }
        List<JavaCodeNode> annotations = node.getAnnotations();
        if (annotations != null) {
            for (JavaCodeNode ann : annotations) {
                JavaCodeRelation relation = new JavaCodeRelation();
                relation.setStartSignature(node.getSignature());
                relation.setEndSignature(ann.getSignature());
                relation.setRelationType(JavaRelationType.ANNOTATION);
                relationMap.getRelations().add(relation);
                resolve(ann);
            }
        }
        List<JavaCodeNode> parameters = node.getParameters();
        if (parameters != null) {
            for (JavaCodeNode parameter : parameters) {
                JavaCodeRelation relation = new JavaCodeRelation();
                relation.setStartSignature(node.getSignature());
                relation.setEndSignature(parameter.getSignature());
                relation.setRelationType(JavaRelationType.PARAMETER);
                relationMap.getRelations().add(relation);
                resolve(parameter);
            }
        }
        JavaCodeNode returnType = node.getReturnType();
        if (returnType != null) {
            JavaCodeRelation relation = new JavaCodeRelation();
            relation.setStartSignature(node.getSignature());
            relation.setEndSignature(returnType.getSignature());
            relation.setRelationType(JavaRelationType.RETURN);
            relationMap.getRelations().add(relation);
            resolve(returnType);
        }
        List<JavaCodeNode> exceptions = node.getExceptions();
        if (exceptions != null) {
            for (JavaCodeNode except : exceptions) {
                JavaCodeRelation relation = new JavaCodeRelation();
                relation.setStartSignature(node.getSignature());
                relation.setEndSignature(except.getSignature());
                relation.setRelationType(JavaRelationType.EXCEPTION);
                relationMap.getRelations().add(relation);
                resolve(except);
            }
        }
        List<JavaCodeNode> genericParameters = node.getGenericParameters();
        if (genericParameters != null) {
            for (JavaCodeNode generic : genericParameters) {
                JavaCodeRelation relation = new JavaCodeRelation();
                relation.setStartSignature(node.getSignature());
                relation.setEndSignature(generic.getSignature());
                relation.setRelationType(JavaRelationType.GENERIC);
                relationMap.getRelations().add(relation);
                resolve(generic);
            }
        }
    }
}
