package i2f.swl.cert.impl;

import i2f.io.file.FileUtil;
import i2f.io.stream.StreamUtil;
import i2f.lru.LruMap;
import i2f.std.consts.StdConst;
import i2f.swl.cert.SwlCertManager;
import i2f.swl.cert.SwlCertUtil;
import i2f.swl.cert.data.SwlCert;
import i2f.swl.consts.SwlCode;
import i2f.swl.exception.SwlException;
import lombok.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Ice2Faith
 * @date 2024/8/8 15:39
 * @desc
 */
@Data
@NoArgsConstructor
public class SwlResourceCertManager implements SwlCertManager {
    public static final String SUFFIX = ".cert";
    public static final String SERVER_SUFFIX = ".server" + SUFFIX;
    public static final String CLIENT_SUFFIX = ".client" + SUFFIX;
    public static final String DEFAULT_PATH = "swl/cert";
    private String classpathBasePath = DEFAULT_PATH;
    private File localFilePath = new File("./" + StdConst.RUNTIME_PERSIST_DIR + "/" + DEFAULT_PATH);

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private transient LruMap<String, SwlCert> cacheCert = new LruMap<>(1024);

    public SwlResourceCertManager(String classpathBasePath, File localFilePath) {
        this.classpathBasePath = classpathBasePath;
        this.localFilePath = localFilePath;
    }

    public String getCertFileName(String certId, boolean server) {
        if (server) {
            return certId + SERVER_SUFFIX;
        }
        return certId + CLIENT_SUFFIX;
    }

    public File getCertFile(String fileName) {
        File baseDir = new File("./" + StdConst.RUNTIME_PERSIST_DIR + "/" + DEFAULT_PATH);
        if (localFilePath != null) {
            baseDir = localFilePath;
        }
        File file = new File(baseDir, fileName);
        return file;
    }

    public String getCertClasspath(String fileName) {
        String basePath = classpathBasePath;
        if (basePath == null) {
            basePath = DEFAULT_PATH;
        }
        if ("/".equals(basePath)) {
            basePath = "";
        }
        if (!basePath.isEmpty() && !basePath.endsWith("/")) {
            basePath = basePath + "/";
        }
        return basePath + fileName;
    }

    @Override
    public SwlCert load(String certId, boolean server) {
        String fileName = getCertFileName(certId, server);
        SwlCert cert = cacheCert.get(fileName);
        if (cert != null) {
            return cert.copy();
        }
        InputStream is = null;
        try {
            boolean hasFile = false;
            File file = getCertFile(fileName);
            if (file.exists()) {
                is = new FileInputStream(file);
                hasFile = true;
            }

            if (is == null) {
                is = Thread.currentThread().getContextClassLoader().getResourceAsStream(getCertClasspath(fileName));
            }
            if (is == null) {
                return null;
            }
            String str = StreamUtil.readString(is, "UTF-8", true);
            if (!hasFile) {
                FileUtil.useParentDir(file);
                FileUtil.save(str, "UTF-8", file);
            }
            SwlCert ret = SwlCertUtil.deserialize(str);
            if (ret != null) {
                cacheCert.put(fileName, ret.copy());
            }
            return ret;
        } catch (IOException e) {

        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {

                }
            }
        }
        return null;
    }

    @Override
    public void store(SwlCert cert, boolean server) {
        if (cert == null) {
            return;
        }
        String certId = cert.getCertId();
        String fileName = getCertFileName(certId, server);
        cacheCert.put(fileName, cert.copy());
        File file = getCertFile(fileName);
        try {
            FileUtil.useParentDir(file);
            String str = SwlCertUtil.serialize(cert);
            FileUtil.save(str, "UTF-8", file);
        } catch (IOException e) {
            throw new SwlException(SwlCode.INTERNAL_EXCEPTION.code(), e.getMessage(), e);
        }
    }
}
