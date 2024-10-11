package i2f.extension.agent.javassist.transformer.shutdown;

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

/**
 * @author Ice2Faith
 * @date 2024/8/2 14:20
 * @desc
 */
public class ShutdownLogClassTransformer implements ClassFileTransformer, InstrumentTransformerFeature {

    @Override
    public boolean canRetransform() {
        return true;
    }

    @Override
    public void onAdded(Instrumentation inst) {
        AgentUtil.retransformLoadedClasses(inst, (clazz) -> {
            String name = clazz.getName();
            if (name.equals("java.lang.Shutdown")) {
                return true;
            }
            return false;
        });
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        className = className.replaceAll("/", ".");
        if (!"java.lang.Shutdown".equals(className)) {
            return null;
        }

        ClassPool cp = ClassPool.getDefault();

        CtClass cc = null;
        try {
            cc = cp.get(className);

            CtMethod method = cc.getDeclaredMethod("exit", new CtClass[]{CtClass.intType});

            method.insertBefore("{\n" +
//                  "    System.out.println(\"shutdown-notify:\"+this);\n" +
                    "    int $zExitCode=$1;\n" +
                    "    StackTraceElement[] $zTraces = Thread.currentThread().getStackTrace();\n" +
                    "    " + AgentContextHolder.class.getName() + ".notifyShutdown($zExitCode,$zTraces);\n" +
//                  "    System.out.println(\"shutdown-notify:\" + this);\n" +
                    "}\n");
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
