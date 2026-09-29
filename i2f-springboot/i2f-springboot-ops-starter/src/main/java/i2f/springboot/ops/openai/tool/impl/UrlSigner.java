package i2f.springboot.ops.openai.tool.impl;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Ice2Faith
 * @date 2026/9/29 20:41
 * @desc
 */
@Data
@NoArgsConstructor
public class UrlSigner {
    public static final String DEFAULT_URL_SIGN_SALT = "abc123def456";

    @Value("${ai.tools.url-signer.sign-salt:}")
    protected String signSalt = DEFAULT_URL_SIGN_SALT;

    public String signContent(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String payload = signSalt + "@" + content + "#" + signSalt;
            byte[] data = digest.digest(payload.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            for (byte b : data) {
                builder.append(String.format("%02x", (int) (b & 0x0ff)));
            }
            String sign = builder.toString();
            sign = sign.substring(0, 5) + sign.substring(sign.length() - 5);
            return sign;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public String signedUrl(String url) {
        String sign = signContent(url);
        int idx = url.indexOf("://");
        return url.substring(0, idx) + "://" + sign + "@" + url.substring(idx + "://".length());
    }

    public String verifyUrl(String url) {
        int idx = url.indexOf("://");
        String protocol = url.substring(0, idx);
        String path = url.substring(idx + "://".length());
        idx = path.indexOf("@");
        String sign = path.substring(0, idx);
        path = path.substring(idx + 1);
        String originUrl = protocol + "://" + path;
        String reSign = signContent(originUrl);
        if (!reSign.equalsIgnoreCase(sign)) {
            throw new IllegalArgumentException("illegal url, verify url signature failure!");
        }
        return originUrl;
    }
}
