package i2f.compress.impl.jdk;

import i2f.compress.data.CompressBindData;
import i2f.compress.impl.AbsCompressor;
import i2f.io.stream.StreamUtil;

import java.io.*;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author Ice2Faith
 * @date 2024/6/28 17:27
 * @desc
 */
public class ZipJdkCompressor extends AbsCompressor {
    @Override
    public void compressBindData(File output, Collection<CompressBindData> inputs) throws IOException {
        FileOutputStream fos = new FileOutputStream(output);
        ZipOutputStream zos = new ZipOutputStream(fos);
        for (CompressBindData input : inputs) {
            String path = input.getDirectory() + "/" + input.getFileName();
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            InputStream is = input.getInputStream();
            if (is == null) {
                continue;
            }
            ZipEntry entry = new ZipEntry(path);
            if(input.getSize()>=0){
                entry.setSize(input.getSize());
            }
            zos.putNextEntry(entry);
            StreamUtil.streamCopy(input.getInputStream(), zos, false, true);
            zos.flush();
            zos.closeEntry();
        }

        zos.close();
    }

    @Override
    public void release(File input, File output, BiConsumer<CompressBindData, File> consumer) throws IOException {
        InputStream fis = new FileInputStream(input);
        ZipInputStream zis = new ZipInputStream(fis);
        ZipEntry entry = null;
        while ((entry = zis.getNextEntry()) != null) {
            if (entry.isDirectory()) {
                zis.closeEntry();
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
            zis.closeEntry();
        }
        zis.close();
    }
}
