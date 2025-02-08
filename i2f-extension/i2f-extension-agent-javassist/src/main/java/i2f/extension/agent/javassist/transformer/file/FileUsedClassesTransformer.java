package i2f.extension.agent.javassist.transformer.file;

import i2f.agent.transformer.InstrumentTransformerFeature;
import i2f.extension.agent.javassist.context.AgentContextHolder;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;

import java.io.File;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;

/**
 * @author Ice2Faith
 * @date 2024/8/2 8:31
 * @desc
 */
public class FileUsedClassesTransformer implements ClassFileTransformer, InstrumentTransformerFeature {
    public static final String FILE_CLASS_NAME = File.class.getName();

    @Override
    public boolean canRetransform() {
        return true;
    }

    @Override
    public void onAdded(Instrumentation inst) {
        new Thread(() -> {
            try {
                Thread.sleep(8 * 1000);
            } catch (Exception e) {

            }
            try {
                inst.retransformClasses(File.class);
            } catch (UnmodifiableClassException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {

        className = className.replaceAll("/", ".");
        if (!FILE_CLASS_NAME.equals(className)) {
            return null;
        }

        ClassPool cp = ClassPool.getDefault();

        CtClass cc = null;
        try {
            cc = cp.get(className);

            CtConstructor[] constructors = cc.getDeclaredConstructors();

            for (CtConstructor method : constructors) {
                if (method.isEmpty()) {
                    continue;
                }


                try {
                    method.insertAfter("{\n" +
//                            "    System.out.println(\"file-new-notify:\"+this);\n" +
                            "    " + AgentContextHolder.class.getName() + ".notifyFile(this);\n" +
//                            "    System.out.println(\"file-new-notify:\" + this);\n" +
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
