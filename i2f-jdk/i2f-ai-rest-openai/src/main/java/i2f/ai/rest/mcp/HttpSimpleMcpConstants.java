package i2f.ai.rest.mcp;

/**
 * @author Ice2Faith
 * @date 2026/7/13 11:17
 * @desc
 */
public interface HttpSimpleMcpConstants {
    String HEADER_APP_ID = "X-App-Id";
    String HEADER_APP_DATE = "X-App-Date";
    String HEADER_APP_NONCE = "X-App-Nonce";
    String HEADER_APP_SIGN = "X-App-Sign";
    String DEFAULT_HMAC_NAME = "HmacSHA256";

    String URL_PATH_GET_TOOLS = "/mcp/tool/list";
    String URL_PATH_CALL_TOOL = "/mcp/tool/call";
}
