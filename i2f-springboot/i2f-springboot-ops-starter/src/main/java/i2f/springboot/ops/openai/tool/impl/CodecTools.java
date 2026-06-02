package i2f.springboot.ops.openai.tool.impl;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author Ice2Faith
 * @date 2026/6/2 14:12
 * @desc
 */
@Component
@Tools
public class CodecTools {

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "encode text as base64"
    )
    public String encode_base64(@ToolParam(description = "the text") String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "decode base64 as text"
    )
    public String decode_base64(@ToolParam(description = "the base64") String base64) {
        return new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);
    }

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "encode text as urlencoded"
    )
    public String encode_url(@ToolParam(description = "the text") String text) throws Exception {
        return URLEncoder.encode(text, StandardCharsets.UTF_8.name());
    }

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "decode urlencoded as text"
    )
    public String decode_url(@ToolParam(description = "the urlencoded") String urlencoded) throws Exception {
        return URLDecoder.decode(urlencoded, StandardCharsets.UTF_8.name());
    }

}
