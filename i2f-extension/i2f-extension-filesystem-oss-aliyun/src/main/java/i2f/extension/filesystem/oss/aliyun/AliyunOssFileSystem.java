package i2f.extension.filesystem.oss.aliyun;


import com.aliyun.oss.OSS;
import com.aliyun.oss.model.*;
import i2f.extension.oss.aliyun.AliyunOssMeta;
import i2f.extension.oss.aliyun.AliyunOssUtil;
import i2f.io.filesystem.IFile;
import i2f.io.filesystem.abs.AbsFileSystem;
import i2f.io.stream.StreamUtil;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;

public class AliyunOssFileSystem extends AbsFileSystem {
    private AliyunOssMeta meta;
    private OSS client;

    public AliyunOssFileSystem(AliyunOssMeta meta) {
        this.meta = meta;
        this.client = getClient();
    }

    public AliyunOssFileSystem(OSS client) {
        this.client = client;
    }

    public OSS getClient() {
        if (this.client == null) {
            this.client = AliyunOssUtil.getClient(this.meta);
        }
        return client;
    }

    @Override
    public String pathSeparator() {
        return super.pathSeparator();
    }

    public Map.Entry<String, String> splitPathAsBucketAndObjectName(String path) {
        String bucketName = null;
        String objectName = null;
        if (path == null) {
            path = "";
        }
        if (path.startsWith(pathSeparator())) {
            path = path.substring(pathSeparator().length());
        }

        int idx = path.indexOf(pathSeparator());
        if (idx >= 0) {
            bucketName = path.substring(0, idx);
            objectName = path.substring(idx + 1);
        } else {
            bucketName = path;
        }

        return new AbstractMap.SimpleEntry<>(bucketName, objectName);
    }

    public String ensureWithPathSeparator(String path) {
        if (path == null) {
            return null;
        }
        if (path.endsWith(pathSeparator())) {
            return path;
        }
        return path + pathSeparator();
    }

    @Override
    public IFile getFile(String path) {
        return new AliyunOssFile(this, path);
    }

    @Override
    public String getAbsolutePath(String path) {
        return path;
    }

