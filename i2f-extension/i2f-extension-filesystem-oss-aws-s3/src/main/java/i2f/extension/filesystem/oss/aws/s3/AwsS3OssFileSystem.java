package i2f.extension.filesystem.oss.aws.s3;


import i2f.extension.oss.aws.s3.AwsS3OssMeta;
import i2f.extension.oss.aws.s3.AwsS3OssUtil;
import i2f.io.filesystem.IFile;
import i2f.io.filesystem.abs.AbsFileSystem;
import i2f.io.stream.StreamUtil;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class AwsS3OssFileSystem extends AbsFileSystem {
    private AwsS3OssMeta meta;
    protected S3Client client;

    public AwsS3OssFileSystem(AwsS3OssMeta meta) {
        this.meta = meta;
        this.client = getClient();
    }

    public AwsS3OssFileSystem(S3Client client) {
        this.client = client;
    }


    public S3Client getClient() {
        if (this.client == null) {
            this.client = AwsS3OssUtil.getClient(this.meta);
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
        return new AwsS3OssFile(this, path);
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
                GetBucketPolicyStatusResponse resp = getClient().getBucketPolicyStatus(e -> {
                    e.bucket(pair.getKey());
                });
                if (resp.sdkHttpResponse().isSuccessful()) {
                    PolicyStatus policyStatus = resp.policyStatus();
                    if (policyStatus != null) {
                        return true;
                    }
                }
                return false;
            } catch (Throwable e) {

            }
        } else {
            ListObjectsResponse resp = getClient().listObjects(e -> {
                e.bucket(pair.getKey())
                        .prefix(pair.getValue());
            });
            if (resp == null) {
                return false;
            }
            List<S3Object> contents = resp.contents();
            for (S3Object item : contents) {
                try {
                    String name = item.key();
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
            ResponseInputStream<GetObjectResponse> resp = getClient().getObject(e -> {
                e.bucket(pair.getKey())
                        .key(pair.getValue());
            });
            return resp != null && resp.response() != null;
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
            GetBucketPolicyStatusResponse resp = getClient().getBucketPolicyStatus(e -> {
                e.bucket(pair.getKey());
            });
            if (resp.sdkHttpResponse().isSuccessful()) {
                PolicyStatus policyStatus = resp.policyStatus();
                if (policyStatus != null) {
                    return true;
                }
            }

        } else {
            ResponseInputStream<GetObjectResponse> resp = getClient().getObject(e -> {
                e.bucket(pair.getKey())
                        .key(pair.getValue());
            });
            return resp != null && resp.response() != null;
        }
        return false;
    }

    @Override
    public List<IFile> listFiles(String path) {
        List<IFile> ret = new LinkedList<>();
        Map.Entry<String, String> pair = splitPathAsBucketAndObjectName(path);
        if("".equals(pair.getKey()) && pair.getValue()==null){
            // 根目录，应该列举bucket
            try {
                ListBucketsResponse resp = getClient().listBuckets();
                if(resp!=null) {
                    List<Bucket> buckets = resp.buckets();
                    for (Bucket bucket : buckets) {
                        String name = bucket.name();
                        IFile file = getFile(pathSeparator() + name);
                        ret.add(file);
                    }
                }
            } catch (Throwable e) {

            }
            return ret;
        }
        String subPath = ensureWithPathSeparator(pair.getValue());
        ListObjectsResponse resp = null;
        AtomicReference<String> nextMarker = new AtomicReference<>(null);
        do {
            resp = getClient().listObjects(e -> {
                e.bucket(pair.getKey())
                        .prefix(subPath)
                        .marker(nextMarker.get());
            });
            if (resp == null) {
                break;
            }
            List<S3Object> contents = resp.contents();
            for (S3Object item : contents) {
                ret.add(getFile(pair.getKey(), item.key()));
            }
            nextMarker.set(resp.nextMarker());
        } while (resp != null && resp.isTruncated());

        return ret;
    }

    @Override
    public void delete(String path) {
        Map.Entry<String, String> pair = splitPathAsBucketAndObjectName(path);
        if (isFile(path)) {
            try {
                getClient().deleteObject(e -> {
                    e.bucket(pair.getKey())
                            .key(pair.getValue());
                });
            } catch (Throwable e) {
                throw new IllegalStateException("delete failure:" + e.getMessage(), e);
            }
        } else {
            if (pair.getKey() != null && !"".equals(pair.getKey())) {
                if (pair.getValue() == null || "".equals(pair.getValue())) {
                    try {
                        getClient().deleteBucket(e -> {
                            e.bucket(pair.getKey());
                        });
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
            ResponseInputStream<GetObjectResponse> resp = getClient().getObject(e -> {
                e.bucket(pair.getKey())
                        .key(pair.getValue());
            });
            return resp;
        } catch (Throwable e) {
            throw new IOException(e.getMessage(), e);
        }
    }


    @Override
    public OutputStream getOutputStream(String path) throws IOException {
        Map.Entry<String, String> pair = splitPathAsBucketAndObjectName(path);
        File tmpFile = File.createTempFile("awss3-oss-" + UUID.randomUUID().toString(), ".tmp");
        FileOutputStream fos = new FileOutputStream(tmpFile);
        return new FilterOutputStream(fos) {
            @Override
            public void close() throws IOException {
                super.close();
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(tmpFile);

                    PutObjectResponse resp = getClient().putObject(e -> {
                        e.bucket(pair.getKey())
                                .key(pair.getValue())
                                .contentLength(tmpFile.length());
                    }, RequestBody.fromFile(tmpFile));
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
        throw new UnsupportedOperationException("awss3-oss not support appendable stream");
    }

    @Override
    public void mkdir(String path) {
        Map.Entry<String, String> pair = splitPathAsBucketAndObjectName(path);
        try {
            boolean exBkt = false;
            try {
                GetBucketPolicyStatusResponse resp = getClient().getBucketPolicyStatus(e -> {
                    e.bucket(pair.getKey());
                });
                if (resp.sdkHttpResponse().isSuccessful()) {
                    PolicyStatus policyStatus = resp.policyStatus();
                    if (policyStatus != null) {
                        exBkt = true;
                    }
                }
            } catch (Throwable e) {

            }
            if (!exBkt) {
                CreateBucketResponse resp = getClient().createBucket(e -> {
                    e.bucket(pair.getKey())
                            .createBucketConfiguration(
                                    CreateBucketConfiguration.builder()
                                            .build()
                            );
                });
            }
            if (pair.getKey() != null) {
                String holdName = combinePath(pair.getValue(), ".ignore");

                PutObjectResponse resp = getClient().putObject(e -> {
                    e.bucket(pair.getKey())
                            .key(holdName)
                            .contentLength(0L)
                            .contentType("application/octet-stream");
                }, RequestBody.fromInputStream(new ByteArrayInputStream(new byte[0]), 0L));
            }
        } catch (Throwable e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void store(String path, InputStream is) throws IOException {
        Map.Entry<String, String> pair = splitPathAsBucketAndObjectName(path);
        try {
            PutObjectResponse resp = getClient().putObject(e -> {
                e.bucket(pair.getKey())
                        .key(pair.getValue())
                        .contentLength(0L);
            }, RequestBody.fromInputStream(is, -1L));

        } catch (Throwable e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    @Override
    public void load(String path, OutputStream os) throws IOException {
        Map.Entry<String, String> pair = splitPathAsBucketAndObjectName(path);
        InputStream is = null;
        try {
            is = getClient().getObject(e -> {
                e.bucket(pair.getKey())
                        .key(pair.getValue());
            });

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
                GetBucketPolicyStatusResponse resp = getClient().getBucketPolicyStatus(e -> {
                    e.bucket(pair.getKey());
                });
                if (resp.sdkHttpResponse().isSuccessful()) {
                    PolicyStatus policyStatus = resp.policyStatus();
                    if (policyStatus != null) {
                        return 0;
                    }
                }
                return -1;
            } catch (Throwable e) {

            }
        }
        try {
            ResponseInputStream<GetObjectResponse> resp = getClient().getObject(e -> {
                e.bucket(pair.getKey())
                        .key(pair.getValue());
            });

            int ret = resp.available();
            resp.close();
            return ret;
        } catch (Throwable e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void copyTo(String srcPath, String dstPath) throws IOException {
        Map.Entry<String, String> srcPair = splitPathAsBucketAndObjectName(srcPath);
        Map.Entry<String, String> dstPair = splitPathAsBucketAndObjectName(dstPath);
        try {
            getClient().copyObject(e -> {
                e.sourceBucket(srcPair.getKey())
                        .sourceKey(srcPair.getValue())
                        .destinationBucket(dstPair.getKey())
                        .destinationKey(dstPair.getValue());
            });
        } catch (Throwable e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void moveTo(String srcPath, String dstPath) throws IOException {
        Map.Entry<String, String> srcPair = splitPathAsBucketAndObjectName(srcPath);
        Map.Entry<String, String> dstPair = splitPathAsBucketAndObjectName(dstPath);
        try {

            getClient().copyObject(e -> {
                e.sourceBucket(srcPair.getKey())
                        .sourceKey(srcPair.getValue())
                        .destinationBucket(dstPair.getKey())
                        .destinationKey(dstPair.getValue());
            });

            getClient().deleteObject(e -> {
                e.bucket(srcPair.getKey())
                        .key(srcPair.getValue());
            });
        } catch (Throwable e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
