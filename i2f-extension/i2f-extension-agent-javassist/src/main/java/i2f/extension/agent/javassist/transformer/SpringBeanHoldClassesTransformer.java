package i2f.extension.agent.javassist.transformer;

import i2f.extension.javassist.JavassistUtil;
import javassist.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2024/8/2 16:21
 * @desc
 */
public class SpringBeanHoldClassesTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        className = className.replaceAll("/", ".");
        if (!className.startsWith("org.springframework.")) {
            return null;
        }

        String lowerName = className.toLowerCase();
        if (!lowerName.contains("context")
                && !lowerName.contains(".context.")
                && !lowerName.contains(".beans.")
                && !lowerName.contains(".core.env.")
                && !lowerName.contains(".web.")) {
            return null;
        }

        String realClassName = className;
        int idx = className.lastIndexOf("$$EnhancerBySpringCGLIB$$");
        if (idx >= 0) {
            realClassName = className.substring(0, idx);
        }
        System.out.println(className);
        System.out.println(realClassName);

        ClassPool cp = ClassPool.getDefault();

        CtClass cc = null;
        if (!realClassName.equals(className)) {
            try {
                cc = cp.get(realClassName);
                Map<CtMethod, CtClass> allMethods = JavassistUtil.getAllMethods(cc);
                Set<CtMethod> methods = allMethods.keySet();
                for (CtMethod method : methods) {
                    if (method.isEmpty()) {
                        continue;
                    }
                    if (Arrays.asList("equals", "hashCode", "clone",
                            "wait", "notify", "finalize", "notifyAll",
                            "getClass", "toString", "registerNatives").contains(method.getName())) {
                        continue;
                    }
                    if (Modifier.isStatic(method.getModifiers())) {
                        continue;
                    }
                    method.insertBefore("{\n" +
                            "    Object $zThis=this;\n" +
                            "    System.out.println(\"spring-bean:real:\"+$zThis);\n" +
                            "}\n");
                }

                return cc.toBytecode();
            } catch (Exception e) {
                System.out.println("error, real class name:" + e.getMessage());
            } finally {
                if (cc != null) {
                    cc.detach();
                }
            }

        }


        cc = null;
        try {
            cc = cp.get(className);
            Map<CtMethod, CtClass> allMethods = JavassistUtil.getAllMethods(cc);
            Set<CtMethod> methods = allMethods.keySet();
            for (CtMethod method : methods) {
                if (method.isEmpty()) {
                    continue;
                }
                if (Arrays.asList("equals", "hashCode", "clone",
                        "wait", "notify", "finalize", "notifyAll",
                        "getClass", "toString", "registerNatives").contains(method.getName())) {
                    continue;
                }
                if (Modifier.isStatic(method.getModifiers())) {
                    continue;
                }
                method.insertBefore("{\n" +
                        "    Object $zThis=this;\n" +
                        "    System.out.println(\"spring-bean:\"+$zThis);\n" +
                        "}\n");
            }

            return cc.toBytecode();
        } catch (Exception e) {
            System.out.println("error, class name:" + e.getMessage());
        } finally {
            if (cc != null) {
                cc.detach();
            }
        }

        if (!realClassName.equals(className)) {
            cc = null;
            try {
                cc = cp.get(realClassName);
                CtConstructor[] constructors = cc.getConstructors();
                for (CtConstructor constructor : constructors) {
                    if (constructor.isEmpty()) {
                        continue;
                    }

                    constructor.insertBefore("{\n" +
                            "    Object $zThis=this;\n" +
                            "    System.out.println(\"spring-bean:constructor:\"+$zThis);\n" +
                            "}\n");
                }

                return cc.toBytecode();
            } catch (Exception e) {
                System.out.println("error, real class name:" + e.getMessage());
            } finally {
                if (cc != null) {
                    cc.detach();
                }
            }
        }

        cc = null;
        try {
            cc = cp.get(className);
            CtConstructor[] constructors = cc.getConstructors();
            for (CtConstructor constructor : constructors) {
                if (constructor.isEmpty()) {
                    continue;
                }
                constructor.insertBefore("{\n" +
                        "    Object $zThis=this;\n" +
                        "    System.out.println(\"spring-bean:constructor:\"+$zThis);\n" +
                        "}\n");
            }

            return cc.toBytecode();
        } catch (Exception e) {
            System.out.println("error, class name:" + e.getMessage());
        } finally {
            if (cc != null) {
                cc.detach();
            }
        }

        return null;
    }
}
