package i2f.extension.agent.javassist.transformer;

import i2f.extension.agent.javassist.context.AgentContextHolder;
import i2f.extension.javassist.JavassistUtil;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;

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
public class SpringApplicationHoldClassesTransformer implements ClassFileTransformer {
    public static final String SPRING_APPLICATION_CLASS_NAME = "org.springframework.boot.SpringApplication";
    public static final String[][] INJECT_APPLICATION_CLASS_FIELD_ARRAY =
            {
                    {SPRING_APPLICATION_CLASS_NAME,null},
                    {"org.springframework.boot.context.event.EventPublishingRunListener","application"}
            };

    public static boolean isSpringApplication(CtClass clazz) {
        return JavassistUtil.isAssignableFrom(clazz, SPRING_APPLICATION_CLASS_NAME);
    }

    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {

        className = className.replaceAll("/", ".");

        String injectClassName = null;
        String injectFieldName = null;
        for (String[] pair : INJECT_APPLICATION_CLASS_FIELD_ARRAY) {
            if (pair[0].equals(className)) {
                injectClassName = pair[0];
                injectFieldName = pair[1];
                break;
            }
        }

        if (injectClassName == null) {
            return null;
        }


        ClassPool cp = ClassPool.getDefault();

        CtClass cc = null;
        try {
            cc = cp.get(className);

            Map<CtMethod, CtClass> allMethods = JavassistUtil.getAllMethods(cc);

            Set<CtMethod> methods = allMethods.keySet();

            for (CtMethod method : methods) {
                if (method.isEmpty()) {
                    continue;
                }
                if(Modifier.isStatic(method.getModifiers())){
                    continue;
                }
                if (Arrays.asList("equals", "hashCode", "clone",
                        "wait", "notify", "finalize", "notifyAll",
                        "getClass", "toString", "registerNatives").contains(method.getName())) {
                    continue;
                }

                String trigger = className + "." + method.getName();
                String injectLocation = "trigger[" + trigger + "] field [" + injectFieldName + "]";
                String tarCode = "if(" + AgentContextHolder.class.getName() + ".springApplication==null) { \n" +
                        "    Object $zObj=this;\n" ;
                        if(injectFieldName==null){
                            tarCode+= "    "+AgentContextHolder.class.getName()+".springApplication=$zObj;\n" ;
                        }else{
                            tarCode+= "    Class $zObjClass=$zObj.getClass();\n" +
                                    "    java.lang.reflect.Field $zContextField = $zObjClass.getDeclaredField(\"" + injectFieldName + "\");\n" +
                                    "    $zContextField.setAccessible(true);\n" +
                                    "    " + AgentContextHolder.class.getName() + ".springApplication=$zContextField.get($zObj);\n" ;
                        }
                        tarCode+="    if(" + AgentContextHolder.class.getName() + ".springApplication!=null){\n" +
                        "        System.out.println(\"spring-application inject : \"+" + AgentContextHolder.class.getName() + ".springApplication + \" by " + injectLocation + "\");\n" +
                        "    }\n" +
                        "}\n";
                try {
                    method.insertBefore("{" + tarCode + "}\n");
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
