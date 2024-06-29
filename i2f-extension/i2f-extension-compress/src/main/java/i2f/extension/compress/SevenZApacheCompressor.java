package i2f.extension.compress;

import i2f.compress.data.CompressBindData;
import i2f.compress.impl.AbsCompressor;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import java.io.*;
import java.util.Collection;
import java.util.function.BiConsumer;

/**
 * @author Ice2Faith
 * @date 2024/6/29 15:28
 * @desc
 */
public class SevenZApacheCompressor extends AbsCompressor {
    @Override
    public void compressBindData(File output, Collection<CompressBindData> inputs) throws IOException {
        SevenZOutputFile zFile=new SevenZOutputFile(output);
        for (CompressBindData input : inputs) {
            String path = input.getDirectory() + "/" + input.getFileName();
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            InputStream is = input.getInputStream();
            if (is == null) {
                continue;
            }


            SevenZArchiveEntry entry = new SevenZArchiveEntry();
            entry.setName(path);
            if (input.getSize() >= 0) {
                entry.setSize(input.getSize());
            }
            zFile.putArchiveEntry(entry);
            zFile.write(is);
            zFile.closeArchiveEntry();

            is.close();

        }

        zFile.close();
    }

    @Override
    public void release(File input, File output, BiConsumer<CompressBindData, File> consumer) throws IOException {
        SevenZFile zFile = new SevenZFile(input);
        SevenZArchiveEntry entry = null;
        while ((entry = zFile.getNextEntry()) != null) {
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
            InputStream is = new InputStream() {
                @Override
                public int read() throws IOException {
                    return zFile.read();
                }

                @Override
                public int read(byte[] b) throws IOException {
                    return zFile.read(b);
                }

                @Override
                public int read(byte[] b, int off, int len) throws IOException {
                    return zFile.read(b, off, len);
                }
            };
            CompressBindData data = new CompressBindData(fileName, directory, is);
            consumer.accept(data, output);
        }
        zFile.close();
    }
}
