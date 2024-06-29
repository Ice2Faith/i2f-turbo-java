package i2f.compress.single.impl;

import i2f.compress.single.ISingleCompressor;
import i2f.io.stream.StreamUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author Ice2Faith
 * @date 2024/6/29 15:44
 * @desc
 */
public class GzipSingleCompressor implements ISingleCompressor {
    @Override
    public void compress(InputStream is, OutputStream os) throws IOException {
        GZIPOutputStream gos = new GZIPOutputStream(os);
        StreamUtil.streamCopy(is,gos,true,true);
    }

    @Override
    public void release(InputStream is, OutputStream os) throws IOException {
        GZIPInputStream gis=new GZIPInputStream(is);
        StreamUtil.streamCopy(gis,os,true,true);
    }
}
