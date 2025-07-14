package i2f.extension.filesystem.oss.aliyun;


import com.aliyun.oss.OSS;
import com.aliyun.oss.model.*;
import i2f.extension.oss.aliyun.AliyunOssMeta;
import i2f.extension.oss.aliyun.AliyunOssUtil;
import i2f.io.filesystem.IFile;
import i2f.io.filesystem.abs.AbsFileSystem;
import i2f.io.stream.StreamUtil;

import java.io.*;
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
            try {
                return getClient().doesBucketExist(pair.getKey());
            } catch (Throwable e) {

            }
        } else {
            ObjectListing listing = getClient().listObjects(pair.getKey(), pair.getValue());
            List<OSSObjectSummary> iter = listing.getObjectSummaries();
            for (OSSObjectSummary item : iter) {
                try {
                    String name = item.getKey();
                    if (name.endsWith(pathSeparator())) {
                        name = name.substring(0, name.length() - pathSeparator().length());
                    }
                    if (name.equals(pair.getValue())) {
                        return true;
                    }

                } catch (Throwable e) {
                }
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
            try {
                return getClient().doesBucketExist(pair.getKey());
            } catch (Throwable e) {

            }
        } else {
            ObjectListing listing = getClient().listObjects(pair.getKey(), pair.getValue());
            List<OSSObjectSummary> iter = listing.getObjectSummaries();
            for (OSSObjectSummary item : iter) {
                try {
                    String name = item.getKey();
                    if (name.endsWith(pathSeparator())) {
                        name = name.substring(0, name.length() - pathSeparator().length());
                    }
                    if (name.equals(pair.getValue())) {
                        return true;
                    }
                } catch (Throwable e) {
                }
            }
        }

        return false;
    }

    @Override
    public List<IFile> listFiles(String path) {
        Map.Entry<String, String> pair = splitPathAsBucketAndObjectName(path);
        String subPath = ensureWithPathSeparator(pair.getValue());
        List<IFile> ret = new ArrayList<>();
        String nextMarker = null;
        ObjectListing listing = null;
        do {
            listing = getClient().listObjects(pair.getKey(), subPath);
            List<OSSObjectSummary> iter = listing.getObjectSummaries();
            for (OSSObjectSummary item : iter) {
                try {
                    String name = item.getKey();
                    ret.add(getFile(pair.getKey(), name));
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
        try {
            ObjectMetadata metadata = getClient().getObjectMetadata(pair.getKey(), pair.getValue());
            return metadata.getContentLength();
        } catch (Throwable e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
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
