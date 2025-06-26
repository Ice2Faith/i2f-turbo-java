package i2f.extension.oss.aliyun;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.model.*;
import lombok.Data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2022/7/8 9:09
 * @desc
 */
@Data
public class AliyunOssUtil {
    protected OSS client;

    public AliyunOssUtil(OSS client) {
        this.client = client;
    }

    public AliyunOssUtil(AliyunOssMeta meta) {
        this.client = getClient(meta);
    }

    public static OSS getClient(AliyunOssMeta meta) {
        CredentialsProvider credentialsProvider =
                CredentialsProviderFactory.newDefaultCredentialProvider(meta.getAccessKeyId(), meta.getAccessKeySecret());

        // 创建OSSClient实例。
        // 当OSSClient实例不再使用时，调用shutdown方法以释放资源。
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        // 显式声明使用 V4 签名算法
        clientBuilderConfiguration.setSignatureVersion(meta.getSignVersion());
        return OSSClientBuilder.create()
                .endpoint(meta.getUrl())
                .credentialsProvider(credentialsProvider)
                .region(meta.getRegion())
                .build();
    }

    public boolean bucketExists(String bucketName) throws IOException {
        try {
            return client.doesBucketExist(bucketName);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public void bucketCreate(String bucketName) throws IOException {
        if (bucketExists(bucketName)) {
            return;
        }
        try {
            client.createBucket(bucketName);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public void bucketRemove(String bucketName) throws IOException {
        try {
            client.deleteBucket(bucketName);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }


    public ObjectMetadata getObjectInfo(String bucketName, String objectName) throws IOException {
        try {
            return client.getObjectMetadata(bucketName, objectName);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public PutObjectResult upload(String bucketName, String objectName, InputStream is, Long fileSize, String mimeType) throws IOException {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            if (fileSize != null) {
                metadata.setContentLength(fileSize);
            }
            if (mimeType != null) {
                metadata.setContentType(mimeType);
            }
            PutObjectRequest req = new PutObjectRequest(bucketName, objectName, is, metadata);
            PutObjectResult resp = client.putObject(req);
            return resp;
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public PutObjectResult upload(String bucketName, String objectName, InputStream is, String contentType) throws IOException {
        return upload(bucketName, objectName, is, -1L, contentType);
    }

    public PutObjectResult upload(String bucketName, String objectName, File file, String contentType) throws IOException {
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            return upload(bucketName, objectName, is, file.length(), contentType);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public InputStream download(String bucketName, String objectName) throws IOException {
        try {
            OSSObject obj = client.getObject(bucketName, objectName);
            return obj.getObjectContent();
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public List<OSSObjectSummary> list(String bucketName) {
        List<OSSObjectSummary> ret = new ArrayList<>();
        String nextMarker = null;
        ObjectListing listing = null;
        do {
            listing = client.listObjects(bucketName);
            ret.addAll(listing.getObjectSummaries());
            nextMarker = listing.getNextMarker();
        } while (listing.isTruncated());
        return ret;
    }

    public List<OSSObjectSummary> list(String bucketName, String prefix) {
        List<OSSObjectSummary> ret = new ArrayList<>();
        String nextMarker = null;
        ObjectListing listing = null;
        do {
            listing = client.listObjects(bucketName, prefix);
            ret.addAll(listing.getObjectSummaries());
            nextMarker = listing.getNextMarker();
        } while (listing.isTruncated());
        return ret;
    }

    public boolean prefixExists(String bucketName, String prefix) throws IOException {
        try {
            return client.doesObjectExist(bucketName, prefix);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public void prefixCreate(String bucketName, String prefix) throws IOException {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(0);
            metadata.setContentType("application/octet-stream");

            PutObjectRequest req = new PutObjectRequest(bucketName, prefix, new ByteArrayInputStream(new byte[0]), metadata);
            PutObjectResult resp = client.putObject(req);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public String urlOf(String bucketName, String objectName) throws IOException {
        try {
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, objectName);
            request.setMethod(HttpMethod.GET);
            return client.generatePresignedUrl(request).toString();
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public String urlOf(GeneratePresignedUrlRequest req) throws IOException {
        return client.generatePresignedUrl(req).toString();
    }

}
