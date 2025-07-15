package i2f.extension.oss.aws.s3;

import lombok.Data;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProviderChain;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ice2Faith
 * @date 2022/7/8 9:09
 * @desc
 */
@Data
public class AwsS3OssUtil {
    protected S3Client client;
    protected S3Presigner presigner;

    public AwsS3OssUtil(S3Client client, S3Presigner presigner) {
        this.client = client;
        this.presigner = presigner;
    }

    public AwsS3OssUtil(AwsS3OssMeta meta) {
        this.client = getClient(meta);
        this.presigner = getS3Presigner(meta);
    }

    public static S3Client getClient(AwsS3OssMeta meta) {
        AwsCredentialsProviderChain chain = getCredentialsProviderChain(meta);

        S3ClientBuilder builder = S3Client.builder()
                .credentialsProvider(chain)
                .region(Region.of(meta.getRegion()));
        if (meta.getUrl() != null && !meta.getUrl().isEmpty()) {
            builder.endpointOverride(URI.create(meta.getUrl()));
        }
        return builder.build();
    }

    public static AwsCredentialsProviderChain getCredentialsProviderChain(AwsS3OssMeta meta) {
        return AwsCredentialsProviderChain
                .builder()
                .addCredentialsProvider(new AwsCredentialsProvider() {
                    @Override
                    public AwsCredentials resolveCredentials() {
                        return AwsBasicCredentials.create(meta.getAccessKeyId(), meta.getSecretAccessKey());
                    }
                })
                .build();
    }

    public boolean bucketExists(String bucketName) throws IOException {
        try {
            GetBucketPolicyStatusResponse resp = client.getBucketPolicyStatus(e -> {
                e.bucket(bucketName);
            });
            if (resp.sdkHttpResponse().isSuccessful()) {
                PolicyStatus policyStatus = resp.policyStatus();
                if (policyStatus != null) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public void bucketCreate(String bucketName) throws IOException {
        if (bucketExists(bucketName)) {
            return;
        }
        try {
            CreateBucketResponse resp = client.createBucket(e -> {
                e.bucket(bucketName)
                        .createBucketConfiguration(
                                CreateBucketConfiguration.builder()
                                        .build()
                        );
            });
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public void bucketRemove(String bucketName) throws IOException {
        try {
            DeleteBucketResponse resp = client.deleteBucket(e -> {
                e.bucket(bucketName);
            });
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }


    public GetObjectResponse getObjectInfo(String bucketName, String objectName) throws IOException {
        try {
            ResponseInputStream<GetObjectResponse> resp = client.getObject(e -> {
                e.bucket(bucketName)
                        .key(objectName);
            });
            return resp.response();
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public PutObjectResponse upload(String bucketName, String objectName, InputStream is, Long fileSize, String mimeType) throws IOException {
        try {
            PutObjectResponse resp = client.putObject(e -> {
                e.bucket(bucketName)
                        .key(objectName)
                        .contentType(mimeType);
            }, RequestBody.fromInputStream(is, fileSize == null ? -1 : fileSize));
            return resp;
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public PutObjectResponse upload(String bucketName, String objectName, InputStream is, String contentType) throws IOException {
        return upload(bucketName, objectName, is, -1L, contentType);
    }

    public PutObjectResponse upload(String bucketName, String objectName, File file, String contentType) throws IOException {
        try {
            PutObjectResponse resp = client.putObject(e -> {
                e.bucket(bucketName)
                        .key(objectName)
                        .contentType(contentType);
            }, RequestBody.fromFile(file));
            return resp;
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public InputStream download(String bucketName, String objectName) throws IOException {
        try {
            ResponseInputStream<GetObjectResponse> resp = client.getObject(e -> {
                e.bucket(bucketName)
                        .key(objectName);
            });
            return resp;
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public List<S3Object> list(String bucketName) {
        List<S3Object> ret = new ArrayList<>();
        ListObjectsResponse resp = null;
        AtomicReference<String> nextMarker = new AtomicReference<>(null);
        do {
            resp = client.listObjects(e -> {
                e.bucket(bucketName)
                        .marker(nextMarker.get());
            });
            if (resp == null) {
                break;
            }
            List<S3Object> contents = resp.contents();
            ret.addAll(contents);
            nextMarker.set(resp.nextMarker());
        } while (resp != null && resp.isTruncated());

        return ret;
    }

    public List<S3Object> list(String bucketName, String prefix) {
        List<S3Object> ret = new ArrayList<>();
        ListObjectsResponse resp = null;
        AtomicReference<String> nextMarker = new AtomicReference<>(null);
        do {
            resp = client.listObjects(e -> {
                e.bucket(bucketName)
                        .prefix(prefix)
                        .marker(nextMarker.get());
            });
            if (resp == null) {
                break;
            }
            List<S3Object> contents = resp.contents();
            ret.addAll(contents);
            nextMarker.set(resp.nextMarker());
        } while (resp != null && resp.isTruncated());

        return ret;
    }

    public boolean prefixExists(String bucketName, String prefix) throws IOException {
        try {
            ListObjectsResponse resp = client.listObjects(e -> {
                e.bucket(bucketName)
                        .prefix(prefix);
            });
            return resp != null && resp.hasContents();
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public void prefixCreate(String bucketName, String prefix) throws IOException {
        try {
            PutObjectResponse resp = client.putObject(e -> {
                e.bucket(bucketName)
                        .key(prefix)
                        .contentType("application/octet-stream");
            }, RequestBody.fromBytes(new byte[0]));
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public static S3Presigner getS3Presigner(AwsS3OssMeta meta) {
        return S3Presigner.builder()
                .credentialsProvider(getCredentialsProviderChain(meta))
                .region(Region.of(meta.getRegion()))
                .build();
    }


    public String urlOf(String bucketName, String objectName) throws IOException {
        try {
            PresignedGetObjectRequest req = presigner
                    .presignGetObject(e -> {
                        e.signatureDuration(Duration.of(999, ChronoUnit.YEARS))
                                .getObjectRequest(r -> {
                                    r.bucket(bucketName)
                                            .key(objectName);
                                });
                    });
            return req.url().toString();
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public String urlOf(GetObjectPresignRequest req) throws IOException {
        return presigner.presignGetObject(req).url().toString();
    }

}
