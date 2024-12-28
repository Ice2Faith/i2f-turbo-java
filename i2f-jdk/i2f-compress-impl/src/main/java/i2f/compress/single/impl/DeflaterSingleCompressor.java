package i2f.compress.single.impl;

import i2f.compress.std.single.ISingleCompressor;
import i2f.io.stream.StreamUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterInputStream;
import java.util.zip.DeflaterOutputStream;

/**
 * @author Ice2Faith
 * @date 2024/6/29 15:44
 * @desc
 */
public class DeflaterSingleCompressor implements ISingleCompressor {
    @Override
    public void compress(InputStream is, OutputStream os) throws IOException {
        DeflaterOutputStream gos = new DeflaterOutputStream(os);
        StreamUtil.streamCopy(is,gos,true,true);
    }

    @Override
    public void release(InputStream is, OutputStream os) throws IOException {
        DeflaterInputStream gis=new DeflaterInputStream(is);
        StreamUtil.streamCopy(gis,os,true,true);
    }
}
