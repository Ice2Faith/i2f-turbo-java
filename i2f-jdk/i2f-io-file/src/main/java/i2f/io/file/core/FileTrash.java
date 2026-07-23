package i2f.io.file.core;

import i2f.io.file.FileUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
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
    public static final DateTimeFormatter FILE_FORMATTER = DateTimeFormatter.ofPattern("HHmmss");
    public static final DateTimeFormatter CREATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    public static final String META_FILE_NAME = "metadata.properties";

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
        dir = new File(dir, DIR_FORMATTER.format(now));
        dir.mkdirs();

        String nameOnly = file.getName();
        String suffix = "";
        int idx = nameOnly.lastIndexOf(".");
        if (idx >= 0) {
            suffix = nameOnly.substring(idx);
            nameOnly = nameOnly.substring(0, idx);
        }
        String name = FILE_FORMATTER.format(now) + "_" + (UUID.randomUUID().toString().replace("-", "")) + suffix;
        File ret = new File(dir, name);
        FileUtil.move(ret, file);

        File metaFile = new File(dir, META_FILE_NAME);
        Properties properties = new Properties();
        properties.put("name", file.getName());
        properties.put("originPath", new File(file.getAbsolutePath()).getParentFile().getAbsolutePath());
        properties.put("createTime", CREATE_TIME_FORMATTER.format(now));

        OutputStream fos = new FileOutputStream(metaFile);
        properties.store(fos, null);
        fos.close();
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
