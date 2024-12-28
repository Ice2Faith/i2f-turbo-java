package i2f.extension.compress;

import i2f.compress.impl.AbsCompressor;
import i2f.compress.std.data.CompressBindData;
import i2f.io.stream.StreamUtil;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;

import java.io.*;
import java.util.Collection;
import java.util.function.BiConsumer;

/**
 * @author Ice2Faith
 * @date 2024/6/29 15:28
 * @desc
 */
public class JarApacheCompressor extends AbsCompressor {
    @Override
    public void compressBindData(File output, Collection<CompressBindData> inputs) throws IOException {
        FileOutputStream fos = new FileOutputStream(output);
        JarArchiveOutputStream zos = new JarArchiveOutputStream(fos);
        for (CompressBindData input : inputs) {
            String path = input.getDirectory() + "/" + input.getFileName();
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            InputStream is = input.getInputStream();
            if (is == null) {
                continue;
            }


            JarArchiveEntry entry = new JarArchiveEntry(path);
            if (input.getSize() >= 0) {
                entry.setSize(input.getSize());
            }
            zos.putArchiveEntry(entry);
            StreamUtil.streamCopy(is, zos, false);
            zos.flush();
            zos.closeArchiveEntry();

            is.close();

        }

        zos.close();
    }

    @Override
    public void release(File input, File output, BiConsumer<CompressBindData, File> consumer) throws IOException {
        InputStream fis = new FileInputStream(input);
        JarArchiveInputStream zis = new JarArchiveInputStream(fis);
        JarArchiveEntry entry = null;
        while ((entry = zis.getNextJarEntry()) != null) {
            if (entry.isDirectory()) {
                continue;
            }
            String path = entry.getName();
            String directory = "";
            String fileName = path;
            int idx = path.lastIndexOf("/");
            if (idx >= 0) {
                directory = path.substring(0, idx);
                fileName = fileName.substring(idx + 1);
            }
            CompressBindData data = new CompressBindData(fileName, directory, zis);
            consumer.accept(data, output);
        }
        zis.close();
    }
}
