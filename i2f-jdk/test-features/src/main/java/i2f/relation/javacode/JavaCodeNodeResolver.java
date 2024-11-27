package i2f.relation.javacode;

import i2f.reflect.ReflectResolver;
import i2f.relation.javacode.data.JavaCodeNode;
import i2f.relation.javacode.data.JavaNodeType;
import i2f.typeof.TypeOf;

import java.lang.annotation.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2024/11/27 19:13
 * @desc
 */
public class JavaCodeNodeResolver {

    protected Map<String, JavaCodeNode> nodeMap = new HashMap<>();

    public static List<JavaCodeNode> parse(Collection<Class<?>> rootClass) {
        List<JavaCodeNode> ret = new ArrayList<>();
        JavaCodeNodeResolver resolver = new JavaCodeNodeResolver();
        for (Class<?> clazz : rootClass) {
            JavaCodeNode nextNode = resolver.parse(clazz, null);
            if (nextNode != null) {
                ret.add(nextNode);
            }
        }
        return ret;
    }

    public JavaCodeNode parse(Class<?> clazz, JavaNodeType type) {
        if (clazz == null) {
            return null;
        }
        String signature = clazz.getName();
        if (nodeMap.containsKey(signature)) {
            return null;
        }
        if (type == null) {
            type = JavaNodeType.CLASS;
        }
        JavaCodeNode node = new JavaCodeNode();
        node.setNodeType(type);
        node.setSignature(signature);
        node.setName(clazz.getSimpleName());
        node.setType(clazz);
        node.setRealType(null);
        nodeMap.put(node.getSignature(), node);

        boolean jdkClass = ReflectResolver.isJdkClas(clazz);
        if (!jdkClass) {
            Field[] fields = clazz.getDeclaredFields();
            if (fields != null) {
                for (Field field : fields) {
                    JavaCodeNode nextNode = parse(field, JavaNodeType.FIELD);
                    if (nextNode != null) {
                        node.getFields().add(nextNode);
                    }
                }
            }

            Method[] methods = clazz.getDeclaredMethods();
            if (methods != null) {
                for (Method method : methods) {
                    JavaCodeNode nextNode = parse(method, JavaNodeType.METHOD);
                    if (nextNode != null) {
                        node.getMethods().add(nextNode);
                    }
                }
            }

            Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            if (constructors != null) {
                for (Constructor<?> constructor : constructors) {
                    JavaCodeNode nextNode = parse(constructor, JavaNodeType.CONSTRUCTOR);
                    if (nextNode != null) {
                        node.getConstructors().add(nextNode);
                    }
                }
            }

            Annotation[] annotations = clazz.getDeclaredAnnotations();
            if (annotations != null) {
                for (Annotation annotation : annotations) {
                    JavaCodeNode nextNode = parse(annotation, JavaNodeType.ANNOTATION);
                    if (nextNode != null) {
                        node.getAnnotations().add(nextNode);
                    }
                }
            }
        }

        if (Object.class.equals(clazz)) {
            return node;
        }

        if (jdkClass) {
            return node;
        }

        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            JavaCodeNode nextNode = parse(superclass, JavaNodeType.SUPER);
            if (nextNode != null) {
                node.setSuperClass(nextNode);
            }
        }
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces != null) {
            for (Class<?> anInterface : interfaces) {
                JavaCodeNode nextNode = parse(anInterface, JavaNodeType.INTERFACE);
                if (nextNode != null) {
                    node.getInterfaces().add(nextNode);
                }
            }
        }
        return node;
    }

    public JavaCodeNode parse(Field field, JavaNodeType type) {
        if (field == null) {
            return null;
        }
        String signature = field.getDeclaringClass().getName() + "." + field.getName();
        if (nodeMap.containsKey(signature)) {
            return null;
        }
        if (type == null) {
            type = JavaNodeType.FIELD;
        }
        JavaCodeNode node = new JavaCodeNode();
        node.setNodeType(type);
        node.setSignature(signature);
        node.setName(field.getName());
        node.setType(field.getType());
        nodeMap.put(node.getSignature(), node);

        Class<?> nextType = field.getType();
        if (nextType != null) {
            JavaCodeNode nextNode = parse(nextType, JavaNodeType.CLASS);
            if (nextNode != null) {
                node.setRealType(nextNode);
            }
        }

        Annotation[] annotations = field.getDeclaredAnnotations();
        if (annotations != null) {
            for (Annotation annotation : annotations) {
                JavaCodeNode nextNode = parse(annotation, JavaNodeType.ANNOTATION);
                if (nextNode != null) {
                    node.getAnnotations().add(nextNode);
                }
            }
        }

        return node;
    }


    public JavaCodeNode parse(Method method, JavaNodeType type) {
        if (method == null) {
            return null;
        }
        String signature = method.getDeclaringClass().getName() + "." + method.getName() + "() -> " + method.toGenericString();
        if (nodeMap.containsKey(signature)) {
            return null;
        }
        if (type == null) {
            type = JavaNodeType.METHOD;
        }
        JavaCodeNode node = new JavaCodeNode();
        node.setNodeType(type);
        node.setSignature(signature);
        node.setName(method.getName());
        node.setType(null);
        nodeMap.put(node.getSignature(), node);

        Annotation[] annotations = method.getDeclaredAnnotations();
        if (annotations != null) {
            for (Annotation annotation : annotations) {
                JavaCodeNode nextNode = parse(annotation, JavaNodeType.ANNOTATION);
                if (nextNode != null) {
                    node.getAnnotations().add(nextNode);
                }
            }
        }

        Class<?>[] parameters = method.getParameterTypes();
        if (parameters != null) {
            for (Class<?> parameter : parameters) {
                JavaCodeNode nextNode = parse(parameter, JavaNodeType.PARAMETER);
                if (nextNode != null) {
                    node.getParameters().add(nextNode);
                }
            }
        }

        Class<?>[] exceptionTypes = method.getExceptionTypes();
        if (exceptionTypes != null) {
            for (Class<?> exceptionType : exceptionTypes) {
                JavaCodeNode nextNode = parse(exceptionType, JavaNodeType.EXCEPTION);
                if (nextNode != null) {
                    node.getParameters().add(nextNode);
                }
            }
        }

        Class<?> returnType = method.getReturnType();
        JavaCodeNode nextNode = parse(returnType, JavaNodeType.RETURN);
        if (nextNode != null) {
            node.setReturnType(nextNode);
        }


        return node;
    }


    public JavaCodeNode parse(Constructor<?> constructor, JavaNodeType type) {
        if (constructor == null) {
            return null;
        }
        String signature = constructor.getDeclaringClass().getName() + "." + "<init>" + "() -> " + constructor.toGenericString();
        if (nodeMap.containsKey(signature)) {
            return null;
        }
        if (type == null) {
            type = JavaNodeType.CONSTRUCTOR;
        }
        JavaCodeNode node = new JavaCodeNode();
        node.setNodeType(type);
        node.setSignature(signature);
        node.setName(constructor.getName());
        node.setType(constructor.getDeclaringClass());
        nodeMap.put(node.getSignature(), node);

        Annotation[] annotations = constructor.getDeclaredAnnotations();
        if (annotations != null) {
            for (Annotation annotation : annotations) {
                JavaCodeNode nextNode = parse(annotation, JavaNodeType.ANNOTATION);
                if (nextNode != null) {
                    node.getAnnotations().add(nextNode);
                }
            }
        }

        Class<?>[] parameters = constructor.getParameterTypes();
        if (parameters != null) {
            for (Class<?> parameter : parameters) {
                JavaCodeNode nextNode = parse(parameter, JavaNodeType.PARAMETER);
                if (nextNode != null) {
                    node.getParameters().add(nextNode);
                }
            }
        }


        Class<?>[] exceptionTypes = constructor.getExceptionTypes();
        if (exceptionTypes != null) {
            for (Class<?> exceptionType : exceptionTypes) {
                JavaCodeNode nextNode = parse(exceptionType, JavaNodeType.EXCEPTION);
                if (nextNode != null) {
                    node.getParameters().add(nextNode);
                }
            }
        }


        return node;
    }

    public JavaCodeNode parse(Annotation annotation, JavaNodeType type) {
        if (annotation == null) {
            return null;
        }
        String signature = annotation.annotationType().getName();
        if (nodeMap.containsKey(signature)) {
            return null;
        }
        if (type == null) {
            type = JavaNodeType.ANNOTATION;
        }
        JavaCodeNode node = new JavaCodeNode();
        node.setNodeType(type);
        node.setSignature(signature);
        node.setName(annotation.annotationType().getName());
        node.setType(annotation.annotationType());
        nodeMap.put(node.getSignature(), node);

        Class<? extends Annotation> annotationType = annotation.annotationType();

        if (TypeOf.typeOf(annotationType, Documented.class)
                || TypeOf.typeOf(annotationType, Inherited.class)
                || TypeOf.typeOf(annotationType, Native.class)
                || TypeOf.typeOf(annotationType, Repeatable.class)
                || TypeOf.typeOf(annotationType, Retention.class)
                || TypeOf.typeOf(annotationType, Target.class)) {
            return node;
        }

        if (annotationType != null) {
            JavaCodeNode nextNode = parse(annotationType, JavaNodeType.ANNOTATION);
            if (nextNode != null) {
                node.setRealType(nextNode);
            }
        }

        return node;
    }
}
