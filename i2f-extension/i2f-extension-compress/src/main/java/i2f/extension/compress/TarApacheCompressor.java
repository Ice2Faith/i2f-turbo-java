package i2f.extension.compress;

import i2f.compress.std.data.CompressBindData;
import i2f.compress.std.impl.AbsCompressor;
import i2f.io.file.FileUtil;
import i2f.io.stream.StreamUtil;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import java.io.*;
import java.util.Collection;
import java.util.function.BiConsumer;

/**
 * @author Ice2Faith
 * @date 2024/6/29 15:28
 * @desc
 */
public class TarApacheCompressor extends AbsCompressor {
    @Override
    public void compressBindData(File output, Collection<CompressBindData> inputs) throws IOException {
        FileOutputStream fos = new FileOutputStream(output);
        TarArchiveOutputStream zos = new TarArchiveOutputStream(fos);
        for (CompressBindData input : inputs) {
            String path = input.getDirectory() + "/" + input.getFileName();
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            InputStream is = input.getInputStream();
            if (is == null) {
                continue;
            }

            File tpFile = null;
            long size = input.getSize();
            if (size < 0) {
                tpFile = FileUtil.getTempFile();
                FileUtil.save(is, tpFile);
                is.close();
                size = tpFile.length();
                is = new FileInputStream(tpFile);
            }


            TarArchiveEntry entry = new TarArchiveEntry(input.getFileName());
            entry.setSize(size);
            entry.setName(path);
            zos.putArchiveEntry(entry);
            StreamUtil.streamCopy(is, zos, false);
            zos.flush();
            zos.closeArchiveEntry();

            is.close();

            if (tpFile != null) {
                tpFile.delete();
            }
        }

        zos.close();
    }

    @Override
    public void release(File input, File output, BiConsumer<CompressBindData, File> consumer) throws IOException {
        InputStream fis = new FileInputStream(input);
        TarArchiveInputStream zis = new TarArchiveInputStream(fis);
        TarArchiveEntry entry = null;
        while ((entry = zis.getNextTarEntry()) != null) {
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
