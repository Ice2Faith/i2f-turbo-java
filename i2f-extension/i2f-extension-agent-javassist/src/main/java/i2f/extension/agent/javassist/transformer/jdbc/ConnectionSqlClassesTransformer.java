package i2f.extension.agent.javassist.transformer.jdbc;

import i2f.agent.AgentUtil;
import i2f.agent.transformer.InstrumentTransformerFeature;
import i2f.extension.agent.javassist.context.AgentContextHolder;
import i2f.extension.javassist.JavassistUtil;
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
public class ConnectionSqlClassesTransformer implements ClassFileTransformer, InstrumentTransformerFeature {

    @Override
    public boolean canRetransform() {
        return true;
    }

    @Override
    public void onAdded(Instrumentation inst) {
        AgentUtil.retransformLoadedClasses(inst, (clazz) -> {
            String className = clazz.getName();
            if (className.startsWith("java.")) {
                if (!className.startsWith("java.sql.")) {
                    return false;
                }
            }

            if (className.startsWith("javax.")) {
                if (!className.startsWith("javax.sql.")) {
                    return false;
                }
            }

            if (className.startsWith("jakarta.")) {
                if (!className.startsWith("jakarta.sql.")) {
                    return false;
                }
            }
            if (!className.toLowerCase().matches(".*(connect|sql|jdbc|pool|proxy).*")) {
                return false;
            }
            boolean ok = AgentUtil.isAssignableFrom(clazz, "java.sql.Connection");
//            if(ok) {
//                System.out.println("connection-filter:" + clazz);
//            }
            return ok;
        });
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        className = className.replaceAll("/", ".");

        if (className.startsWith("java.")) {
            if (!className.startsWith("java.sql.")) {
                return null;
            }
        }

        if (className.startsWith("javax.")) {
            if (!className.startsWith("javax.sql.")) {
                return null;
            }
        }

        if (className.startsWith("jakarta.")) {
            if (!className.startsWith("jakarta.sql.")) {
                return null;
            }
        }

        if (!className.toLowerCase().matches(".*(connect|sql|jdbc|pool|proxy).*")) {
            return null;
        }

        ClassPool cp = ClassPool.getDefault();

        CtClass cc = null;
        try {
            cc = cp.get(className);
            if (!JavassistUtil.isAssignableFrom(cc, "java.sql.Connection")) {
                return null;
            }
//            System.out.println("match-connection:" + className);

            Set<CtMethod> methods = new LinkedHashSet<>();
            methods.addAll(Arrays.asList(cc.getDeclaredMethods()));
            methods.addAll(Arrays.asList(cc.getMethods()));
            for (CtMethod method : methods) {
                if (!Arrays.asList(
                        "prepareCall",
                        "prepareStatement"
                ).contains(method.getName())) {
                    continue;
                }
                if (method.isEmpty()) {
                    continue;
                }
                CtClass[] parameterTypes = method.getParameterTypes();
                boolean isFirstSql = false;
                if (parameterTypes.length > 0) {
                    CtClass firstParameterType = parameterTypes[0];
                    if ("java.lang.String".equals(firstParameterType.getName())) {
                        isFirstSql = true;
                    }
                }
                if (isFirstSql) {
                    method.insertBefore("{\n" +
//                            "    System.out.println(\"connection-sql-notify:\"+this);\n" +
                            "    String $zSql=$1;\n" +
                            "    java.sql.Statement $zStat=null;\n" +
                            "    " + AgentContextHolder.class.getName() + ".notifyStatementExecute($zSql,$zStat);\n" +
//                            "    System.out.println(\"connection-sql-notify:\" + this);\n" +
                            "}\n");
                }

                System.out.println("connection-sql-transformer:" + method);
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
