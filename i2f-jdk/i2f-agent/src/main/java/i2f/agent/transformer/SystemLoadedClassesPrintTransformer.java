package i2f.agent.transformer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * @author Ice2Faith
 * @date 2022/4/3 12:16
 * @desc
 */
public class SystemLoadedClassesPrintTransformer implements ClassFileTransformer, InstrumentTransformerFeature {
    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
//        System.out.println("transform class:" + className);
        return classfileBuffer;
    }

    @Override
    public boolean canRetransform() {
        return true;
    }

    @Override
    public void onAdded(Instrumentation inst) {

    }
}
