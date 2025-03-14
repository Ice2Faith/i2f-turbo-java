package i2f.extension.agent.javassist.transformer.web;

import i2f.agent.AgentUtil;
import i2f.agent.transformer.InstrumentTransformerFeature;
import i2f.extension.agent.javassist.context.AgentContextHolder;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2024/10/10 23:20
 * @desc
 */
public class WebFilterThreadTraceIdClassesTransformer implements ClassFileTransformer, InstrumentTransformerFeature {
    public static final String[] FILTER_CLASSES = {
            "org.springframework.web.filter.RequestContextFilter",
            "org.springframework.web.filter.FormContentFilter",
            "org.springframework.web.filter.CharacterEncodingFilter",
            "org.springframework.web.filter.OncePerRequestFilter",
    };

    @Override
    public boolean canRetransform() {
        return true;
    }

    @Override
    public void onAdded(Instrumentation inst) {
        AgentUtil.retransformLoadedClasses(inst, (clazz) -> {
            String name = clazz.getName();
            for (String item : FILTER_CLASSES) {
                if (item.equals(name)) {
                    return true;
                }
            }
            return name.matches("^(javax\\.servlet\\.|jakarta\\.servlet\\.|org\\.springframework\\.web\\.)([a-zA-Z0-9_]+\\.)*([a-zA-Z0-9_$]*)(Filter)$");
        });
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        className = className.replaceAll("/", ".");
        boolean isTarget = false;
        for (String item : FILTER_CLASSES) {
            if (item.equals(className)) {
                isTarget = true;
            }
        }
        if (!isTarget) {
            isTarget = className.matches("^(javax\\.servlet\\.|jakarta\\.servlet\\.|org\\.springframework\\.web\\.)([a-zA-Z0-9_]+\\.)*([a-zA-Z0-9_$]*)(Filter)$");
        }
        if (!isTarget) {
            return null;
        }
        System.out.println("match-filter:" + className);

        ClassPool cp = ClassPool.getDefault();

        CtClass cc = null;
        try {
            cc = cp.get(className);

            Set<CtMethod> methods = new LinkedHashSet<>();
            methods.addAll(Arrays.asList(cc.getDeclaredMethods()));
            methods.addAll(Arrays.asList(cc.getMethods()));
            for (CtMethod method : methods) {
                if (!Arrays.asList(
                        "doFilter",
                        "doFilterInternal"
                ).contains(method.getName())) {
                    continue;
                }
                method.insertBefore("{\n" +
//                        "    System.out.println(\"filter-set-trace-id:\"+this);\n" +
                        "    " + AgentContextHolder.class.getName() + ".setTraceId($1);\n" +
//                        "    System.out.println(\"filter-set-trace-id:\" + this);\n" +
                        "}\n");
                method.insertAfter("{\n" +
//                        "    System.out.println(\"filter-remove-trace-id:\"+this);\n" +
                        "    " + AgentContextHolder.class.getName() + ".removeTraceId();\n" +
//                        "    System.out.println(\"filter-remove-trace-id:\" + this);\n" +
                        "}\n", true);
                System.out.println("filter-trace-transformer:" + method);
            }


            return cc.toBytecode();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cc != null) {
                cc.detach();
            }
        }

        return null;
    }
}
