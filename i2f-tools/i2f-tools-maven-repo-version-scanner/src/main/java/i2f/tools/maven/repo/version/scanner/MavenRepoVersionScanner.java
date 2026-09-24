package i2f.tools.maven.repo.version.scanner;

import i2f.io.file.ClassFileUtil;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class MavenRepoVersionScanner {

    public static void main(String[] args) throws Exception {
        if (args.length == 1) {
            if ("--help".equals(args[0])
                    || "-h".equals(args[0])) {
                System.out.println("java -jar this.jar [maven-repo-path] [save-file-name]");
                System.out.println("    such: java -jar this.jar D:\\maven\\mvn output.txt");
                return;
            }
        }
        String repoPath = args.length > 0 ? args[0]
                : System.getProperty("user.home") + "/.m2/repository";

        Path repoRoot = Paths.get(repoPath);
        if (!Files.exists(repoRoot)) {
            System.err.println("repo path not exists: " + repoPath);
            return;
        }

        if (Files.isSymbolicLink(repoRoot)) {
            repoRoot = repoRoot.toRealPath();
        }


        System.out.println("begin scan repo: " + repoRoot);

        String fileName = "mvn-jar-versions.txt";
        if (args.length > 1) {
            fileName = args[1];
        }
        File saveFile = new File(fileName);
        saveFile = new File(saveFile.getAbsolutePath());
        if (!saveFile.getParentFile().exists()) {
            saveFile.getParentFile().mkdirs();
        }

        // 打开输出文件
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        int[] count = {0};       // 已处理的 JAR 数
        int[] written = {0};     // 成功写入的行数

        Path scanRoot = repoRoot;
        try {
            Files.walkFileTree(scanRoot, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    String fileName = file.getFileName().toString();
                    if (!fileName.endsWith(".jar")) {
                        return FileVisitResult.CONTINUE;
                    }

                    count[0]++;

                    try {
                        Path extraPath = scanRoot.relativize(file);
                        String coordinate = extractCoordinate(extraPath);
                        if (coordinate == null) {
                            return FileVisitResult.CONTINUE;
                        }

                        String jdkVersion = extractJdkVersionFromJar(file);
                        if (jdkVersion != null) {
                            writer.write(coordinate + "@" + jdkVersion);
                            writer.newLine();
                            written[0]++;
                        }
                    } catch (Exception e) {
                        // 静默跳过损坏的 JAR
                    }

                    // 每 500 个打印进度
                    if (count[0] % 500 == 0) {
                        System.out.println("processed: " + count[0] + " jar, written: " + written[0] + " records.");
                    }

                    return FileVisitResult.CONTINUE;
                }
            });
        } finally {
            writer.close();
        }

        System.out.println("finished! processed " + count[0] + " jar, written " + written[0]
                + " records to " + fileName);
    }

    /**
     * 从 JAR 文件路径解析 Maven 坐标。
     * 目录结构: groupId路径/artifactId/version/artifactId-version.jar
     */
    private static String extractCoordinate(Path jarPath) {
        String fileName = jarPath.getFileName().toString();
        if (!fileName.endsWith(".jar")) {
            return null;
        }

        String baseName = fileName.substring(0, fileName.length() - 4);

        Path parent = jarPath.getParent();
        if (parent == null) {
            return null;
        }

        // 最后一层目录是 version
        String version = parent.getFileName().toString();

        // version 上一层是 artifactId
        Path artifactDir = parent.getParent();
        if (artifactDir == null) {
            return null;
        }
        String artifactId = artifactDir.getFileName().toString();

        // 再往上都是 groupId 的各级目录
        List<String> groupIdParts = new ArrayList<>();
        Path cursor = artifactDir.getParent();
        while (cursor != null && !cursor.equals(jarPath.getRoot())) {
            groupIdParts.add(0, cursor.getFileName().toString());
            cursor = cursor.getParent();
        }

        if (groupIdParts.isEmpty()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < groupIdParts.size(); i++) {
            if (i > 0) sb.append(".");
            sb.append(groupIdParts.get(i));
        }
        String groupId = sb.toString();

        // 校验：文件名应该是 artifactId-version.jar
        if (!baseName.startsWith(artifactId + "-")) {
            return null; // classifier 构件，跳过
        }

        return groupId + ":" + artifactId + ":" + version;
    }

    /**
     * 打开 JAR 文件，找到首个 .class 文件，读取 major_version。
     */
    private static String extractJdkVersionFromJar(Path jarPath) throws IOException {
        JarFile jarFile = null;
        try {
            int majorVersion = -1;
            // 采样一定数量的class文件选取最大的版本
            // 一般情况这并不需要采样多个文件
            int count = 100;
            jarFile = new JarFile(jarPath.toFile());
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();

                // 跳过 module-info 和 META-INF
                if (name.equals("module-info.class")) {
                    continue;
                }
                if (name.equals("package-info.class")) {
                    continue;
                }
                if (!name.endsWith(".class")) {
                    continue;
                }
                if (name.startsWith("META-INF/")) {
                    continue;
                }

                // 读取前 8 字节
                InputStream is = null;
                try {
                    is = jarFile.getInputStream(entry);

                    int ver = ClassFileUtil.getMajorVersion(is);
                    if (ver > majorVersion) {
                        majorVersion = ver;
                    }

                    count--;
                    if (count < 0) {
                        break;
                    }
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException ignored) {
                        }
                    }
                }
            }

            return ClassFileUtil.majorVersionToJdkVersion(majorVersion);
        } finally {
            if (jarFile != null) {
                try {
                    jarFile.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}