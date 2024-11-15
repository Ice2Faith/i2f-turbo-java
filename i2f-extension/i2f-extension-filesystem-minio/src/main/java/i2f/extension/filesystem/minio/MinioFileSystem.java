package i2f.extension.filesystem.minio;


import i2f.extension.minio.MinioMeta;
import i2f.extension.minio.MinioUtil;
import i2f.io.filesystem.IFile;
import i2f.io.filesystem.abs.AbsFileSystem;
import i2f.io.stream.StreamUtil;
import io.minio.*;
import io.minio.messages.Item;

import java.io.*;
import java.util.*;

public class MinioFileSystem extends AbsFileSystem {
    private MinioMeta meta;
    private MinioClient client;

    public MinioFileSystem(MinioMeta meta) {
        this.meta = meta;
        this.client = getClient();
    }

    public MinioFileSystem(MinioClient client) {
        this.client = client;
    }

    public MinioClient getClient() {
        if (this.client == null) {
            this.client = MinioUtil.getClient(this.meta);
        }
        return client;
    }

    @Override
    public String pathSeparator() {
        return super.pathSeparator();
    }

    public Map.Entry<String, String> parseMinioPath(String path) {
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

    public String minioPath(String path) {
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
        return new MinioFile(this, path);
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
        Map.Entry<String, String> pair = parseMinioPath(path);
        if (pair.getValue() == null) {
            try {
                return getClient().bucketExists(BucketExistsArgs.builder().bucket(pair.getKey()).build());
            } catch (Throwable e) {

            }
        } else {
            Iterable<Result<Item>> iter = getClient().listObjects(ListObjectsArgs.builder()
                    .bucket(pair.getKey())
                    .prefix(pair.getValue())
                    .recursive(false)
                    .build()
            );
            for (Result<Item> res : iter) {
                try {
                    Item item = res.get();
                    if (item.isDir()) {
                        String name = item.objectName();
                        if (name.endsWith(pathSeparator())) {
                            name = name.substring(0, name.length() - pathSeparator().length());
                        }
                        if (name.equals(pair.getValue())) {
                            return true;
                        }
                    }
                } catch (Throwable e) {
                }
            }
        }

        return false;
    }

    @Override
    public boolean isFile(String path) {
        Map.Entry<String, String> pair = parseMinioPath(path);
        if (pair.getValue() == null) {
            return false;
        }
        try {
            ObjectStat stat = getClient().statObject(StatObjectArgs.builder()
                    .bucket(pair.getKey())
                    .object(pair.getValue())
                    .build()
            );
            return true;
        } catch (Throwable e) {

        }
        return false;
    }

    @Override
    public boolean isExists(String path) {
        if (path.endsWith(pathSeparator())) {
            path = path.substring(0, path.length() - pathSeparator().length());
        }
        Map.Entry<String, String> pair = parseMinioPath(path);
        if (pair.getValue() == null) {
            try {
                return getClient().bucketExists(BucketExistsArgs.builder().bucket(pair.getKey()).build());
            } catch (Throwable e) {

            }
        } else {
            Iterable<Result<Item>> iter = getClient().listObjects(ListObjectsArgs.builder()
                    .bucket(pair.getKey())
                    .prefix(pair.getValue())
                    .recursive(false)
                    .build()
            );
            for (Result<Item> res : iter) {
                try {
                    Item item = res.get();
                    String name = item.objectName();
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
        Map.Entry<String, String> pair = parseMinioPath(path);
        Iterable<Result<Item>> iter = getClient().listObjects(ListObjectsArgs.builder()
                .bucket(pair.getKey())
                .prefix(minioPath(pair.getValue()))
                .recursive(false)
                .build()
        );
        List<IFile> ret = new LinkedList<>();
        for (Result<Item> res : iter) {
            try {
                Item item = res.get();
                String name = item.objectName();
                ret.add(getFile(pair.getKey(), name));
            } catch (Throwable e) {
            }
        }
        return ret;
    }

    @Override
    public void delete(String path) {
        Map.Entry<String, String> pair = parseMinioPath(path);
        if (isFile(path)) {
            try {
                getClient().removeObject(RemoveObjectArgs.builder()
                        .bucket(pair.getKey())
                        .object(pair.getValue())
                        .build());
            } catch (Throwable e) {
                throw new IllegalStateException("delete failure:" + e.getMessage(), e);
            }
        } else {
            if (pair.getKey() != null && !"".equals(pair.getKey())) {
                if (pair.getValue() == null || "".equals(pair.getValue())) {
                    try {
                        getClient().removeBucket(RemoveBucketArgs.builder()
                                .bucket(pair.getKey())
                                .build());
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
        Map.Entry<String, String> pair = parseMinioPath(path);
        try {
            return getClient().getObject(GetObjectArgs.builder()
                    .bucket(pair.getKey())
                    .object(pair.getValue())
                    .build());
        } catch (Throwable e) {
            throw new IOException(e.getMessage(), e);
        }
    }


    @Override
    public OutputStream getOutputStream(String path) throws IOException {
        Map.Entry<String, String> pair = parseMinioPath(path);
        File tmpFile = File.createTempFile("minio-" + UUID.randomUUID().toString(), ".tmp");
        FileOutputStream fos = new FileOutputStream(tmpFile);
        return new FilterOutputStream(fos) {
            @Override
            public void close() throws IOException {
                super.close();
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(tmpFile);
                    getClient().putObject(PutObjectArgs.builder()
                            .bucket(pair.getKey())
                            .object(pair.getValue())
                            .stream(fis, tmpFile.length(), -1)
                            .build());
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
        throw new UnsupportedOperationException("minio not support appendable stream");
    }

    @Override
    public void mkdir(String path) {
        Map.Entry<String, String> pair = parseMinioPath(path);
        try {
            boolean exBkt = getClient().bucketExists(BucketExistsArgs.builder().bucket(pair.getKey()).build());
            if (!exBkt) {
                getClient().makeBucket(MakeBucketArgs.builder().bucket(pair.getKey()).build());
            }
            if (pair.getKey() != null) {
                String holdName = combinePath(pair.getValue(), ".ignore");
                getClient().putObject(PutObjectArgs.builder()
                        .bucket(pair.getKey())
                        .object(holdName)
                        .stream(new ByteArrayInputStream(new byte[0]), 0, -1)
                        .build());
            }
        } catch (Throwable e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void store(String path, InputStream is) throws IOException {
        Map.Entry<String, String> pair = parseMinioPath(path);
        try {
            getClient().putObject(PutObjectArgs.builder()
                    .bucket(pair.getKey())
                    .object(pair.getValue())
                    .stream(is, -1, -1)
                    .build());
        } catch (Throwable e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    @Override
    public void load(String path, OutputStream os) throws IOException {
        Map.Entry<String, String> pair = parseMinioPath(path);
        InputStream is = null;
        try {
            is = getClient().getObject(GetObjectArgs.builder()
                    .bucket(pair.getKey())
                    .object(pair.getValue())
                    .build());
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
        Map.Entry<String, String> pair = parseMinioPath(path);
        try {
            ObjectStat stat = getClient().statObject(StatObjectArgs.builder()
                    .bucket(pair.getKey())
                    .object(pair.getValue())
                    .build()
            );
            return stat.length();
        } catch (Throwable e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
