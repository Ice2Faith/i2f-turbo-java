package i2f.turbo.idea.plugin.jdbc.procedure;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileVisitor;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.xml.XmlUtil;
import i2f.xml.data.Xml;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class JdbcProcedureProjectMetaHolder {
    public static final Logger log = Logger.getInstance(JdbcProcedureProjectMetaHolder.class);

    // key: projectPath,value:{key:procedureId,value: ProcedureMeta}
    public static final ConcurrentMap<String, Map<String, VirtualFile>> XML_FILE_MAP = new ConcurrentHashMap<>();
    public static final ConcurrentMap<String, Map<String, String>> XML_FILE_META_KEY_MAP = new ConcurrentHashMap<>();
    public static final ConcurrentMap<String, Map<String, ProcedureMeta>> PROCEDURE_META_MAP = new ConcurrentHashMap<>();
    public static final List<String> FEATURES = getFeatures()
            .stream().filter(e -> !e.startsWith("cause"))
            .collect(Collectors.toUnmodifiableList());

    public static final List<String> FEATURES_CAUSE = getFeatures()
            .stream().filter(e -> e.startsWith("cause"))
            .collect(Collectors.toUnmodifiableList());

    public static List<String> getFeatures() {
        List<String> ret = new ArrayList<>();
        Class<FeatureConsts> clazz = FeatureConsts.class;
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            if (!String.class.isAssignableFrom(field.getType())) {
                continue;
            }
            if (!Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            try {
                Object value = field.get(null);
                if (value != null) {
                    ret.add(String.valueOf(value));
                }
            } catch (Exception e) {

            }
        }
        return ret;
    }

    public static final AtomicBoolean initRefreshThread = new AtomicBoolean(false);

    static {
        startRefreshThread();
    }

    public static Map<String, ProcedureMeta> getProjectMetaMap(Project project) {
        String projectPath = getProjectPath(project);
        Map<String, ProcedureMeta> metaMap = PROCEDURE_META_MAP.get(projectPath);
        if (metaMap == null) {
            return new HashMap<>();
        }
        return metaMap;
    }

    public static ProcedureMeta getProjectMeta(Project project, String procedureId) {
        Map<String, ProcedureMeta> metaMap = getProjectMetaMap(project);
        if (metaMap == null) {
            return null;
        }
        return metaMap.get(procedureId);
    }

    public static void startRefreshThread() {
        if (initRefreshThread.getAndSet(true)) {
            return;
        }
        Thread thread = new Thread(() -> {
            do {
                refreshThreadTask();
                try {
                    Thread.sleep(15 * 1000);
                } catch (Throwable e) {

                }
            } while (true);
        });
        thread.setName("xproc4j-meta-holder");
        thread.setDaemon(true);
        thread.start();
    }

    public static void refreshThreadTask() {
        log.debug("xml-search-refreshing");

        try {
            refreshXmlFiles();
        } catch (Throwable e) {
            log.warn("xml-search-refresh-xml-file-error", e);
        }
        try {
            collectProcedureMeta();
        } catch (Throwable e) {
            log.warn("xml-search-refresh-procedure-meta-error", e);
        }
    }


    public static void collectProcedureMeta() {
        for (Map.Entry<String, Map<String, VirtualFile>> projectEntry : XML_FILE_MAP.entrySet()) {
            Map<String, VirtualFile> projectXmlFileMap = projectEntry.getValue();
            Map<String, String> xmlFileMetaKeyMap = XML_FILE_META_KEY_MAP.computeIfAbsent(projectEntry.getKey(), k -> new ConcurrentHashMap<>());
            for (Map.Entry<String, VirtualFile> entry : projectXmlFileMap.entrySet()) {
                VirtualFile value = entry.getValue();
                if (value == null || !value.isValid()) {
                    xmlFileMetaKeyMap.remove(entry.getKey());
                    projectXmlFileMap.remove(entry.getKey());
                    continue;
                }
                // 性能关键：原实现 new File(path).length()/lastModified() 会对每个 XML
                // 每轮执行两次真实磁盘 syscall；工程放在机械盘或网络盘时单轮可达数百毫秒。
                // VFS 已经把 length 与 modificationStamp 缓存在内存中，直接读取即可。
                String metaKey = value.getLength() + "#" + value.getModificationStamp();

                String exKey = xmlFileMetaKeyMap.get(entry.getKey());
                if (Objects.equals(metaKey, exKey)) {
                    continue;
                }
                xmlFileMetaKeyMap.put(entry.getKey(), metaKey);

                try (InputStream is = value.getInputStream()) {
                    Xml xml = XmlUtil.parseXmlSax(value.getName(), is);
                    Xml root = XmlUtil.getRootNode(xml);
                    inflateXml2Meta(projectEntry.getKey(), null, 0, root, value);
                } catch (Throwable e) {

                }
            }
        }

    }

    public static void inflateXml2Meta(String projectPath, String parentName, int level, Xml root, VirtualFile value) {
        Map<String, ProcedureMeta> procedureMetaMap = PROCEDURE_META_MAP.computeIfAbsent(projectPath, k -> new ConcurrentHashMap<>());
        if (level > 4) {
            return;
        }
        String name = root.getName();
        if (TagConsts.PROCEDURE.equals(name)
                || TagConsts.SCRIPT_SEGMENT.equals(name)) {

            List<Xml> attributes = root.getAttributes();
            if (attributes != null) {

                ProcedureMeta meta = new ProcedureMeta();
                meta.setType(ProcedureMeta.Type.XML);
                meta.setArguments(new ArrayList<>());
                meta.setArgumentFeatures(new LinkedHashMap<>());
                for (Xml attr : attributes) {
                    String attrName = attr.getName();
                    if (AttrConsts.ID.equals(attrName)) {
                        meta.setName(attr.getValue());
                        continue;
                    }
                    String[] arr = attrName.split("\\.");
                    meta.getArguments().add(arr[0]);
                    for (int i = 1; i < arr.length; i++) {
                        List<String> features = meta.getArgumentFeatures().computeIfAbsent(arr[0], (key) -> new ArrayList<>());
                        features.add(arr[i]);
                    }
                }
                meta.setTarget(value);
                if (meta.getName() != null) {
//                    log.warn("xml-procedure-meta found:"+meta.getName());
                    procedureMetaMap.put(meta.getName(), meta);
                    if (parentName != null && !parentName.isEmpty()) {
                        procedureMetaMap.put(parentName + "." + meta.getName(), meta);
                    }
                    if (level == 0) {
                        parentName = meta.getName();
                    } else {
                        if (parentName != null && !parentName.contains(".")) {
                            parentName = parentName + "." + meta.getName();
                        }
                    }
                }
            }
        }
        List<Xml> children = root.getChildren();
        if (children != null) {
            for (Xml item : children) {
                inflateXml2Meta(projectPath, parentName, level + 1, item, value);
            }
        }
    }

    public static String getProjectPath(Project project) {
        String path = project.getProjectFilePath();
        if (path == null || path.isEmpty()) {
            // 非 .ipr 工程 / 尚未初始化时可能为 null，
            // 作为 ConcurrentHashMap 的 key 会直接 NPE。
            path = project.getBasePath();
        }
        if (path == null || path.isEmpty()) {
            path = project.getName();
        }
        return path;
    }


    public static void refreshXmlFiles() {


        ProjectManager projectManager = ProjectManager.getInstance();
        @NotNull Project[] projects = projectManager.getOpenProjects();
        for (Project project : projects) {
            String projectPath = getProjectPath(project);
            VirtualFile workspaceFile = project.getWorkspaceFile();
            if (workspaceFile == null) {
                workspaceFile = project.getProjectFile();
            }
            if (workspaceFile == null) {
                continue;
            }
            VirtualFile searchDir = workspaceFile.getParent();
            if (searchDir != null) {
                searchDir = searchDir.getParent();
            }
            if (searchDir == null) {
                continue;
            }
            log.debug("xml-search-dir:" + searchDir.getPath());
            collectXmlFile(projectPath, searchDir);
        }

    }

    public static void collectXmlFile(String projectPath, VirtualFile projectFile) {
        Map<String, VirtualFile> xmlFileMap = XML_FILE_MAP.computeIfAbsent(projectPath, k -> new ConcurrentHashMap<>());
        if (projectFile == null) {
            return;
        }
        String rootPath = projectFile.getPath();
        walkFileTree(projectFile, (file) -> {
            String name = file.getName();
            if (name.startsWith(".")) {
                return false;
            }
            if (file.isDirectory()) {
                if (Arrays.asList("node_modules",
                        ".idea", ".vscode",
                        ".git", ".svn",
                        ".github", ".gitlab", ".gitee",
                        ".gradle", ".mvn"
                ).contains(name)) {
                    return false;
                }
            } else if (!isXmlFileName(name)) {
                // 性能关键：非 XML 文件直接放行，不构造完整路径。
                // VirtualFile#getPath 是惰性计算（逐级拼接父路径 + 字符数组拷贝），
                // 原实现对目录树中每个文件都调用一次，在几十万文件的前端工程中
                // 单轮刷新就会产生数十万次字符串分配，而 .xml 占比通常不到 1%。
                return true;
            }
            String path = file.getPath();
            path = path.substring(rootPath.length());
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (path.startsWith("/output/")
                    || path.startsWith("/out/")
                    || path.startsWith("/target/")
                    || path.startsWith("/logs/")
                    || path.startsWith("/log/")
                    || path.startsWith("/build/")
                    || path.startsWith("/classes/")
                    || path.startsWith("/dist/")
                    || path.startsWith("/production/")) {
                return false;
            }
            if (path.contains("/target/classes/")
                    || path.contains("/build/classes/")) {
                return false;
            }
            return true;
        }, (file) -> {
            if (file.isDirectory()) {
                return;
            }
            String name = file.getName();
            if (!isXmlFileName(name)) {
                return;
            }
            String path = file.getPath();
            if (path.contains("/src/")
                    || path.contains("/resources/")) {
                path = path.substring(rootPath.length());
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }

                xmlFileMap.put(path, file);
            }
        });
    }

    /**
     * 廉价后缀判断：只比较末尾 4 个字符，不产生临时字符串，也不触发 VFS 路径计算。
     */
    private static boolean isXmlFileName(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        int idx = name.lastIndexOf(".");
        if (idx <= 0) {
            return false;
        }
        String suffix = name.substring(idx).toLowerCase();
        return ".xml".equals(suffix);
    }

    public static void walkFileTree(VirtualFile file, Predicate<VirtualFile> filter, Consumer<VirtualFile> consumer) {
        if (file == null) {
            return;
        }

        VfsUtilCore.visitChildrenRecursively(file, new VirtualFileVisitor<VirtualFile>() {
            @Override
            public boolean visitFile(@NotNull VirtualFile file) {
                if (filter != null && !filter.test(file)) {
                    return false;
                }
                consumer.accept(file);
                return true;
            }
        });
    }

}