    @Override
    public boolean isDirectory(String path) {
        if (path.endsWith(pathSeparator())) {
            path = path.substring(0, path.length() - pathSeparator().length());
        }
        Map.Entry<String, String> pair = splitPathAsBucketAndObjectName(path);
        if (pair.getValue() == null) {
            if("".equals(pair.getKey())){
                // 根目录，一定是目录
                return true;
            }
            try {
                return getClient().doesBucketExist(pair.getKey());
            } catch (Throwable e) {

            }
        } else {
            ObjectListing listing = getClient().listObjects(pair.getKey(), ensureWithPathSeparator(pair.getValue()));
            List<OSSObjectSummary> iter = listing.getObjectSummaries();
            for (OSSObjectSummary item : iter) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isFile(String path) {
        Map.Entry<String, String> pair = splitPathAsBucketAndObjectName(path);
        if (pair.getValue() == null) {
            return false;
        }
        try {
            return getClient().doesObjectExist(pair.getKey(), pair.getValue());
        } catch (Throwable e) {

        }
        return false;
    }

    @Override
    public boolean isExists(String path) {
        if (path.endsWith(pathSeparator())) {
            path = path.substring(0, path.length() - pathSeparator().length());
        }
        Map.Entry<String, String> pair = splitPathAsBucketAndObjectName(path);
        if (pair.getValue() == null) {
            // 根目录，一定存在
            if("".equals(pair.getKey())){
                return true;
            }
            try {
                return getClient().doesBucketExist(pair.getKey());
            } catch (Throwable e) {

            }
        } else {
            // 是文件
            try {
                boolean ok= getClient().doesObjectExist(pair.getKey(), pair.getValue());
                if(ok){
                    return true;
                }
            } catch (Exception e) {

            }

            // 是目录
            try {
                ObjectListing listing = getClient().listObjects(pair.getKey(), ensureWithPathSeparator(pair.getValue()));
                List<OSSObjectSummary> iter = listing.getObjectSummaries();
                for (OSSObjectSummary item : iter) {
                    return true;
                }
            } catch (Exception e) {

            }
        }

        return false;
    }

    public String decodeObjectName(String name){
        try {
            return URLDecoder.decode(name,"UTF-8");
        } catch (UnsupportedEncodingException e) {

        }
        return name;
    }

    @Override
    public List<IFile> listFiles(String path) {
        List<IFile> ret = new LinkedList<>();
        Map.Entry<String, String> pair = splitPathAsBucketAndObjectName(path);
        if("".equals(pair.getKey()) && pair.getValue()==null){
            // 根目录，应该列举bucket
            try {
                List<Bucket> buckets = getClient().listBuckets();
                for (Bucket bucket : buckets) {
                    String name = bucket.getName();
                    IFile file = getFile(pathSeparator() + name);
                    ret.add(file);
                }
            } catch (Throwable e) {

            }
            return ret;
        }
        String inPath=path;
        if(inPath.endsWith(pathSeparator())){
            inPath=inPath.substring(0,inPath.length()-pathSeparator().length());
        }
        Set<String> savePathSet=new LinkedHashSet<>();
        String subPath = ensureWithPathSeparator(pair.getValue());
        String nextMarker = null;
        ObjectListing listing = null;
        do {
            listing = getClient().listObjects(pair.getKey(), subPath);
            List<OSSObjectSummary> iter = listing.getObjectSummaries();
            for (OSSObjectSummary item : iter) {
                try {
                    String name = decodeObjectName(item.getKey());
                    if (name.endsWith(this.pathSeparator())) {
                        name = name.substring(0, name.length() - this.pathSeparator().length());
                    }

                    if (name.startsWith(this.pathSeparator())) {
                        name = name.substring(this.pathSeparator().length());
                    }

                    String subName=name;
                    if(subPath!=null){
                        if(subName.startsWith(subPath)){
                            subName=subName.substring(subPath.length());
                        }
                    }

                    int idx = subName.indexOf(this.pathSeparator());
                    if (idx >= 0) {
                        subName = subName.substring(0, idx);
                    }

                    IFile file = this.getFile(this.pathSeparator() + (String)pair.getKey(), subPath + subName);
                    String savePath = file.getPath();
                    if(Objects.equals(savePath, inPath)){
                        continue;
                    }
                    if(savePathSet.contains(savePath)){
                        continue;
                    }
                    savePathSet.add(savePath);
                    ret.add(file);
                } catch (Throwable e) {
                }
            }
            nextMarker = listing.getNextMarker();
        } while (listing.isTruncated());
        return ret;
    }

    @Override
    public void delete(String path) {
        Map.Entry<String, String> pair = splitPathAsBucketAndObjectName(path);
        if (isFile(path)) {
            try {
                getClient().deleteObject(pair.getKey(), pair.getValue());
            } catch (Throwable e) {
                throw new IllegalStateException("delete failure:" + e.getMessage(), e);
            }
        } else {
            if (pair.getKey() != null && !"".equals(pair.getKey())) {
                if (pair.getValue() == null || "".equals(pair.getValue())) {
                    try {
                        getClient().deleteBucket(pair.getKey());
                    } catch (Exception e) {
                        throw new IllegalStateException("delete failure:" + e.getMessage(), e);
                    }
                }
            }
        }

    }

    @Override
    public boolean isAppendable(String path) {
        return false;
    }

    @Override
    public InputStream getInputStream(String path) throws IOException {
        Map.Entry<String, String> pair = splitPathAsBucketAndObjectName(path);
        try {

            OSSObject obj = getClient().getObject(pair.getKey(), pair.getValue());
            return obj.getObjectContent();
        } catch (Throwable e) {
            throw new IOException(e.getMessage(), e);
        }
    }


    @Override
    public OutputStream getOutputStream(String path) throws IOException {
        Map.Entry<String, String> pair = splitPathAsBucketAndObjectName(path);
        File tmpFile = File.createTempFile("aliyun-oss-" + UUID.randomUUID().toString(), ".tmp");
        FileOutputStream fos = new FileOutputStream(tmpFile);
        return new FilterOutputStream(fos) {
            @Override
            public void close() throws IOException {
                super.close();
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(tmpFile);
                    ObjectMetadata metadata = new ObjectMetadata();
                    metadata.setContentLength(tmpFile.length());

                    PutObjectRequest req = new PutObjectRequest(pair.getKey(), pair.getValue(), fis, metadata);
                    PutObjectResult resp = getClient().putObject(req);

                } catch (Throwable e) {
                    throw new IOException(e.getMessage(), e);
                } finally {
                    if (fis != null) {
                        fis.close();
                    }
                    tmpFile.delete();
                }
            }
        };
    }

    @Override
    public OutputStream getAppendOutputStream(String path) throws IOException {
        throw new UnsupportedOperationException("aliyun-oss not support appendable stream");
    }

    @Override
    public void mkdir(String path) {
        Map.Entry<String, String> pair = splitPathAsBucketAndObjectName(path);
        try {
            boolean exBkt = getClient().doesBucketExist(pair.getKey());
            if (!exBkt) {
                getClient().createBucket(pair.getKey());
            }
            if (pair.getKey() != null) {
                String holdName = combinePath(pair.getValue(), ".ignore");

                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(0);
                metadata.setContentType("application/octet-stream");

                PutObjectRequest req = new PutObjectRequest(pair.getKey(), holdName, new ByteArrayInputStream(new byte[0]), metadata);
                PutObjectResult resp = getClient().putObject(req);

            }
        } catch (Throwable e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void store(String path, InputStream is) throws IOException {
        Map.Entry<String, String> pair = splitPathAsBucketAndObjectName(path);
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            PutObjectRequest req = new PutObjectRequest(pair.getKey(), pair.getValue(), is, metadata);
            PutObjectResult resp = getClient().putObject(req);

        } catch (Throwable e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    @Override
    public void load(String path, OutputStream os) throws IOException {
        Map.Entry<String, String> pair = splitPathAsBucketAndObjectName(path);
        InputStream is = null;
        try {
            OSSObject obj = getClient().getObject(pair.getKey(), pair.getValue());
            is = obj.getObjectContent();

            StreamUtil.streamCopy(is, os);
        } catch (Throwable e) {
            throw new IOException(e.getMessage(), e);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    @Override
    public long length(String path) {
        Map.Entry<String, String> pair = splitPathAsBucketAndObjectName(path);
        if(pair.getValue()==null){
            if("".equals(pair.getKey())){
                // 根目录，直接返回0
                return 0;
            }
            try {
                boolean ok = getClient().doesBucketExist(pair.getKey());
                return ok?0:-1;
            } catch (Throwable e) {

            }
        }
        try {
            ObjectMetadata metadata = getClient().getObjectMetadata(pair.getKey(), pair.getValue());
            return metadata.getContentLength();
        } catch (Throwable e) {

        }
        return -1;
    }

    @Override
    public void copyTo(String srcPath, String dstPath) throws IOException {
        Map.Entry<String, String> srcPair = splitPathAsBucketAndObjectName(srcPath);
        Map.Entry<String, String> dstPair = splitPathAsBucketAndObjectName(dstPath);
        try {
            getClient().copyObject(srcPair.getKey(), srcPair.getValue(), dstPair.getKey(), dstPair.getValue());
        } catch (Throwable e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void moveTo(String srcPath, String dstPath) throws IOException {
        Map.Entry<String, String> srcPair = splitPathAsBucketAndObjectName(srcPath);
        Map.Entry<String, String> dstPair = splitPathAsBucketAndObjectName(dstPath);
        try {
            if (Objects.equals(srcPair.getKey(), dstPair.getKey())) {
                getClient().renameObject(srcPair.getKey(), srcPair.getValue(), dstPair.getValue());
            } else {
                getClient().copyObject(srcPair.getKey(), srcPair.getValue(), dstPair.getKey(), dstPair.getValue());
                getClient().deleteObject(srcPair.getKey(), srcPair.getValue());
            }
        } catch (Throwable e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
