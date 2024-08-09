package i2f.environment.impl;

import i2f.environment.IWritableEnvironment;
import i2f.jvm.JvmUtil;

import java.lang.management.CompilationMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2024/8/9 8:50
 * @desc
 */
public class SystemAdditionalEnvironment implements IWritableEnvironment {
    public static final SystemAdditionalEnvironment INSTANCE = new SystemAdditionalEnvironment();

    public static final String RUNTIME_BOOT_CLASS_PATH = "runtime.boot.class.path";
    public static final String RUNTIME_CLASS_PATH = "runtime.class.path";
    public static final String RUNTIME_INPUT_ARGUMENTS = "runtime.input.arguments";
    public static final String RUNTIME_LIBRARY_PATH = "runtime.library.path";
    public static final String RUNTIME_PID = "runtime.pid";
    public static final String RUNTIME_START_TIME = "runtime.start.time";
    public static final String RUNTIME_VM_NAME = "runtime.vm.name";
    public static final String RUNTIME_VM_VENDOR = "runtime.vm.vendor";
    public static final String RUNTIME_VM_VERSION = "runtime.vm.version";
    public static final String COMPILATION_TOTAL_COMPILATION_TIME = "compilation.total.compilation.time";
    public static final String COMPILATION_NAME = "compilation.name";
    public static final String OPERATING_SYSTEM_ARCH = "operating.system.arch";
    public static final String OPERATING_SYSTEM_AVAILABLE_PROCESSORS = "operating.system.available.processors";
    public static final String OPERATING_SYSTEM_NAME = "operating.system.name";
    public static final String OPERATING_SYSTEM_VERSION = "operating.system.version";
    public static final Map<String, String> unmodifiedMap;

    static {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        Map<String, String> map = new HashMap<>();
        map.put(RUNTIME_BOOT_CLASS_PATH, runtimeMXBean.getBootClassPath());
        map.put(RUNTIME_CLASS_PATH, runtimeMXBean.getClassPath());
        StringJoiner joiner = new StringJoiner("\n");
        List<String> arguments = runtimeMXBean.getInputArguments();
        for (String argument : arguments) {
            joiner.add(argument);
        }
        map.put(RUNTIME_INPUT_ARGUMENTS, joiner.toString());
        map.put(RUNTIME_LIBRARY_PATH, runtimeMXBean.getLibraryPath());
        map.put(RUNTIME_PID, String.valueOf(JvmUtil.getPid()));
        map.put(RUNTIME_START_TIME, String.valueOf(runtimeMXBean.getStartTime()));
        map.put(RUNTIME_VM_NAME, runtimeMXBean.getVmName());
        map.put(RUNTIME_VM_VENDOR, runtimeMXBean.getVmVendor());
        map.put(RUNTIME_VM_VERSION, runtimeMXBean.getVmVersion());

        CompilationMXBean compilationMXBean = ManagementFactory.getCompilationMXBean();
        map.put(COMPILATION_TOTAL_COMPILATION_TIME, String.valueOf(compilationMXBean.getTotalCompilationTime()));
        map.put(COMPILATION_NAME, compilationMXBean.getName());

        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        map.put(OPERATING_SYSTEM_ARCH, operatingSystemMXBean.getArch());
        map.put(OPERATING_SYSTEM_AVAILABLE_PROCESSORS, String.valueOf(operatingSystemMXBean.getAvailableProcessors()));
        map.put(OPERATING_SYSTEM_VERSION, operatingSystemMXBean.getVersion());
        map.put(OPERATING_SYSTEM_NAME, operatingSystemMXBean.getName());

        unmodifiedMap = map;
    }

    protected ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();

    @Override
    public void setProperty(String name, String value) {
        if (name == null) {
            return;
        }
        if (value != null) {
            map.put(name, value);
            System.setProperty(name, value);
        }
    }

    @Override
    public String getProperty(String name) {
        if (name == null) {
            return null;
        }
        String ret = unmodifiedMap.get(name);
        if (ret != null) {
            return ret;
        }
        ret = map.get(name);
        if (ret != null) {
            return ret;
        }
        ret = System.getProperty(name);
        if (ret != null) {
            return ret;
        }
        return null;
    }

    @Override
    public Map<String, String> getAllProperties() {
        Map<String, String> ret = new LinkedHashMap<>();
        Properties properties = System.getProperties();
        Set<String> names = properties.stringPropertyNames();
        for (String name : names) {
            String prop = properties.getProperty(name);
            ret.put(name, prop);
        }
        ret.putAll(map);
        ret.putAll(unmodifiedMap);
        return ret;
    }
}
