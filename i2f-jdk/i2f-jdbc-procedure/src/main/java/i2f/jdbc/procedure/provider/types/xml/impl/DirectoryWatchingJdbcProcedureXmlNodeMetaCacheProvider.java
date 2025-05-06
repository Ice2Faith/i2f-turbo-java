package i2f.jdbc.procedure.provider.types.xml.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.XProc4jConsts;
import i2f.jdbc.procedure.event.XProc4jEventHandler;
import i2f.jdbc.procedure.event.impl.DefaultXProc4jEventHandler;
import i2f.jdbc.procedure.parser.JdbcProcedureParser;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.provider.event.ProcedureMetaProviderNotifyEvent;
import i2f.resources.ResourceUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2025/4/16 13:57
 */
@Data
@NoArgsConstructor
public class DirectoryWatchingJdbcProcedureXmlNodeMetaCacheProvider extends AbstractJdbcProcedureXmlNodeMetaCacheProvider implements Runnable {
    protected final CopyOnWriteArraySet<File> dirs = new CopyOnWriteArraySet<>();
    protected final ConcurrentHashMap<String, XmlNode> nodeMap = new ConcurrentHashMap<>();
    protected volatile WatchService watcher;
    protected volatile XProc4jEventHandler eventHandler = new DefaultXProc4jEventHandler();

    public DirectoryWatchingJdbcProcedureXmlNodeMetaCacheProvider(List<String> locations) {
        if (locations == null) {
            return;
        }
        Set<File> paths = ResourceUtil.getResourcesFiles(locations);
        this.dirs.addAll(paths);
    }

    public DirectoryWatchingJdbcProcedureXmlNodeMetaCacheProvider(CopyOnWriteArraySet<File> dirs) {
        if (dirs == null) {
            return;
        }
        this.dirs.addAll(dirs);
    }

    public static void main(String[] args) {
        Set<File> paths = ResourceUtil.getResourcesFiles(Arrays.asList("classpath*:procedure/", "resources", "classpath:com", "file:/E:/procedure"));
        CopyOnWriteArraySet<File> dirs = new CopyOnWriteArraySet<>(paths);

//        File file=new File("./src/main/resources/procedure");
//        CopyOnWriteArraySet<File> dirs=new CopyOnWriteArraySet<>();
//        dirs.add(file);

        DirectoryWatchingJdbcProcedureXmlNodeMetaCacheProvider provider = new DirectoryWatchingJdbcProcedureXmlNodeMetaCacheProvider(dirs);
        provider.watching();
        provider.loopHandleEvents();
        System.out.println("ok");
    }

    public static void walkFileTree(File file, Predicate<File> consumer) {
        if (file == null) {
            return;
        }
        if (!consumer.test(file)) {
            return;
        }
        try {
            File[] files = file.listFiles();
            if (files == null) {
                return;
            }
            for (File item : files) {
                try {
                    walkFileTree(item, consumer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void notifyChange() {
        ProcedureMetaProviderNotifyEvent event = new ProcedureMetaProviderNotifyEvent();
        event.setProvider(this);
        if (eventHandler != null) {
            eventHandler.publish(event);
        }
    }

    public synchronized void watching() {
        try {
            if (watcher == null) {
                watcher = FileSystems.getDefault().newWatchService();
            }
            for (File dir : dirs) {
                if (dir == null) {
                    continue;
                }
                if (!dir.exists()) {
                    continue;
                }
                registerAll(Paths.get(dir.getAbsolutePath()));
                walkFileTree(dir, (file) -> {
                    handleXmlNodeFile(file);
                    return true;
                });
            }
            if (!nodeMap.isEmpty()) {
                notifyChange();
            }
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    // 注册目录及其子目录
    protected void registerAll(Path start) throws IOException {
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    protected void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY);
        System.out.println(XProc4jConsts.NAME + " watching directory: " + dir);
    }

    @Override
    public void run() {
        this.watching();
        this.loopHandleEvents();
    }

    // 处理所有事件
    void loopHandleEvents() {
        this.watching();
        while (true) {
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                if (kind == StandardWatchEventKinds.OVERFLOW) {
                    continue;
                }

                Object context = event.context();
                if (!(context instanceof Path)) {
                    continue;
                }
                Path path = (Path) event.context();
                Watchable watchable = key.watchable();
                if (!(watchable instanceof Path)) {
                    continue;
                }
                Path parent = (Path) watchable;
                Path fullPath = parent.resolve(path);
                File file = fullPath.toFile();

                // 处理文件变化
                if (StandardWatchEventKinds.ENTRY_DELETE != kind) {
                    handleXmlNodeFile(file);
                }
            }

            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }
    }

    public void handleXmlNodeFile(File file) {
        if (file == null) {
            return;
        }
        if (!file.isFile()) {
            return;
        }
        String name = file.getName();
        String suffix = "";
        int idx = name.lastIndexOf(".");
        if (idx >= 0) {
            suffix = name.substring(idx + 1);
        }
        if (!"xml".equalsIgnoreCase(suffix)) {
            return;
        }
        Map<String, XmlNode> ret = new HashMap<>();
        try {
            XmlNode node = JdbcProcedureParser.parse(file);
            System.out.println(XProc4jConsts.NAME + " watching xml-node file change parsed:" + file.getAbsolutePath());
            String id = node.getTagAttrMap().get(AttrConsts.ID);
            if (id != null) {
                ret.put(id, node);
                System.out.println(XProc4jConsts.NAME + " watching xml-node node:" + id);
                Map<String, XmlNode> next = new HashMap<>();
                JdbcProcedureParser.resolveEmbedIdNode(node, next);
                for (Map.Entry<String, XmlNode> entry : next.entrySet()) {
                    ret.put(entry.getKey(), entry.getValue());
                    if (!entry.getKey().equals(id)) {
                        String childId = id + "." + entry.getKey();
                        System.out.println(XProc4jConsts.NAME + " watching xml-node node-child:" + childId);
                        ret.put(childId, entry.getValue());
                    }
                }
            }
            if (!ret.isEmpty()) {
                nodeMap.putAll(ret);
                notifyChange();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, XmlNode> parseResources() {
        return new LinkedHashMap<>(nodeMap);
    }
}
