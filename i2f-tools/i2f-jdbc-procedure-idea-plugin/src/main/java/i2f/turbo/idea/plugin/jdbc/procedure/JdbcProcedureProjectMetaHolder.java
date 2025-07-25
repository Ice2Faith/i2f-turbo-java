package i2f.turbo.idea.plugin.jdbc.procedure;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileVisitor;
import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.xml.XmlUtil;
import i2f.xml.data.Xml;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class JdbcProcedureProjectMetaHolder {
    public static final Logger log = Logger.getInstance(JdbcProcedureProjectMetaHolder.class);

    public static final ConcurrentMap<String, VirtualFile> XML_FILE_MAP = new ConcurrentHashMap<>();
    public static final ConcurrentMap<String, ProcedureMeta> PROCEDURE_META_MAP = new ConcurrentHashMap<>();
    public static final List<String> FEATURES = Arrays.asList(
            "int",
            "double",
            "float",
            "string",
            "long",
            "short",
            "char",
            "byte",
            "boolean",
            "render",
            "visit",
            "eval",
            "test",
            "null",
            "date",
            "trim",
            "align",

            "body-text",
            "body-xml",
            "spacing-left",
            "spacing-right",
            "spacing",

            "eval-java",
            "eval-js",
            "eval-tinyscript",
            "eval-ts",
            "eval-groovy",
            "class",
            "not",

            "dialect",

            "is-null",
            "is-not-null",
            "is-empty",
            "is-not-empty",

            "date-now",
            "uuid",
            "current-time-millis",
            "snow-uid",

            "in",
            "out"

    );

    public static final List<String> FEATURES_CAUSE = Arrays.asList(
            "cause-first",
            "cause-last",
            "cause-raw",
            "cause-root"
    );

    public static final AtomicBoolean initRefreshThread = new AtomicBoolean(false);

    static {
        startRefreshThread();
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
        log.warn("xml-search-refreshing");

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
        for (Map.Entry<String, VirtualFile> entry : XML_FILE_MAP.entrySet()) {
            VirtualFile value = entry.getValue();
            try (InputStream is = value.getInputStream()) {
                Xml xml = XmlUtil.parseXmlSax(value.getName(), is);
                Xml root = XmlUtil.getRootNode(xml);
                inflateXml2Meta(null, 0, root, value);
            } catch (Exception e) {

            }
        }
    }

    public static void inflateXml2Meta(String parentName, int level, Xml root, VirtualFile value) {
        if (level > 4) {
            return;
        }
        String name = root.getName();
        if ("procedure".equals(name)
                || "script-segment".equals(name)) {

            List<Xml> attributes = root.getAttributes();
            if (attributes != null) {

                ProcedureMeta meta = new ProcedureMeta();
                meta.setType(ProcedureMeta.Type.XML);
                meta.setArguments(new ArrayList<>());
                meta.setArgumentFeatures(new LinkedHashMap<>());
                for (Xml attr : attributes) {
                    String attrName = attr.getName();
                    if ("id".equals(attrName)) {
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
                    PROCEDURE_META_MAP.put(meta.getName(), meta);
                    if (parentName != null && !parentName.isEmpty()) {
                        PROCEDURE_META_MAP.put(parentName + "." + meta.getName(), meta);
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
                inflateXml2Meta(parentName, level + 1, item, value);
            }
        }
    }


    public static void refreshXmlFiles() {


        ProjectManager projectManager = ProjectManager.getInstance();
        @NotNull Project[] projects = projectManager.getOpenProjects();
        for (Project project : projects) {
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
            log.warn("xml-search-dir:" + searchDir.getPath());
            collectXmlFile(searchDir);
        }

    }

    public static void collectXmlFile(VirtualFile projectFile) {
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
//            log.warn("xml-search-route:" + file.getPath());
            String suffix = file.getName();
            int idx = suffix.lastIndexOf(".");
            if (idx >= 0) {
                suffix = suffix.substring(idx);
            }

            if (".xml".equalsIgnoreCase(suffix)) {
                String path = file.getPath();
//                log.warn("xml-search-found:" + file.getName() + "," + file.getPath());
                if (path.contains("/src/")
                        || path.contains("/resources/")) {
                    path = path.substring(rootPath.length());
                    if (!path.startsWith("/")) {
                        path = "/" + path;
                    }

                    XML_FILE_MAP.put(path, file);
//                    log.warn("xml-search-match:" + file.getName() + "," + file.getPath());
                }
            }
        });
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
