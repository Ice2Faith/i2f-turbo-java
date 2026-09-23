package i2f.ai.std.rag.impl;

import i2f.ai.std.rag.RagFileReader;
import i2f.io.file.FileUtil;
import i2f.io.stream.StreamUtil;
import i2f.os.OsUtil;
import i2f.resources.ResourceUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2026/7/14 18:38
 * @desc
 */
@Data
@NoArgsConstructor
public class PdfEasyOcrCmdRagFileReader implements RagFileReader {
    public static final Set<String> SUFFIXES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            ".pdf"
    )));

    @FunctionalInterface
    public static interface Pdf2ImagesExtractor {
        List<File> extract(File pdfFile, File extractDir) throws IOException;
    }

    protected Pdf2ImagesExtractor extractor;

    public PdfEasyOcrCmdRagFileReader(Pdf2ImagesExtractor extractor) {
        this.extractor = extractor;
    }

    @Override
    public boolean support(File file) {
        String name = file.getName();
        int idx = name.lastIndexOf(".");
        String suffix = "";
        if (idx >= 0) {
            suffix = name.substring(idx).toLowerCase();
        }
        if (extractor == null) {
            return false;
        }
        if (SUFFIXES.contains(suffix)) {
            return true;
        }
        return false;
    }

    @Override
    public String read(File file) throws IOException {
        File parentFile = file.getParentFile();
        File imageDir = new File(parentFile, "images_" + UUID.randomUUID().toString().replace("-", ""));
        File listFile = new File(parentFile, "list_" + UUID.randomUUID().toString().replace("-", "") + ".txt");

        File tmpFile = new File(parentFile, "tmp_" + UUID.randomUUID().toString().replace("-", "") + ".md");
        File scriptFile = new File(parentFile, "ocr_" + UUID.randomUUID().toString().replace("-", "") + ".py");
        try {
            imageDir.mkdirs();

            List<File> imageFiles = extractor.extract(file, imageDir);

            StringBuilder builder = new StringBuilder();
            for (File imageFile : imageFiles) {
                builder.append(imageFile.getAbsolutePath()).append("\n");
            }
            StreamUtil.writeString(builder.toString(), listFile);

            InputStream sis = ResourceUtil.getClasspathResourceAsStream("/assets/python/ocr_easyocr.py");
            StreamUtil.writeBytes(sis, scriptFile);
            String cmdOutput = null;
            if (OsUtil.isWindows()) {
                cmdOutput = OsUtil.execCmd(true, TimeUnit.MINUTES.toMillis(10),
                        new String[]{"cmd", "/c", "python", scriptFile.getName(), "-f", listFile.getName(), "-o", tmpFile.getName()},
                        null, parentFile, null
                );
            } else {
                cmdOutput = OsUtil.execCmd(true, TimeUnit.MINUTES.toMillis(10),
                        new String[]{"sh", "-c", "python", scriptFile.getName(), "-f", listFile.getName(), "-o", tmpFile.getName()},
                        null, parentFile, null
                );
            }
            if (!tmpFile.exists()) {
                return null;
            }

            return StreamUtil.readString(tmpFile);

        } finally {
            if (tmpFile != null && tmpFile.exists()) {
                tmpFile.delete();
            }
            if (scriptFile != null && scriptFile.exists()) {
                scriptFile.delete();
            }
            if (imageDir.exists()) {
                FileUtil.delete(imageDir);
            }
            if (listFile.exists()) {
                listFile.delete();
            }
        }
    }
}
