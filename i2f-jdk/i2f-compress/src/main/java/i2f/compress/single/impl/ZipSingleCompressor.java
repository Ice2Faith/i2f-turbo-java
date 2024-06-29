package i2f.compress.single.impl;

import i2f.compress.single.ISingleCompressor;
import i2f.io.stream.StreamUtil;
import i2f.io.stream.impl.BlackHoleOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.*;

/**
 * @author Ice2Faith
 * @date 2024/6/29 15:44
 * @desc
 */
public class ZipSingleCompressor implements ISingleCompressor {
    @Override
    public void compress(InputStream is, OutputStream os) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(os);
        zos.putNextEntry(new ZipEntry("data"));
        StreamUtil.streamCopy(is, zos, false, true);
        zos.closeEntry();
        zos.close();
    }

    @Override
    public void release(InputStream is, OutputStream os) throws IOException {
        ZipInputStream zis = new ZipInputStream(is);
        ZipEntry entry = null;
        BlackHoleOutputStream bhos = new BlackHoleOutputStream();
        while ((entry = zis.getNextEntry()) != null) {
            if ("data".equals(entry.getName())) {
                StreamUtil.streamCopy(zis, os, false, false);
            } else {
                StreamUtil.streamCopy(zis, bhos, false, false);
            }
            zis.closeEntry();
        }
        is.close();
        os.close();
    }
}
