package i2f.springboot.ai.mcp.client.official.v2024.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/10/15 15:57
 * @desc
 */
@Data
@NoArgsConstructor
public class McpServerItemProperties {
    protected Type type;

    // custom meta
    protected String name;
    protected String description;
    protected McpServerMetaProperties meta;

    // common fields
    protected long timeout;
    protected boolean disabled;

    // stdio type fields
    protected String command;
    protected List<String> args;
    protected Map<String, Object> env;
    protected String cwd;

    // http type fields
    protected String url;
    protected Map<String, Object> headers;

    // simple type fields
    // protected String url;
    protected String appId;
    protected String appKey;
    protected String hmacName = "HmacSHA256";

    public static enum Type {
        stdio,
        http,
        simple;
    }
}
