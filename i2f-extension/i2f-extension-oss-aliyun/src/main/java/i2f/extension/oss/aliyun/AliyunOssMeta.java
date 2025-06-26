package i2f.extension.oss.aliyun;

import com.aliyun.oss.common.comm.SignVersion;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/6/25 20:11
 * @desc
 */
@Data
@NoArgsConstructor
public class AliyunOssMeta {
    public static final SignVersion DEFAULT_SIGN_VERSION = SignVersion.V4;

    // endpoint , such: https://oss-cn-hangzhou.aliyuncs.com
    protected String url;
    // such: cn-hangzhou
    protected String region;

    protected String accessKeyId;
    protected String accessKeySecret;
    // 见 com.aliyun.oss.common.comm.SignVersion
    protected String signVersion;

    public SignVersion getSignVersion() {
        if (signVersion == null || "".equals(signVersion)) {
            return DEFAULT_SIGN_VERSION;
        }
        try {
            return SignVersion.valueOf(signVersion);
        } catch (IllegalArgumentException e) {

        }
        return DEFAULT_SIGN_VERSION;
    }

    public void setSignVersion(SignVersion signVersion) {
        this.signVersion = signVersion.name();
    }
}
