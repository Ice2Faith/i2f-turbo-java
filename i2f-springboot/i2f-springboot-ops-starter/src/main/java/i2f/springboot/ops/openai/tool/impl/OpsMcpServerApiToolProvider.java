package i2f.springboot.ops.openai.tool.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.ai.std.mcp.server.manager.McpServerAdditionalToolProvider;
import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.*;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.definition.ToolDefinition;
import i2f.ai.std.tool.impl.ListableAppToolManager;
import i2f.extension.jackson.serializer.JacksonJsonSerializer;
import i2f.lru.LruMap;
import i2f.net.NetworkUtil;
import i2f.net.http.data.HttpHeaders;
import i2f.web.servlet.ServletFileUtil;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Ice2Faith
 * @date 2026/10/13 8:44
 * @desc
 */
@ConditionalOnExpression("${ai.mcp-server.api.enable:true}")
@RestController
@RequestMapping(OpsMcpServerApiToolProvider.API_ROOT_PATH)
@Data
@NoArgsConstructor
@Component
public class OpsMcpServerApiToolProvider  implements McpServerAdditionalToolProvider {
    public static final String API_ROOT_PATH = "/mcp/api";
    public static final String API_TOKEN_HEADER = "X-MCP-TOKEN";

    @Autowired
    private Environment environment;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired(required = false)
    private TmpFileTools tmpFileTools;

    @Autowired(required = false)
    private LocalFileTools localFileTools;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private volatile ToolManager holder;
    private final ReentrantLock lock = new ReentrantLock();

