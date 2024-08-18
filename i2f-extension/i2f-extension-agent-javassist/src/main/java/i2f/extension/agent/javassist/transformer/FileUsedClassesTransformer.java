package i2f.extension.agent.javassist.transformer;

import i2f.extension.agent.javassist.context.AgentContextHolder;
import i2f.extension.javassist.JavassistUtil;
import javassist.*;

import java.io.File;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2024/8/2 8:31
 * @desc
 */
public class FileUsedClassesTransformer implements ClassFileTransformer {
    public static final String FILE_CLASS_NAME= File.class.getName();
    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {

        className = className.replaceAll("/", ".");
        if(!FILE_CLASS_NAME.equals(className)){
            return null;
        }

        ClassPool cp = ClassPool.getDefault();

        CtClass cc = null;
        try {
            cc = cp.get(className);

            CtConstructor[] constructors = cc.getConstructors();

            for (CtConstructor method : constructors) {
                if (method.isEmpty()) {
                    continue;
                }


                String trigger = className + "." + method.getName();
                String injectLocation = "trigger[" + trigger + "]";
                String tarCode = "System.out.println(this.getAbsolutePath());\n";
                try {
                    method.insertAfter("{" + tarCode + "}\n");
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
