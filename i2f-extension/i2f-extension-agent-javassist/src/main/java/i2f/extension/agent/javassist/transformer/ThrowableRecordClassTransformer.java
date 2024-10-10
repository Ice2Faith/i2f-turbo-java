package i2f.extension.agent.javassist.transformer;

import i2f.agent.AgentUtil;
import i2f.agent.transformer.InstrumentTransformerFeature;
import i2f.extension.agent.javassist.context.AgentContextHolder;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.Modifier;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * @author Ice2Faith
 * @date 2024/8/2 14:20
 * @desc
 */
public class ThrowableRecordClassTransformer implements ClassFileTransformer, InstrumentTransformerFeature {

    @Override
    public boolean canRetransform() {
        return true;
    }

    @Override
    public void onAdded(Instrumentation inst) {
        AgentUtil.retransformLoadedClasses(inst, (clazz) -> {
            return Throwable.class.equals(clazz) || Throwable.class.isAssignableFrom(clazz);
        });
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        className = className.replaceAll("/", ".");
        if (!className.matches("^([a-zA-Z0-9_]+)?(\\.[a-zA-Z0-9_]+)*([a-zA-Z0-9_$]*)(Throwable|Exception|Error)$")) {
            return null;
        }
        System.out.println("match-throwable:" + className);


        ClassPool cp = ClassPool.getDefault();

        CtClass cc = null;
        try {
            cc = cp.get(className);

            CtConstructor[] constructors = cc.getDeclaredConstructors();
            for (CtConstructor constructor : constructors) {
                if (!Modifier.isPublic(constructor.getModifiers())) {
                    continue;
                }
                constructor.insertAfter("{\n" +
//                        "    System.out.println(\"new-throwable:\"+this);\n" +
                        "    Throwable $zEx = this;\n" +
                        "    " + AgentContextHolder.class.getName() + ".notifyThrowable($zEx);\n" +
//                        "    System.out.println(\"trigger-throwable:\" + this);\n" +
                        "}\n");
                System.out.println("throwable-notify-transformer:" + constructor);
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
