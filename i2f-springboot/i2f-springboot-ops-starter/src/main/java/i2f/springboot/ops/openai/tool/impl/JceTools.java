package i2f.springboot.ops.openai.tool.impl;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @author Ice2Faith
 * @date 2026/6/2 14:13
 * @desc
 */
@Component
@Tools
public class JceTools {

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "message digest on jce platform"
    )
    public String message_digest_jce(@ToolParam(value = "text", description = "the text")
                                     String text,
                                     @ToolParam(value = "algorithm", description = "the algorithm for jce, for example: MD5 or SHA-256")
                                     String algorithm,
                                     @ToolParam(value = "provider", description = "the provider for jce, cloud be null, or other provider name")
                                     String provider) throws Throwable {
        MessageDigest digest = null;
        if (provider == null || provider.isEmpty()) {
            digest = MessageDigest.getInstance(algorithm);
        } else {
            digest = MessageDigest.getInstance(algorithm, provider);
        }
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        byte[] ret = digest.digest(bytes);
        StringBuilder builder = new StringBuilder();
        for (byte bt : ret) {
            builder.append(String.format("%02x", (int) (bt & 0x0ff)));
        }
        return builder.toString();
    }
}