    public ToolManager getHolderManager() {
        if (holder != null) {
            return holder;
        }
        lock.lock();
        try {
            if (holder != null) {
                return holder;
            }
            ListableAppToolManager ret = new ListableAppToolManager();
            ret.setJsonSerializer(new JacksonJsonSerializer(objectMapper));
            ret.setInvocationHandler(null);
            Map<String, ToolRawDefinition> map = ToolRawHelper.parseTools(null, this);
            ret.getTools().addAll(map.values());
            for (ToolRawDefinition tool : ret.getTools()) {
                tool.setName("ops." + tool.getName());
                tool.getJsonSchema().setName(tool.getName());
            }

            holder = ret;
            return holder;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<ToolDefinition> listTools() {
        return getHolderManager().listTools();
    }

    @Override
    public boolean support(ToolBaseCallRequest request) {
        return getHolderManager().support(request);
    }

    @Override
    public Object callTool(ToolBaseCallRequest request) throws Throwable {
        return getHolderManager().callTool(request);
    }

    public static final String GET_SERVER_GUIDE = "get_server_guide";

    @Tool(value = GET_SERVER_GUIDE,
            tags = {
                    AiTags.READONLY_VALUE
            },
            description = "get the guide of this mcp server, include file upload/download etc."
    )
    public Map<String, Object> get_server_guide() throws IOException {
        Map<String, Object> ret = new HashMap<>();
        /*language=markdown*/
        String guide = "# MCP 服务器使用向导\n" +
                "\n" +
                "## API 端点\n" +
                "\n" +
                "- 开放 API 端点\n" +
                "- baseURL\n" +
                "\n" +
                "```\n" +
                "{{VAR_BASE_URL}}\n" +
                "```\n" +
                "\n" +
                "- 这个 baseURL 可能对你不可达，请进行有效性验证\n" +
                "- 或进行 baseURL 确认探测\n" +
                "- 使用 API 前，请先进行连通性校验\n" +
                "- 调用具体 API 之前，需要先获取接入令牌\n" +
                "\n" +
                "### 端点有效性验证 API（连通性校验，ping）\n" +
                "\n" +
                "- 请求改地址，响应码为 200 即为有效\n" +
                "- 则可以使用其他 API\n" +
                "- 否则，请确认 baseURL 正确\n" +
                "\n" +
                "```http request\n" +
                "GET {{VAR_BASE_URL}}{{VAR_PING_PATH}}\n" +
                "```\n" +
                "\n" +
                "### baseURL 确认探测策略\n" +
                "\n" +
                "- 按以下顺序进行探测确认\n" +
                "    - 本文档返回的接入点\n" +
                "    - 读取客户端 MCP 服务配置\n" +
                "    - netstat 查客户端进程的连接表\n" +
                "    - 调 net_info 枚举网卡后逐候选探测\n" +
                "    - 询问用户：'你配置这个 MCP 时用的 URL 的 host:port 是什么?'\n" +
                "- 注意，得到的结构仅保留：`{protocol}://{host}:{port}/`\n" +
                "- 然后将后续的 API 路径拼接即为 API 端点正确接入点\n" +
                "\n" +
                "### API 接入令牌获取\n" +
                "\n" +
                "- 使用工具 `{{VAR_TOKEN_TOOL}}` 获取调用令牌\n" +
                "\n" +
                "### 文件上传 API\n" +
                "\n" +
                "- 使用 `multipart/form-data` 上传，文件字段名为 `file`\n" +
                "\n" +
                "```http request\n" +
                "POST {{VAR_BASE_URL}}{{VAR_FILE_UPLOAD_PATH}}\n" +
                "Content-Type: multipart/form-data; charset=utf-8\n" +
                "{{API_TOKEN_HEADER}}: {获取到的接入令牌}\n" +
                "```\n" +
                "\n" +
                "### 文件下载 API\n" +
                "\n" +
                "- 直接通过令牌调用下载\n" +
                "- 支持通过 `upload://xxx` 形式URL下载，或者直接通过工作区路径下载\n" +
                "- 请求参数多选一互斥\n" +
                "\n" +
                "```http request\n" +
                "POST {{VAR_BASE_URL}}{{VAR_FILE_DOWNLOAD_PATH}}\n" +
                "Content-Type: application/json; charset=utf-8\n" +
                "{{API_TOKEN_HEADER}}: {获取到的接入令牌}\n" +
                "\n" +
                "{\n" +
                " \"url\": \"{接受 `upload://xxx` 形式URL}\",\n" +
                " \"path\": \"{接受工作区路径}\"\n" +
                "}\n" +
                "```\n" +
                "\n" +
                "\n";

        String domain = NetworkUtil.getPreferredIp() + ":" + environment.getProperty("server.port", "8080");
        String contextPath = environment.getProperty("server.servlet.context-path", "");


        HttpHeaders httpHeaders = ToolCallContextHolder.get("http_headers");
        if (httpHeaders != null) {
            String host = httpHeaders.getFirstHeader("host");
            if (host != null && !host.isEmpty()) {
                domain = host;
                if(NetworkUtil.isLocalHost(host)){
                    /*language=markdown*/
                    guide+="## 本机 MCP 服务\n" +
                            "\n" +
                            "- 当前 MCP 服务与你是同一台机器\n" +
                            "- 因此，可以不使用上传下载 API 端点进行文件交互";
                }else{
                    /*language=markdown*/
                    guide+="## 远程 MCP 服务\n" +
                            "\n" +
                            "- 当前 MCP 服务与你不是同一台机器\n" +
                            "- 因此，如果要进行文件交互，必须通过 API 端点进行交互";
                }
            }
        }

        if (contextPath.endsWith("/")) {
            contextPath = contextPath.substring(0, contextPath.length() - 1);
        }

        String baseURL = "http://" + domain + contextPath;
        guide = guide.replace("{{VAR_BASE_URL}}", baseURL);
        guide = guide.replace("{{VAR_PING_PATH}}", API_ROOT_PATH + PING_PATH);
        guide = guide.replace("{{VAR_TOKEN_TOOL}}", MCP_API_TOKEN_TOLL);
        guide = guide.replace("{{API_TOKEN_HEADER}}", API_TOKEN_HEADER);
        guide = guide.replace("{{VAR_FILE_UPLOAD_PATH}}", API_ROOT_PATH + FILE_UPLOAD_PATH);
        guide = guide.replace("{{VAR_FILE_DOWNLOAD_PATH}}", API_ROOT_PATH + FILE_DOWNLOAD_PATH);
        ret.put("guide", guide);
        return ret;
    }

    private LruMap<String, Long> apiTokenCache = new LruMap<>(4096);
    public static final long tokenExpireTsDuration = TimeUnit.MINUTES.toMillis(30);

    public static final String MCP_API_TOKEN_TOLL = "mcp_api_token";

    @Tool(value = MCP_API_TOKEN_TOLL,
            tags = {
                    AiTags.READONLY_VALUE
            },
            description = "get the token of mcp api."
    )
    public Map<String, Object> mcp_api_token() throws IOException {
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String token = "msk-" + uuid;
        apiTokenCache.put(token, System.currentTimeMillis() + tokenExpireTsDuration);
        Map<String, Object> ret = new HashMap<>();
        ret.put("token", token);
        ret.put("expires", "30min if not use any api");
        return ret;
    }

    public static final String PING_PATH = "/ping";

    @GetMapping(PING_PATH)
    public Map<String, Object> ping() {
        Map<String, Object> ret = new HashMap<>();
        ret.put("code", 200);
        ret.put("message", "success");
        ret.put("data", null);
        return ret;
    }

    public boolean verifyToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        Long ts = apiTokenCache.get(token);
        if (ts == null || ts < System.currentTimeMillis()) {
            return false;
        }
        apiTokenCache.put(token, System.currentTimeMillis() + tokenExpireTsDuration);
        return true;
    }

    public static final String FILE_UPLOAD_PATH = "/file/upload";

    @PostMapping(FILE_UPLOAD_PATH)
    public Map<String, Object> fileUpload(@RequestParam("file") MultipartFile file,
                                          HttpServletRequest request) throws Exception {
        String token = request.getHeader(API_TOKEN_HEADER);
        Map<String, Object> ret = new HashMap<>();
        ret.put("code", 200);
        ret.put("message", "success");
        ret.put("data", null);
        if (!verifyToken(token)) {
            ret.put("code", 401);
            ret.put("message", "missing api token `" + API_TOKEN_HEADER + "`, or token expired, please use tool `" + MCP_API_TOKEN_TOLL + "` to got new");
            return ret;
        }

        TmpFileTools.UploadTmpFileMetadata metadata = tmpFileTools.saveFile(file.getInputStream(), file.getOriginalFilename());
        ret.put("data", metadata);

        return ret;
    }

    @Data
    @NoArgsConstructor
    public static class FileDownloadDto {
        protected String url;
        protected String path;
    }

    public static final String FILE_DOWNLOAD_PATH = "/file/download";

    @PostMapping(FILE_DOWNLOAD_PATH)
    public void fileDownload(@RequestBody FileDownloadDto dto,
                             HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        String token = request.getHeader(API_TOKEN_HEADER);
        if (!verifyToken(token)) {
            response.setStatus(401);
            response.setContentType("text/html");
            response.getWriter().write("missing api token `" + API_TOKEN_HEADER + "`, or token expired, please use tool `" + MCP_API_TOKEN_TOLL + "` to got new");
            return;
        }


        String url = dto.getUrl();
        if (url != null && !url.isEmpty()) {
            try {
                File file = tmpFileTools.getFileByUrl(url);
                ServletFileUtil.downloadFileRangeSupport(request, response, file);
                return;
            } catch (Exception e) {
                response.setStatus(404);
                response.setContentType("text/html");
                response.getWriter().write("download file by url, file not exists or current policy not allow access this file.");
            }
        }

        String path = dto.getPath();
        if (path != null && !path.isEmpty()) {
            try {
                File file = localFileTools.getFile(path);
                ServletFileUtil.downloadFileRangeSupport(request, response, file);
                return;
            } catch (Exception e) {
                response.setStatus(404);
                response.setContentType("text/html");
                response.getWriter().write("download file by url, file not exists or current policy not allow access this file.");
            }
        }

        response.setStatus(400);
        response.setContentType("text/html");
        response.getWriter().write("bad request, missing `url` or `path` request field.");
    }
}
