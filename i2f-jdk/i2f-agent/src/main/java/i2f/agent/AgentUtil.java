package i2f.agent;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import i2f.agent.transformer.InstrumentTransformerFeature;
import i2f.jvm.JvmUtil;
import i2f.match.StringMatcher;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.function.Predicate;
import java.util.jar.JarFile;

/**
 * @author Ice2Faith
 * @date 2022/4/3 11:13
 * @desc
 */
public class AgentUtil {

    public static boolean isAssignableFrom(Class<?> clazz, String type) {
        if (clazz == null) {
            return false;
        }
        String name = clazz.getName();
        if (name == null) {
            return false;
        }
        if (name.equals(type)) {
            return true;
        }
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            boolean ok = isAssignableFrom(superclass, type);
            if (ok) {
                return true;
            }
        }

        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> anInterface : interfaces) {
            boolean ok = isAssignableFrom(anInterface, type);
            if (ok) {
                return true;
            }
        }

        return false;
    }

    public static void appendAgentJarToBootstrapClassLoaderSearch(Instrumentation inst) {
        if (inst == null) {
            return;
        }
        try {
            File agentJarFile = getAgentJarFile();
            if (agentJarFile == null) {
                return;
            }
            if (!agentJarFile.isFile()) {
                return;
            }
            appendJarFileToBootstrapClassLoaderSearch(inst, agentJarFile);
            File parentFile = agentJarFile.getParentFile();
            if (parentFile == null) {
                return;
            }
            File libDir = new File(parentFile, "lib");
            appendJarLibDirToBootstrapClassLoaderSearch(inst, libDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void appendJarFileToBootstrapClassLoaderSearch(Instrumentation inst, File jarFile) {
        if (inst == null) {
            return;
        }
        if (jarFile == null) {
            return;
        }
        if (!jarFile.isFile()) {
            return;
        }
        try {
            inst.appendToBootstrapClassLoaderSearch(new JarFile(jarFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void appendJarLibDirToBootstrapClassLoaderSearch(Instrumentation inst, File libDir) {
        if (inst == null) {
            return;
        }
        if (libDir == null) {
            return;
        }
        if (!libDir.isDirectory()) {
            return;
        }
        File[] files = libDir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            String name = file.getName();
            if (name.endsWith(".jar")) {
                if (file.isFile()) {
                    try {
                        inst.appendToBootstrapClassLoaderSearch(new JarFile(file));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


    }

    public static File getAgentJarFile() {
        List<String> inputArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
        for (String inputArgument : inputArguments) {
            if (inputArgument == null) {
                continue;
            }
            inputArgument = inputArgument.trim();
            if (inputArgument.startsWith("-javaagent:")) {
                String javaAgent = inputArgument.substring("-javaagent:".length());
                int idx = javaAgent.indexOf("=");
                if (idx >= 0) {
                    javaAgent = javaAgent.substring(0, idx);
                }
                return new File(javaAgent.trim());
            }
        }
        return null;
    }

    public static void addTransformers(Instrumentation inst, List<ClassFileTransformer> transformers) {
        if (transformers == null || transformers.isEmpty()) {
            return;
        }
        for (ClassFileTransformer transformer : transformers) {
            if (transformer instanceof InstrumentTransformerFeature) {
                InstrumentTransformerFeature feature = (InstrumentTransformerFeature) transformer;
                inst.addTransformer(transformer, feature.canRetransform());
            } else {
                inst.addTransformer(transformer);
            }
        }
        for (ClassFileTransformer transformer : transformers) {
            if (transformer instanceof InstrumentTransformerFeature) {
                InstrumentTransformerFeature feature = (InstrumentTransformerFeature) transformer;
                feature.onAdded(inst);
            }
        }
    }

    public static List<Class<?>> getLoadedClasses(Instrumentation inst) {
        Class<?>[] classes = inst.getAllLoadedClasses();
        List<Class<?>> ret = new ArrayList<>(Arrays.asList(classes));
        return ret;
    }

    public static String makeActionPattenArg(Set<String> actions, Set<String> classPattens) {
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for (String action : actions) {
            if (action == null || action.isEmpty()) {
                continue;
            }
            if (!isFirst) {
                builder.append(",");
            }
            builder.append(action);
            isFirst = false;
        }
        builder.append("@");
        isFirst = true;
        for (String patten : classPattens) {
            if (patten == null || patten.isEmpty()) {
                continue;
            }
            if (!isFirst) {
                builder.append(",");
            }
            builder.append(patten);
            isFirst = false;
        }
        return builder.toString();
    }

    /**
     * 参数解释:
     * 每一组参数使用&分隔
     * 因此上面就有两组
     * args,stat,ret@com.i2f.**,i2f.core.**
     * args@org.**
     * 每一组分为actions@full-method-ant-pattens
     * 含义为监视在一组ant-pattens定义的全限定函数名上的actions动作
     *
     * @param arg
     * @return
     */
    public static Map<String, Set<String>> parseClassPattenMap(String arg) {
        Map<String, Set<String>> actionPattens = new HashMap<>();
        if (arg == null || arg.isEmpty()) {
            return actionPattens;
        }
        String[] rules = arg.split("&");
        for (String rule : rules) {
            String[] pair = rule.split("@", 2);
            if (pair.length < 2) {
                continue;
            }
            String actions = pair[0];
            String pattens = pair[1];
            String[] actionArr = actions.split(",");
            String[] pattenArr = pattens.split(",");
            Set<String> actionSet = new HashSet<>();
            for (String action : actionArr) {
                action = action.trim().toLowerCase();
                if (action.isEmpty()) {
                    continue;
                }
                actionSet.add(action);
            }
            Set<String> pattenSet = new HashSet<>();
            for (String patten : pattenArr) {
                patten = patten.trim();
                if (patten.isEmpty()) {
                    continue;
                }
                pattenSet.add(patten);
            }
            for (String action : actionSet) {
                actionPattens.put(action, new HashSet<>(pattenSet));
            }
        }
        return actionPattens;
    }

    public static void retransformLoadedClasses(Instrumentation inst, Predicate<Class<?>> filter) {
        List<Class<?>> list = AgentUtil.getLoadedClasses(inst);
        for (Class<?> item : list) {
            if (filter == null || filter.test(item)) {
                try {
                    System.out.println("retransform:" + item.getName());
                    inst.retransformClasses(item);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void retransformLoadedClasses(Instrumentation inst, Map<String, Set<String>> actionPattens) {
        List<Class<?>> list = AgentUtil.getLoadedClasses(inst);
        for (Class<?> item : list) {
            boolean isTar = false;
            for (String patten : actionPattens.keySet()) {
                if (StringMatcher.antClass().match(item.getName(), patten)) {
                    isTar = true;
                    break;
                }
            }
            if (item.getName().contains("$")) {
                isTar = false;
            }
            if (isTar) {
                try {
                    System.out.println("retransform:" + item.getName());
                    inst.retransformClasses(item);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static VirtualMachine agentCurrent(File agentJarFile, String arg) throws Exception {
        return agentCurrent(agentJarFile.getAbsolutePath(), arg);
    }

    public static VirtualMachine agentCurrent(String agentJarPath, String arg) throws Exception {
        String pid = JvmUtil.getPid();
        return agentByPid(pid, agentJarPath, arg);
    }

    public static VirtualMachine agentByPid(String pid, File agentJarFile, String arg) throws Exception {
        return agentByPid(pid, agentJarFile.getCanonicalPath(), arg);
    }

    public static VirtualMachine agentByPid(String pid, String agentJarPath, String arg) throws Exception {
        VirtualMachine vm = VirtualMachine.attach(pid);
        vm.loadAgent(agentJarPath, arg);
        vm.detach();
        return vm;
    }

    public static List<VirtualMachine> agentByName(String name, File agentJarFile, String arg) throws Exception {
        return agentByName(name, agentJarFile.getAbsolutePath(), arg);
    }

    public static List<VirtualMachine> agentByName(String name, String agentJarPath, String arg) throws Exception {
        List<VirtualMachine> ret = new ArrayList<>();
        List<VirtualMachineDescriptor> vmds = VirtualMachine.list();
        for (VirtualMachineDescriptor item : vmds) {
            if (item.displayName().contains(name)) {
                VirtualMachine vm = agentByPid(item.id(), agentJarPath, arg);
                ret.add(vm);
            }
        }
        return ret;
    }
}
