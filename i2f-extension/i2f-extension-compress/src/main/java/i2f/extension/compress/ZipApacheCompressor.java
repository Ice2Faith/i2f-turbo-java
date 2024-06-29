package i2f.extension.compress;

import i2f.compress.data.CompressBindData;
import i2f.compress.impl.AbsCompressor;
import i2f.io.stream.StreamUtil;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import java.io.*;
import java.util.Collection;
import java.util.function.BiConsumer;

/**
 * @author Ice2Faith
 * @date 2024/6/29 15:28
 * @desc
 */
public class ZipApacheCompressor extends AbsCompressor {
    @Override
    public void compressBindData(File output, Collection<CompressBindData> inputs) throws IOException {
        FileOutputStream fos = new FileOutputStream(output);
        ZipArchiveOutputStream zos = new ZipArchiveOutputStream(fos);
        for (CompressBindData input : inputs) {
            String path = input.getDirectory() + "/" + input.getFileName();
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            InputStream is = input.getInputStream();
            if (is == null) {
                continue;
            }


            ZipArchiveEntry entry = new ZipArchiveEntry(path);
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
        ZipArchiveInputStream zis = new ZipArchiveInputStream(fis);
        ZipArchiveEntry entry = null;
        while ((entry = zis.getNextZipEntry()) != null) {
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
