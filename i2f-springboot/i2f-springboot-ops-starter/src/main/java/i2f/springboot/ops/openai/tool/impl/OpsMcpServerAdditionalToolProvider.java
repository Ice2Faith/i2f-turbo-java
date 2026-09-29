package i2f.springboot.ops.openai.tool.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.ai.std.mcp.server.manager.McpServerAdditionalToolProvider;
import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.ToolBaseCallRequest;
import i2f.ai.std.tool.ToolManager;
import i2f.ai.std.tool.ToolRawDefinition;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.definition.ToolDefinition;
import i2f.ai.std.tool.impl.ListableAppToolManager;
import i2f.extension.jackson.serializer.JacksonJsonSerializer;
import i2f.io.file.FileUtil;
import i2f.springboot.ops.openai.async.AsyncTaskDispatcher;
import i2f.springboot.ops.util.HumanUtil;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Ice2Faith
 * @date 2026/9/29 14:39
 * @desc
 */
@Data
@NoArgsConstructor
@Component
public class OpsMcpServerAdditionalToolProvider implements McpServerAdditionalToolProvider {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired(required = false)
    private TmpFileTools tmpFileTools;

    @Autowired(required = false)
    private LocalFileTools localFileTools;

    @Autowired
    private AsyncTaskDispatcher asyncTaskDispatcher;

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

    @Tool(
            tags = {
                    AiTags.WRITABLE_VALUE
            },
            description = "store upload file url to local file.\n"+  LocalFileTools.PATH_PROMPT
    )
    public Map<String, Object> store_url_file_to_local(@ToolParam(value = "fileUrl", description = "the fileUrl, for example 'upload://xxxxx/data.py' ")
                                                       String fileUrl,
                                                       @ToolParam(value = "localPath", description = "the localPath where to store, for example 'test.py' ")
                                                       String localPath) throws IOException {
        if (tmpFileTools == null) {
            throw new IllegalStateException("not enable tmp-file config!");
        }
        File srcFile = tmpFileTools.getFileByUrl(fileUrl);
        if (!srcFile.exists()) {
            throw new IllegalStateException("source file not exists, maybe expired!");
        }
        File dstFile = localFileTools.getFile(localPath);
        dstFile = new File(dstFile.getAbsolutePath());
        FileUtil.copy(dstFile, srcFile);

        Map<String, Object> ret = new HashMap<>();
        ret.put("savePath", dstFile.getAbsolutePath());
        ret.put("saveSuccess", false);
        if (dstFile.exists()) {
            ret.put("saveSuccess", true);
            ret.put("saveSizeInBytes", dstFile.length());
            ret.put("saveSizeInHuman", HumanUtil.humanFileSize(dstFile.length()));
        }
        return ret;
    }


}
