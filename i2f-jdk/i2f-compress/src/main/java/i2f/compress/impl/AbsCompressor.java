package i2f.compress.impl;


import i2f.compress.ICompressor;
import i2f.compress.data.CompressBindData;
import i2f.compress.data.CompressBindFile;
import i2f.io.stream.StreamUtil;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author ltb
 * @date 2022/3/31 15:38
 * @desc
 */
public abstract class AbsCompressor implements ICompressor {

    public List<CompressBindFile> fetchFiles(Collection<File> files, Predicate<File> filter) {
        List<CompressBindFile> ret = new LinkedList<>();
        String rootDir = "";
        for (File file : files) {
            fetchFilesNext(ret, file, rootDir, filter);
        }
        return ret;
    }

    protected void fetchFilesNext(List<CompressBindFile> ret, File file, String rootDir, Predicate<File> filter) {
        if (rootDir == null) {
            rootDir = "";
        }
        if (!file.exists()) {
            return;
        }
        if (filter != null && !filter.test(file)) {
            return;
        }
        if (file.isFile()) {
            ret.add(new CompressBindFile(file, rootDir));
        } else if (file.isDirectory()) {
            ret.add(new CompressBindFile(file, rootDir));
            String nextDir = rootDir + "/" + file.getName();
            if (nextDir.startsWith("/")) {
                nextDir = nextDir.substring(1);
            }
            File[] files = file.listFiles();
            if (files != null) {
                for (File item : files) {
                    if (filter != null && !filter.test(item)) {
                        continue;
                    }
                    fetchFilesNext(ret, item, nextDir, filter);
                }
            }
        }
    }


    @Override
    public void compressBindFile(File output, Collection<CompressBindFile> inputs) throws IOException {
        List<CompressBindData> list = new LinkedList<>();
        for (CompressBindFile item : inputs) {
            if (item == null) {
                continue;
            }
            CompressBindData data = CompressBindData.of(item);
            list.add(data);
        }
        if (!output.getParentFile().exists()) {
            output.getParentFile().mkdirs();
        }
        compressBindData(output, list);
        for (CompressBindData item : list) {
            InputStream is = item.getInputStream();
            if (is != null) {
                is.close();
            }
        }
    }

    @Override
    public void compressFile(File output, Collection<File> inputs, Predicate<File> filter) throws IOException {
        List<CompressBindFile> list = fetchFiles(inputs, filter);
        compressBindFile(output, list);
    }

    @Override
    public void release(File input, File output) throws IOException {
        if (!output.getParentFile().exists()) {
            output.getParentFile().mkdirs();
        }
        release(input, output, (data, rootDir) -> {
            String directory = data.getDirectory();
            String fileName = data.getFileName();
            File dir = new File(rootDir, directory);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                OutputStream os = new FileOutputStream(file);
                StreamUtil.streamCopy(data.getInputStream(), os, true, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
