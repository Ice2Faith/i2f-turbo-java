package i2f.extension.agent.javassist.transformer;

import i2f.agent.AgentUtil;
import i2f.agent.transformer.InstrumentTransformerFeature;
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
    public static void getProxyMethod() {
//        System.out.println("new-throwable:"+this);
//        boolean isExcept=true;
//        String name = this.getClass().getName();
//        if(name.startsWith("java.lang.")){
//            isExcept=false;
//        }
//        if(isExcept) {
//            String className = "i2f.extension.agent.javassist.context.AgentContextHolder";
//            Class clazz = null;
//            if (clazz == null) {
//                try {
//                    clazz = Class.forName(className);
//                } catch (Throwable ex) {
//
//                }
//            }
//            if (clazz == null) {
//                try {
//                    clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
//                } catch (Throwable ex) {
//
//                }
//            }
//            if (clazz != null) {
//                try {
//                    java.lang.reflect.Method method = clazz.getDeclaredMethod("notifyThrowable", new Class[]{Throwable.class});
//                    Object ivkObj = null;
//                    Object[] ivkArgs = new Object[]{this};
//                    method.invoke(ivkObj, ivkArgs);
//                    System.out.println("trigger-throwable:" + this);
//                } catch (Exception e) {
//
//                }
//            }
//        }

    }

    @Override
    public boolean canRetransform() {
        return false;
    }

    @Override
    public void onAdded(Instrumentation inst) {
        new Thread(() -> {
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {

            }
            AgentUtil.retransformLoadedClasses(inst, (clazz) -> {
                return Throwable.class.equals(clazz) || Throwable.class.isAssignableFrom(clazz);
            });
        }).start();
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        className = className.replaceAll("/", ".");
        if (!"java.lang.Throwable".equals(className)) {
            return null;
        }


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
                        "System.out.println(\"new-throwable:\"+this);\n" +
                        "        boolean isExcept=true;\n" +
                        "        String name = this.getClass().getName();\n" +
                        "        if(name.startsWith(\"java.lang.\")){\n" +
                        "            isExcept=false;\n" +
                        "        }\n" +
                        "        if(isExcept) {\n" +
                        "            String className = \"i2f.extension.agent.javassist.context.AgentContextHolder\";\n" +
                        "            Class clazz = null;\n" +
                        "            if (clazz == null) {\n" +
                        "                try {\n" +
                        "                    clazz = Class.forName(className);\n" +
                        "                } catch (Throwable ex) {\n" +
                        "\n" +
                        "                }\n" +
                        "            }\n" +
                        "            if (clazz == null) {\n" +
                        "                try {\n" +
                        "                    clazz = Thread.currentThread().getContextClassLoader().loadClass(className);\n" +
                        "                } catch (Throwable ex) {\n" +
                        "\n" +
                        "                }\n" +
                        "            }\n" +
                        "            if (clazz != null) {\n" +
                        "                try {\n" +
                        "                    java.lang.reflect.Method method = clazz.getDeclaredMethod(\"notifyThrowable\", new Class[]{Throwable.class});\n" +
                        "                    Object ivkObj = null;\n" +
                        "                    Object[] ivkArgs = new Object[]{this};\n" +
                        "                    method.invoke(ivkObj, ivkArgs);\n" +
                        "                    System.out.println(\"trigger-throwable:\" + this);\n" +
                        "                } catch (Exception e) {\n" +
                        "\n" +
                        "                }\n" +
                        "            }\n" +
                        "        }\n" +
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
