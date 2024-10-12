package i2f.extension.agent.javassist.transformer.rmi;

import i2f.agent.AgentUtil;
import i2f.agent.transformer.InstrumentTransformerFeature;
import i2f.extension.agent.javassist.context.AgentContextHolder;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.rmi.Naming;
import java.security.ProtectionDomain;

/**
 * @author Ice2Faith
 * @date 2024/10/12 22:36
 */
public class RmiNamingLookupClassFileTransformer implements ClassFileTransformer, InstrumentTransformerFeature {
    public static final String NAMING_CLASS_NAME = Naming.class.getName();

    @Override
    public boolean canRetransform() {
        return true;
    }

    @Override
    public void onAdded(Instrumentation inst) {
        AgentUtil.retransformLoadedClasses(inst, (clazz) -> {
            return NAMING_CLASS_NAME.equals(clazz.getName());
        });
    }

    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {

        className = className.replaceAll("/", ".");
        if (!NAMING_CLASS_NAME.equals(className)) {
            return null;
        }

        ClassPool cp = ClassPool.getDefault();

        CtClass cc = null;
        try {
            cc = cp.get(className);

            CtMethod[] methods = cc.getDeclaredMethods();

            for (CtMethod method : methods) {
                if (method.isEmpty()) {
                    continue;
                }
                if (!"lookup".equals(method.getName())) {
                    continue;
                }
                CtClass[] parameterTypes = method.getParameterTypes();
                if (parameterTypes == null || parameterTypes.length != 1) {
                    continue;
                }


                try {
                    method.insertBefore("{\n" +
//                            "    System.out.println(\"rmi-naming-lookup-notify:\"+this);\n" +
                            "    " + AgentContextHolder.class.getName() + ".notifyRmiNamingLookup($1);\n" +
//                            "    System.out.println(\"rmi-naming-lookup-notify:\" + this);\n" +
                            "}\n");
                } catch (Exception e) {
                    System.out.println(method);
                    e.printStackTrace();
                }
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
