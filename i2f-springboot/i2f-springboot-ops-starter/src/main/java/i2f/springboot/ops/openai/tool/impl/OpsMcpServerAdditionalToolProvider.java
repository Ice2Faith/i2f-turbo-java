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
import i2f.springboot.ops.openai.async.AsyncTaskHelper;
import i2f.springboot.ops.openai.async.AsyncTaskItem;
import i2f.springboot.ops.openai.data.OpenAiMeta;
import i2f.springboot.ops.openai.properties.OpenAiOpsProperties;
import i2f.springboot.ops.util.HumanUtil;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
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

    @Autowired
    private AsyncTaskHelper asyncTaskHelper;

    @Autowired
    private OpenAiOpsProperties openAiOpsProperties;

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

    public static final String UPLOAD_LOCAL_FILE = "upload_local_file";

    @Tool(value = UPLOAD_LOCAL_FILE,
            tags = {
                    AiTags.READONLY_VALUE
            },
            description = "upload local file and got `upload://xxx` url.\n" + LocalFileTools.PATH_PROMPT
    )
    public TmpFileTools.UploadTmpFileMetadata upload_local_file(@ToolParam(value = "localPath", description = "the file localPath where to upload, for example 'test.py' ")
                                                                String localPath) throws IOException {
        File srcFile = localFileTools.getFile(localPath);
        if (!srcFile.exists()) {
            throw new IllegalStateException("file not found: " + srcFile.getAbsolutePath());
        }
        if (!srcFile.isFile()) {
            throw new IllegalStateException("not file type, cannot read to upload: " + srcFile.getAbsolutePath());
        }
        TmpFileTools.UploadTmpFileMetadata metadata = tmpFileTools.saveFile(new FileInputStream(srcFile), srcFile.getName());
        return metadata;
    }

    public static final String STORE_URL_FILE_TO_LOCAL = "store_url_file_to_local";

    @Tool(value = STORE_URL_FILE_TO_LOCAL,
            tags = {
                    AiTags.WRITABLE_VALUE
            },
            description = "store upload file url to local file.\n" + LocalFileTools.PATH_PROMPT
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

    public static final String QUERY_ASYNC_TASK_STATUS_BY_URL = "query_async_task_status_by_url";

    @Tool(value = QUERY_ASYNC_TASK_STATUS_BY_URL,
            tags = {
                    AiTags.READONLY_VALUE
            },
            description = "query async task status by task url, url like `" + AsyncTaskHelper.PROTOCOL + "://xxx?xxx`"
    )
    public Map<String, Object> query_async_task_status_by_url(@ToolParam(value = "taskUrl", description = "the taskUrl, for example 'task://xxxxx?xxx' ")
                                                              String taskUrl,
                                                              @ToolParam(value = "awaitSeconds", description = "the awaitSeconds,cloud be null means right now returns, for example 20 ")
                                                              Long awaitSeconds) throws IOException {
        if (awaitSeconds == null) {
            awaitSeconds = 0L;
        }
        if (awaitSeconds < 0) {
            awaitSeconds = 0L;
        }
        if (awaitSeconds > 3 * 60) {
            awaitSeconds = 3 * 60L;
        }
        long bts = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(1);

        AsyncTaskItem taskItem = asyncTaskHelper.verifyTask(taskUrl);

        OpenAiMeta meta = getOpenAiMeta();
        if (meta == null) {
            throw new IllegalStateException("not dashscope openai endpoint config!");
        }

        AtomicReference<AsyncTaskItem> holder = new AtomicReference<>();
        holder.set(taskItem);

        AsyncTaskItem queryItem = asyncTaskDispatcher.query(taskItem, meta);
        holder.set(queryItem);

        if (!queryItem.isFinished()) {
            latch.countDown();
        } else {
            long maxTestTs = (awaitSeconds + 5) * 1000;
            Thread thread = new Thread(() -> {
                try {
                    double sleepTs = 300;
                    long diffTs = System.currentTimeMillis() - bts;
                    while (diffTs < maxTestTs) {

                        AsyncTaskItem next = asyncTaskDispatcher.query(taskItem, meta);
                        holder.set(next);
                        if (next.isFinished()) {
                            break;
                        }
                        Thread.sleep((long) sleepTs);
                        sleepTs = sleepTs * 1.2;
                        sleepTs = Math.min(10 * 1000, sleepTs);
                        diffTs = System.currentTimeMillis() - bts;
                    }

                } catch (Exception e) {
                    latch.countDown();
                }
            });
            thread.setDaemon(true);
            thread.start();
        }


        if (awaitSeconds > 0) {
            try {
                latch.await(awaitSeconds, TimeUnit.SECONDS);
            } catch (Exception e) {
                // ignore
            }
        }
        long ets = System.currentTimeMillis();
        Map<String, Object> ret = new HashMap<>();
        ret.put("awaitSeconds", awaitSeconds);
        ret.put("realAwaitSeconds", (ets - bts) / 1000);

        AsyncTaskItem result = holder.get();
        ret.put("result", result);
        ret.put("finished", result.isFinished());
        ret.put("hint", "retry later, if task not finished.");

        return ret;
    }

    public OpenAiMeta getOpenAiMeta() {
        OpenAiOpsProperties.OpenAiOptions endpoint = openAiOpsProperties.getDefaultEndpoint();
        if (endpoint != null && endpoint.isEnable()) {
            OpenAiMeta ret = new OpenAiMeta();
            ret.setBaseUrl(endpoint.getBaseUrl());
            ret.setApiKey(endpoint.getApiKey());
            return ret;
        }
        endpoint = openAiOpsProperties.getDashscopeEndpoint();
        if (endpoint != null && endpoint.isEnable()) {
            OpenAiMeta ret = new OpenAiMeta();
            ret.setBaseUrl(endpoint.getBaseUrl());
            ret.setApiKey(endpoint.getApiKey());
            return ret;
        }
        return null;
    }
}
