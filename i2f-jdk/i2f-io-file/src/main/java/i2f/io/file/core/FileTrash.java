package i2f.io.file.core;

import i2f.io.file.FileUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @author Ice2Faith
 * @date 2026/7/22 20:22
 * @desc
 */
@Data
@NoArgsConstructor
public class FileTrash {
    public static final String DEFAULT_ROOT_PROPERTY = "file.trash.default";
    public static final FileTrash DEFAULT = new FileTrash();

    public static final String DEFAULT_ROOT = ".app_trash";
    public static final DateTimeFormatter DIR_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter FILE_FORMATTER = DateTimeFormatter.ofPattern("HHmmssSSS");
    public static final DateTimeFormatter CREATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    public static final String META_FILE_NAME = "metadata.txt";

    protected String rootDir = getDefaultRootDir();

    public FileTrash(String rootDir) {
        this.rootDir = rootDir;
    }

    public static String getDefaultRootDir() {
        String prop = System.getProperty(DEFAULT_ROOT_PROPERTY);
        if (prop != null && !prop.isEmpty()) {
            return prop;
        }
        return DEFAULT_ROOT;
    }

    public File moveToTrash(File file) throws IOException {
        if (!file.exists()) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        File dir = new File(rootDir);
        // 按天隔离
        dir = new File(dir, DIR_FORMATTER.format(now));
        // 同一天额按照时间大致有序
        String name = FILE_FORMATTER.format(now) + "_" + (UUID.randomUUID().toString().replace("-", ""));
        dir = new File(dir, name);
        dir.mkdirs();

        File ret = new File(dir, file.getName());
        FileUtil.move(ret, file);

        File metaFile = new File(dir, META_FILE_NAME);
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(metaFile), StandardCharsets.UTF_8))) {
            writer.println("name=" + file.getName());
            writer.println("originPath=" + new File(file.getAbsolutePath()).getParentFile().getAbsolutePath());
            writer.println("createTime=" + CREATE_TIME_FORMATTER.format(now));
        }

        return ret;
    }

    public void cleanTrash() throws IOException {
        File dir = new File(rootDir);
        if (!dir.exists()) {
            return;
        }
        FileUtil.delete(dir);
    }

    public void cleanTrashBeforeDays(int days) throws IOException {
        File dir = new File(rootDir);
        if (!dir.exists()) {
            return;
        }
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cmpDate = now.plusDays(-days);
        for (File file : files) {
            String name = file.getName();
            try {
                LocalDateTime itemDate = LocalDateTime.parse(name, DIR_FORMATTER);
                if (itemDate.isBefore(cmpDate)) {
                    FileUtil.delete(file);
                }
            } catch (Exception e) {
                // ignore error
            }
        }
    }
}
