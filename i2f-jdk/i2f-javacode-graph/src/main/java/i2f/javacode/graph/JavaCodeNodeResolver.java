package i2f.javacode.graph;

import i2f.javacode.graph.data.JavaCodeNode;
import i2f.javacode.graph.data.JavaNodeType;
import i2f.reflect.ReflectResolver;
import i2f.typeof.TypeOf;
import i2f.typeof.token.TypeToken;
import i2f.typeof.token.data.TypeNode;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2024/11/27 19:13
 * @desc
 */
public class JavaCodeNodeResolver {

    protected Map<String, JavaCodeNode> nodeMap = new HashMap<>();
    protected boolean dropObject = false;
    protected Predicate<Class<?>> classFilter;
    protected Predicate<Field> fieldFiler;
    protected Predicate<Method> methodFilter;
    protected Predicate<Constructor<?>> constructorFilter;
    protected Predicate<Annotation> annotationFilter;

    public static final Predicate<Class<?>> defaultClassFilter = (clazz) -> {
        if (clazz == null) {
            return false;
        }
        if (clazz.isAnonymousClass()) {
            return false;
        }
        int modifiers = clazz.getModifiers();
        if (!Modifier.isPublic(modifiers)) {
            return false;
        }
        return true;
    };
    public static final Predicate<Field> defaultFieldFiler = (field) -> {
        if (field == null) {
            return false;
        }
        int modifiers = field.getModifiers();
        if (Modifier.isTransient(modifiers)) {
            return false;
        }
        if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
            return false;
        }
        return true;
    };
    public static final Predicate<Method> defaultMethodFilter = (method) -> {
        if (method == null) {
            return false;
        }
        if (method.getName().startsWith("lambda$")) {
            return false;
        }
        int modifiers = method.getModifiers();
        if (Modifier.isPrivate(modifiers)) {
            return false;
        }
        return true;
    };
    public static final Predicate<Constructor<?>> defaultConstructorFilter = (constructor) -> {
        if (constructor == null) {
            return false;
        }
        int modifiers = constructor.getModifiers();
        if (Modifier.isPrivate(modifiers)) {
            return false;
        }
        return true;
    };

    public JavaCodeNodeResolver withDefault() {
        this.dropObject = true;
        this.classFilter = defaultClassFilter;
        this.fieldFiler = defaultFieldFiler;
        this.methodFilter = defaultMethodFilter;
        this.constructorFilter = defaultConstructorFilter;
        this.annotationFilter = null;
        return this;
    }

    public List<JavaCodeNode> parse(Collection<Class<?>> rootClass) {
        List<JavaCodeNode> ret = new ArrayList<>();
        for (Class<?> clazz : rootClass) {
            JavaCodeNode nextNode = parse(clazz, null);
            if (nextNode != null) {
                ret.add(nextNode);
            }
        }
        return ret;
    }

    public JavaCodeNode parse(Type genType, JavaNodeType type, boolean notFilter) {
        if (genType == null) {
            return null;
        }
        if (Class.class.equals(genType)) {
            Class<?> clazz = (Class<?>) genType;
            return parse(clazz, type, notFilter);
        }
        TypeNode fullType = TypeToken.getFullGenericType(genType);
        return parse(fullType, type, notFilter);
    }

    public JavaCodeNode parse(TypeNode typeNode, JavaNodeType type, boolean notFilter) {
        if (typeNode == null) {
            return null;
        }
        Class<?> clazz = typeNode.getType();
        if (!notFilter) {
            if (dropObject && Object.class.equals(clazz)) {
                return null;
            }
        }
        String signature = typeNode.fullName();
        if (nodeMap.containsKey(signature)) {
            return nodeMap.get(signature);
        }
        if (type == null) {
            if (typeNode.getArgs() == null || typeNode.getArgs().isEmpty()) {
                type = JavaNodeType.CLASS;
            } else {
                type = JavaNodeType.GENERIC;
            }
        }

        JavaCodeNode node = new JavaCodeNode();
        node.setNodeType(type);
        node.setSignature(signature);
        node.setName(typeNode.simpleName());
        node.setType(typeNode.getType());
        Class<?> realType = typeNode.getType();
        if (realType != null) {
            if (classFilter == null || classFilter.test(realType)) {
                JavaCodeNode nextNode = parse(realType, JavaNodeType.CLASS, false);
                if (nextNode != null) {
                    node.setRealType(nextNode);
                }
            }
        }
        nodeMap.put(node.getSignature(), node);

        List<TypeNode> args = typeNode.getArgs();
        if (args != null) {
            for (TypeNode arg : args) {
                JavaCodeNode nextNode = parse(arg, JavaNodeType.GENERIC, true);
                if (nextNode != null) {
                    node.getGenericParameters().add(nextNode);
                }
            }
        }

        return node;
    }

    public JavaCodeNode parse(Class<?> clazz, JavaNodeType type) {
        return parse(clazz, type, false);
    }

    public JavaCodeNode parse(Class<?> clazz, JavaNodeType type, boolean notFilter) {
        if (clazz == null) {
            return null;
        }
        if (!notFilter) {
            if (classFilter != null && !classFilter.test(clazz)) {
                return null;
            }
            if (dropObject && Object.class.equals(clazz)) {
                return null;
            }
        }
        String signature = clazz.getName();
        if (nodeMap.containsKey(signature)) {
            return nodeMap.get(signature);
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
                    if (fieldFiler == null || fieldFiler.test(field)) {
                        JavaCodeNode nextNode = parse(field, JavaNodeType.FIELD);
                        if (nextNode != null) {
                            node.getFields().add(nextNode);
                        }
                    }
                }
            }

            Method[] methods = clazz.getDeclaredMethods();
            if (methods != null) {
                for (Method method : methods) {
                    if (methodFilter == null || methodFilter.test(method)) {
                        JavaCodeNode nextNode = parse(method, JavaNodeType.METHOD);
                        if (nextNode != null) {
                            node.getMethods().add(nextNode);
                        }
                    }
                }
            }

            Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            if (constructors != null) {
                for (Constructor<?> constructor : constructors) {
                    if (constructorFilter == null || constructorFilter.test(constructor)) {
                        JavaCodeNode nextNode = parse(constructor, JavaNodeType.CONSTRUCTOR);
                        if (nextNode != null) {
                            node.getConstructors().add(nextNode);
                        }
                    }
                }
            }

            Annotation[] annotations = clazz.getDeclaredAnnotations();
            if (annotations != null) {
                for (Annotation annotation : annotations) {
                    if (annotationFilter == null || annotationFilter.test(annotation)) {
                        JavaCodeNode nextNode = parse(annotation, JavaNodeType.ANNOTATION);
                        if (nextNode != null) {
                            node.getAnnotations().add(nextNode);
                        }
                    }
                }
            }
        }

        if (Object.class.equals(clazz)) {
            return node;
        }


        Type genericSuperclass = clazz.getGenericSuperclass();
        Type[] genericInterfaces = clazz.getGenericInterfaces();

        boolean hasGenericSuperClass = TypeOf.isGenericType(genericSuperclass);
        boolean hasGenericInterfaces = TypeOf.hasGenericType(genericInterfaces);

        if (jdkClass && !(hasGenericSuperClass || hasGenericInterfaces)) {
            return node;
        }

        if (hasGenericSuperClass) {
            JavaCodeNode nextNode = parse(genericSuperclass, JavaNodeType.SUPER, false);
            if (nextNode != null) {
                node.setSuperClass(nextNode);
            }
        } else {
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null) {
                boolean doNext = true;
                if (classFilter == null || classFilter.test(superclass)) {
                    doNext = true;
                }
                if (dropObject && Object.class.equals(superclass)) {
                    doNext = false;
                }
                if (doNext) {
                    JavaCodeNode nextNode = parse(superclass, JavaNodeType.SUPER, false);
                    if (nextNode != null) {
                        node.setSuperClass(nextNode);
                    }
                }
            }
        }


        if (hasGenericInterfaces) {
            for (Type anInterface : genericInterfaces) {
                JavaCodeNode nextNode = parse(anInterface, JavaNodeType.INTERFACE, false);
                if (nextNode != null) {
                    node.getInterfaces().add(nextNode);
                }
            }
        } else {
            Class<?>[] interfaces = clazz.getInterfaces();
            if (interfaces != null) {
                for (Class<?> anInterface : interfaces) {
                    if (classFilter == null || classFilter.test(anInterface)) {
                        JavaCodeNode nextNode = parse(anInterface, JavaNodeType.INTERFACE, false);
                        if (nextNode != null) {
                            node.getInterfaces().add(nextNode);
                        }
                    }
                }
            }
        }
        return node;
    }

    public JavaCodeNode parse(Field field, JavaNodeType type) {
        if (field == null) {
            return null;
        }
        if (fieldFiler != null && !fieldFiler.test(field)) {
            return null;
        }
        String signature = field.getDeclaringClass().getName() + "." + field.getName();
        if (nodeMap.containsKey(signature)) {
            return nodeMap.get(signature);
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

        Type genericType = field.getGenericType();
        if (TypeOf.isGenericType(genericType)) {
            JavaCodeNode nextNode = parse(genericType, JavaNodeType.TYPE, false);
            if (nextNode != null) {
                node.setRealType(nextNode);
            }
        } else {
            Class<?> nextType = field.getType();
            if (nextType != null) {
                if (classFilter == null || classFilter.test(nextType)) {
                    JavaCodeNode nextNode = parse(nextType, JavaNodeType.CLASS, false);
                    if (nextNode != null) {
                        node.setRealType(nextNode);
                    }
                }
            }
        }

        Annotation[] annotations = field.getDeclaredAnnotations();
        if (annotations != null) {
            for (Annotation annotation : annotations) {
                if (annotationFilter == null || annotationFilter.test(annotation)) {
                    JavaCodeNode nextNode = parse(annotation, JavaNodeType.ANNOTATION);
                    if (nextNode != null) {
                        node.getAnnotations().add(nextNode);
                    }
                }
            }
        }

        return node;
    }


    public JavaCodeNode parse(Method method, JavaNodeType type) {
        if (method == null) {
            return null;
        }
        if (methodFilter != null && !methodFilter.test(method)) {
            return null;
        }
        String signature = method.getDeclaringClass().getName() + "." + method.getName() + "() -> " + method.toGenericString();
        if (nodeMap.containsKey(signature)) {
            return nodeMap.get(signature);
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
                if (annotationFilter == null || annotationFilter.test(annotation)) {
                    JavaCodeNode nextNode = parse(annotation, JavaNodeType.ANNOTATION);
                    if (nextNode != null) {
                        node.getAnnotations().add(nextNode);
                    }
                }
            }
        }

        Type[] genericParameterTypes = method.getGenericParameterTypes();
        if (TypeOf.hasGenericType(genericParameterTypes)) {
            for (Type parameter : genericParameterTypes) {
                JavaCodeNode nextNode = parse(parameter, JavaNodeType.PARAMETER, true);
                if (nextNode != null) {
                    node.getParameters().add(nextNode);
                }
            }
        } else {
            Class<?>[] parameters = method.getParameterTypes();
            if (parameters != null) {
                for (Class<?> parameter : parameters) {
                    JavaCodeNode nextNode = parse(parameter, JavaNodeType.PARAMETER, true);
                    if (nextNode != null) {
                        node.getParameters().add(nextNode);
                    }
                }
            }
        }

        Class<?>[] exceptionTypes = method.getExceptionTypes();
        if (exceptionTypes != null) {
            for (Class<?> exceptionType : exceptionTypes) {
                JavaCodeNode nextNode = parse(exceptionType, JavaNodeType.EXCEPTION, false);
                if (nextNode != null) {
                    node.getParameters().add(nextNode);
                }
            }
        }

        Type genericReturnType = method.getGenericReturnType();
        if (TypeOf.isGenericType(genericReturnType)) {
            JavaCodeNode nextNode = parse(genericReturnType, JavaNodeType.RETURN, true);
            if (nextNode != null) {
                node.setReturnType(nextNode);
            }
        } else {
            Class<?> returnType = method.getReturnType();
            JavaCodeNode nextNode = parse(returnType, JavaNodeType.RETURN, true);
            if (nextNode != null) {
                node.setReturnType(nextNode);
            }
        }

        return node;
    }


    public JavaCodeNode parse(Constructor<?> constructor, JavaNodeType type) {
        if (constructor == null) {
            return null;
        }
        if (constructorFilter != null && !constructorFilter.test(constructor)) {
            return null;
        }
        String signature = constructor.getDeclaringClass().getName() + "." + "<init>" + "() -> " + constructor.toGenericString();
        if (nodeMap.containsKey(signature)) {
            return nodeMap.get(signature);
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
                if (annotationFilter == null || annotationFilter.test(annotation)) {
                    JavaCodeNode nextNode = parse(annotation, JavaNodeType.ANNOTATION);
                    if (nextNode != null) {
                        node.getAnnotations().add(nextNode);
                    }
                }
            }
        }

        Type[] genericParameterTypes = constructor.getGenericParameterTypes();
        if (TypeOf.hasGenericType(genericParameterTypes)) {
            for (Type parameter : genericParameterTypes) {
                JavaCodeNode nextNode = parse(parameter, JavaNodeType.PARAMETER, true);
                if (nextNode != null) {
                    node.getParameters().add(nextNode);
                }
            }
        } else {
            Class<?>[] parameters = constructor.getParameterTypes();
            if (parameters != null) {
                for (Class<?> parameter : parameters) {
                    JavaCodeNode nextNode = parse(parameter, JavaNodeType.PARAMETER, true);
                    if (nextNode != null) {
                        node.getParameters().add(nextNode);
                    }
                }
            }
        }


        Class<?>[] exceptionTypes = constructor.getExceptionTypes();
        if (exceptionTypes != null) {
            for (Class<?> exceptionType : exceptionTypes) {
                JavaCodeNode nextNode = parse(exceptionType, JavaNodeType.EXCEPTION, false);
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
        if (annotationFilter != null && !annotationFilter.test(annotation)) {
            return null;
        }
        String signature = annotation.annotationType().getName();
        if (nodeMap.containsKey(signature)) {
            return nodeMap.get(signature);
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

        Annotation[] nextAnnotations = annotationType.getDeclaredAnnotations();
        if (nextAnnotations != null) {
            for (Annotation nextAnnotation : nextAnnotations) {
                if (annotationFilter == null || annotationFilter.test(nextAnnotation)) {
                    JavaCodeNode nextNode = parse(annotation, JavaNodeType.ANNOTATION);
                    if (nextNode != null) {
                        node.getAnnotations().add(nextNode);
                    }
                }
            }
        }


        return node;
    }
}
