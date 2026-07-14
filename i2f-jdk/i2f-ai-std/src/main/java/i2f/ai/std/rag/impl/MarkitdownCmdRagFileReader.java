package i2f.ai.std.rag.impl;

import i2f.ai.std.rag.RagFileReader;
import i2f.io.stream.StreamUtil;
import i2f.os.OsUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2026/7/14 18:38
 * @desc
 */
public class MarkitdownCmdRagFileReader implements RagFileReader {
    public static final MarkitdownCmdRagFileReader INSTANCE = new MarkitdownCmdRagFileReader();
    public static final Set<String> SUFFIXES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            ".doc", ".docx",
            ".xls", ".xlsx",
            ".ppt", ".pptx",
            ".pdf",
            ".epub"
    )));

    @Override
    public boolean support(File file) {
        String name = file.getName();
        int idx = name.lastIndexOf(".");
        String suffix = "";
        if (idx >= 0) {
            suffix = name.substring(idx).toLowerCase();
        }
        if (SUFFIXES.contains(suffix)) {
            return true;
        }
        return false;
    }

    @Override
    public String read(File file) throws IOException {
        File parentFile = file.getParentFile();
        File tmpFile = new File(parentFile, "tmp_" + UUID.randomUUID().toString().replace("-", "") + ".md");
        try {
            String cmdOutput = null;
            if (OsUtil.isWindows()) {
                cmdOutput = OsUtil.execCmd(true, TimeUnit.MINUTES.toMillis(3),
                        new String[]{"cmd", "/c", "markitdown", file.getName(), "-o", tmpFile.getName()},
                        null, parentFile, null
                );
            } else {
                cmdOutput = OsUtil.execCmd(true, TimeUnit.MINUTES.toMillis(3),
                        new String[]{"sh", "-c", "markitdown", file.getName(), "-o", tmpFile.getName()},
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
        }
    }
}
