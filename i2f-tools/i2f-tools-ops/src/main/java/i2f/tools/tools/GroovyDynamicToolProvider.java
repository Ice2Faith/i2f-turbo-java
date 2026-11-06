package i2f.tools.tools;

import groovy.lang.GroovyShell;
import i2f.ai.std.tool.ToolRawDefinition;
import i2f.ai.std.tool.ToolRawDefinitionsProvider;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.ai.std.tool.schema.JsonSchemaAnnotationResolver;
import i2f.extension.groovy.GroovyScript;
import i2f.io.stream.StreamUtil;
import i2f.reflect.ReflectResolver;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2026/11/6 15:32
 * @desc
 */
@ConditionalOnExpression("${ai.tools.groovy-dynamic-tools.enable:false}")
@ConditionalOnClass({
        GroovyScript.class,
        GroovyShell.class
})
@Component
@Data
@NoArgsConstructor
@Slf4j
public class GroovyDynamicToolProvider implements ToolRawDefinitionsProvider, ApplicationContextAware, ApplicationRunner {
    // filePath,bean,fileLastModify
    protected final ConcurrentHashMap<String, Map.Entry<Object, Long>> fileBeanMap = new ConcurrentHashMap<>();
    protected ApplicationContext applicationContext;
    protected final Map<String, ToolRawDefinition> cache = new ConcurrentHashMap<>();
    protected final ReentrantLock lock = new ReentrantLock();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    refreshTools();
                } catch (Throwable e) {
                    log.warn("refresh tools error: " + e.getMessage(), e);
                }
                try {
                    Thread.sleep(15 * 1000);
                } catch (Exception e) {
                    // ignore
                }
            }
        });
        thread.setDaemon(true);
        thread.setName("groovy-tool-refresher");
        thread.start();
    }

    @Override
    public List<ToolRawDefinition> getTools() {
        lock.lock();
        try {
            return new ArrayList<>(cache.values());
        } finally {
            lock.unlock();
        }
    }

    public void refreshTools() {
        Map<String, ToolRawDefinition> tools = getGroovyTools();
        lock.lock();
        try {
            cache.clear();
            cache.putAll(tools);
            log.info("current groovy dynamic tools count: " + tools.size());
        } finally {
            lock.unlock();
        }
    }

    public Map<String, ToolRawDefinition> getGroovyTools() {
        List<Object> beans = scanGroovyTools();
        return ToolRawHelper.parseTools(JsonSchemaAnnotationResolver.INSTANCE, beans);
    }

    public List<Object> scanGroovyTools() {
        List<File> files = scanGroovyScripts();
        for (File file : files) {
            long lmt = file.lastModified();
            Map.Entry<Object, Long> entry = fileBeanMap.get(file.getAbsolutePath());
            if (entry != null) {
                if (entry.getValue() >= lmt) {
                    continue;
                }
            }

            try {
                String script = StreamUtil.readString(file, StandardCharsets.UTF_8.name());
                Class clazz = GroovyScript.parseAsClass(script);
                Object obj = ReflectResolver.getInstance(clazz);
                AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
                factory.autowireBean(obj);
                factory.initializeBean(obj, file.getName());

                fileBeanMap.put(file.getAbsolutePath(), new AbstractMap.SimpleEntry<>(obj, lmt));
            } catch (Throwable e) {
                log.warn("load groovy tool failure: " + file.getAbsolutePath() + ", error: " + e.getMessage(), e);
            }
        }

        return fileBeanMap.values().stream()
                .filter(e -> e.getKey() != null)
                .map(e -> e.getKey())
                .collect(Collectors.toList());
    }

    public static List<File> scanGroovyScripts() {
        List<File> ret = new ArrayList<>();
        scanGroovyScriptsNext(ret, new File("./tools"));
        return ret;
    }

    public static void scanGroovyScriptsNext(List<File> ret, File file) {
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File item : files) {
                    scanGroovyScriptsNext(ret, item);
                }
            }
        }
        if (file.isFile()) {
            String name = file.getName();
            if (name.endsWith(".groovy")) {
                ret.add(file);
            }
        }
    }

}
