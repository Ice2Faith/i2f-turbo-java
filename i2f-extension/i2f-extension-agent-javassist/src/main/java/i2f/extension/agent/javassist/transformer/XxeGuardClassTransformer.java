package i2f.extension.agent.javassist.transformer;

import i2f.match.StringMatcher;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2024/7/31 18:08
 * @desc
 */
public class XxeGuardClassTransformer implements ClassFileTransformer {
    public static final String SPRING_CONTEXT_CLASS_NAME = "org.springframework.context.ApplicationContext";
    public static volatile Object context;

    public static AtomicBoolean hasInterval = new AtomicBoolean(false);

    public static void main(String[] args) {
        String className = "org.springframework.boot.web.servlet.context.WebApplicationContextServletContextAwareProcessor";
        boolean ret = StringMatcher.antClass().match(className, "org.springframework.**");
        System.out.println(ret);
    }
    public static boolean isSpringApplicationContext(CtClass clazz){
        return isAssignableFrom(clazz,SPRING_CONTEXT_CLASS_NAME);
    }
    public static boolean isAssignableFrom(CtClass clazz,String fullClassName){
        if(clazz==null){
            return false;
        }
        String name = clazz.getName();
        if(name==null){
            return false;
        }
        if(Object.class.getName().equals(name)){
            return false;
        }
        if(fullClassName.equals(name)){
            return true;
        }
        try {
            CtClass superclass = clazz.getSuperclass();
            if(isAssignableFrom(superclass,fullClassName)){
                return true;
            }
        } catch (Exception e) {

        }
        try {
            CtClass[] interfaces = clazz.getInterfaces();
            if(interfaces!=null){
                for (CtClass anInterface : interfaces) {
                    if(isAssignableFrom(anInterface,fullClassName)){
                        return true;
                    }
                }
            }
        } catch (Exception e) {

        }
        return false;
    }

    public static Map<CtMethod,CtClass> getAllMethods(CtClass clazz){
        Map<CtMethod,CtClass> ret=new LinkedHashMap<>();
        if(clazz==null){
            return ret;
        }
        CtMethod[] methods = clazz.getMethods();
        if(methods!=null){
            for (CtMethod method : methods) {
                ret.put(method,clazz);
            }
        }
        CtMethod[] declaredMethods = clazz.getDeclaredMethods();
        if(declaredMethods!=null){
            for (CtMethod method : declaredMethods) {
                ret.put(method,clazz);
            }
        }
        try {
            CtClass superclass = clazz.getSuperclass();
            Map<CtMethod, CtClass> next = getAllMethods(superclass);
            ret.putAll(next);
        } catch (Exception e) {

        }
        try {
            CtClass[] interfaces = clazz.getInterfaces();
            if(interfaces!=null){
                for (CtClass anInterface : interfaces) {
                    Map<CtMethod, CtClass> next = getAllMethods(anInterface);
                    ret.putAll(next);
                }
            }
        } catch (Exception e) {

        }
        return ret;
    }

    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        className = className.replaceAll("/", ".");

        if (className.startsWith("java.lang.")
                || className.startsWith("java.util.")
                || className.startsWith("java.time.")
                || className.startsWith("java.reflect.")
                || className.startsWith("java.math.")
                || className.startsWith("java.text.")) {
//            System.out.println("jump class:"+className);
            return classfileBuffer;
        }

        if(!className.contains("WebApplicationContextServletContextAwareProcessor")){
            return classfileBuffer;
        }

        if (className.startsWith("org.springframework.")) {
            ClassPool cp = ClassPool.getDefault();

            CtClass cc = null;
            try {
                cc = cp.get(className);

//                if(!isSpringApplicationContext(cc)){
//                    return classfileBuffer;
//                }

                System.out.println("spring-context-class:"+className);

                Map<CtMethod, CtClass> allMethods = getAllMethods(cc);

                Set<CtMethod> methods = allMethods.keySet();

                for (CtMethod method : methods) {
                    if (method.isEmpty()) {
//                    System.out.println("jump method empty:" + item.getName());
                        continue;
                    }
                    if (Arrays.asList("equals", "hashCode", "clone",
                            "wait", "notify", "finalize", "notifyAll",
                            "getClass", "toString").contains(method.getName())) {
//                    System.out.println("jump method in:" + item.getName());
                        continue;
                    }
//                    System.out.println("spring-context-class-method:"+method);

//                    if(!Modifier.isPublic(method.getModifiers())) {
//                        method.setModifiers(method.getModifiers()|Modifier.PUBLIC);
//
//                    }

                    boolean isTar="org.springframework.boot.web.servlet.context.WebApplicationContextServletContextAwareProcessor".equals(className);
                    String tarCode="Object $zObj=this;\n" +
                            "  System.out.println(\"tar-obj:\"+$zObj);\n"+
                            "                    Class $zObjClass=$zObj.getClass();\n" +
                            "  System.out.println(\"tar-clazz:\"+$zObjClass);\n"+
                            "                    java.lang.reflect.Field $zContextField = $zObjClass.getDeclaredField(\"webApplicationContext\");\n" +
                            "  System.out.println(\"tar-field:\"+$zContextField);\n"+
                            "                    $zContextField.setAccessible(true);\n" +
                            "                    "+XxeGuardClassTransformer.class.getName()+".context=$zContextField.get($zObj);";
                    try {
                        String trigger = className + "." + method.getName();
                        method.insertBefore("{" +
//                                "    Class<?> $clazz=this.getClass();\n" +
//                                "    Class<?> $contextClass=org.springframework.context.ApplicationContext.class;\n" +
                                        (isTar?tarCode:"")+
                                "        System.out.println(\"spring-context method-test. --" + trigger + "\");" +
//                                "    if($contextClass.isAssignableFrom($clazz)){\n" +
//                                "        i2f.extension.agent.javassist.transformer.XxeGuardClassTransformer.context=this;\n" +
//                                "        System.out.println(\"spring-context constructor-inject. --" + trigger + "\");" +
//                                "    }" +
                                "}\n");
                        System.out.println("spring-context inject method:" + method);
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                }

                if (!hasInterval.getAndSet(true)) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {

                                }
                                System.out.println("interval check spring-context");
                                if (XxeGuardClassTransformer.context != null) {
                                    try {
                                        System.out.println("springContext:" + XxeGuardClassTransformer.context);
                                        Class<?> clazz = XxeGuardClassTransformer.context.getClass();
                                        Method method = clazz.getMethod("getBeanFactory");
                                        Object beanFactory = method.invoke(XxeGuardClassTransformer.context);
                                        System.out.println("beanFactory:" + beanFactory);
                                        if (beanFactory != null) {
                                            Class<?> beanFactoryClass = beanFactory.getClass();
                                            Method method1 = beanFactoryClass.getMethod("getBeanDefinitionNames");
                                            Object beanNames = method1.invoke(beanFactory);
                                            System.out.println("beanNames:" + beanNames);
                                            if (beanNames != null) {
                                                int len = Array.getLength(beanNames);
                                                for (int i = 0; i < len; i++) {
                                                    System.out.println("bean:" + Array.get(beanNames, i));
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }, "test-context");
                    System.out.println("start-test-context....");
                    thread.start();
                }
                return cc.toBytecode();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            } finally {
                if (cc != null) {
                    cc.detach();
                }
            }
        }

        if (!className.startsWith("javax.xml.")) {
            return classfileBuffer;
        }

        if ("javax.xml.parsers.DocumentBuilderFactory".equals(className)) {
            ClassPool cp = ClassPool.getDefault();
            System.out.println("xxe:" + className);
            CtClass cc = null;
            try {
                cc = cp.get(className);
                CtMethod method = cc.getDeclaredMethod("newInstance");

                method.instrument(new ExprEditor() {
                    @Override
                    public void edit(MethodCall m) throws CannotCompileException {
                        if (m.getMethodName().equals("newInstance")) {
                            m.replace("$_ = $proceed($$); " +
                                    "if ($_ != null) { " +
                                    "    $_.setExpandEntityReferences(false); \n" +
                                    "    System.out.println(\"disabled xxe features : DocumentBuilderFactory.newInstance()\"); \n" +
                                    "}" +
                                    "return $_;");
                        }
                    }
                });

                return cc.toBytecode();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            } finally {
                if (cc != null) {
                    cc.detach();
                }
            }

//            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
//            builderFactory.setExpandEntityReferences(false);
//            System.out.println("disabled xxe features : DocumentBuilderFactory.newInstance()");
        }
        if ("javax.xml.parsers.SAXParserFactory".equals(className)) {
            ClassPool cp = ClassPool.getDefault();
            System.out.println("xxe:" + className);
            CtClass cc = null;
            try {
                cc = cp.get(className);
                CtMethod method = cc.getDeclaredMethod("newInstance");

                method.instrument(new ExprEditor() {
                    @Override
                    public void edit(MethodCall m) throws CannotCompileException {
                        if (m.getMethodName().equals("newInstance")) {
                            m.replace("$_ = $proceed($$); " +
                                    "if ($_ != null) { " +
                                    "     try {\n" +
                                    "         $_.setFeature(\"http://apache.org/xml/features/disallow-doctype-decl\", true);\n" +
                                    "         $_.setFeature(\"http://xml.org/sax/features/external-general-entities\", false);\n" +
                                    "         $_.setFeature(\"http://xml.org/sax/features/external-parameter-entities\", false);\n" +
                                    "         System.out.println(\"disabled xxe features : SAXParserFactory.newInstance()\");\n" +
                                    "     } catch (Exception e) {\n" +
                                    "         e.printStackTrace();\n" +
                                    "     }" +
                                    "}" +
                                    "return $_;");
                        }
                    }
                });

                return cc.toBytecode();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            } finally {
                if (cc != null) {
                    cc.detach();
                }
            }

//            SAXParserFactory factory = SAXParserFactory.newInstance();
//            try {
//                factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
//                factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
//                factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
//                System.out.println("disabled xxe features : SAXParserFactory.newInstance()");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
        if ("javax.xml.stream.XMLInputFactory".equals(className)) {
            ClassPool cp = ClassPool.getDefault();
            System.out.println("xxe:" + className);
            CtClass cc = null;
            try {
                cc = cp.get(className);
                CtMethod method = cc.getDeclaredMethod("newInstance");

                method.instrument(new ExprEditor() {
                    @Override
                    public void edit(MethodCall m) throws CannotCompileException {
                        if (m.getMethodName().equals("newInstance")) {
                            m.replace("$_ = $proceed($$); " +
                                    "if ($_ != null) { " +
                                    "    $_.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false); \n" +
                                    "    $_.setProperty(XMLInputFactory.SUPPORT_DTD, false); \n" +
                                    "    System.out.println(\"disabled xxe features : XMLInputFactory.newInstance()\"); \n" +
                                    "}" +
                                    "return $_;");
                        }
                    }
                });

                return cc.toBytecode();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            } finally {
                if (cc != null) {
                    cc.detach();
                }
            }

//            XMLInputFactory factory = XMLInputFactory.newInstance();
//            factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
//            factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
//            System.out.println("disabled xxe features : XMLInputFactory.newInstance()");
        }


        return classfileBuffer;
    }
}
