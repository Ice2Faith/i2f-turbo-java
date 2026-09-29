package i2f.springboot.ops.dashscope.tool;

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
import i2f.springboot.ops.dashscope.controller.DashScopeOpsController;
import i2f.springboot.ops.openai.async.AsyncTaskDispatcher;
import i2f.springboot.ops.openai.async.AsyncTaskHelper;
import i2f.springboot.ops.openai.async.AsyncTaskItem;
import i2f.springboot.ops.openai.async.AsyncTaskMessage;
import i2f.springboot.ops.openai.tool.impl.OpsMcpServerAdditionalToolProvider;
import i2f.springboot.ops.openai.tool.impl.TmpFileTools;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Ice2Faith
 * @date 2026/9/29 21:29
 * @desc
 */
@Conditional(DashScopeOpsController.DashScopeCondition.class)
@Data
@NoArgsConstructor
@Component
public class DashScopeOpsMcpServerAdditionalToolProvider implements McpServerAdditionalToolProvider {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AsyncTaskDispatcher asyncTaskDispatcher;

    @Autowired
    private AsyncTaskHelper asyncTaskHelper;

    @Autowired
    private DashScopeImageKlingText2ImageTools dashScopeImageKlingText2ImageTools;

    @Autowired
    private DashScopeImageViduText2ImageTools dashScopeImageViduText2ImageTools;

    @Autowired
    private DashScopeImageWanText2ImageTools dashScopeImageWanText2ImageTools;

    @Autowired
    private DashScopeVideoHappyHorseText2VideoTools dashScopeVideoHappyHorseText2VideoTools;

    @Autowired
    private DashScopeVideoKlingText2VideoTools dashScopeVideoKlingText2VideoTools;

    @Autowired
    private DashScopeVideoPixVerseText2VideoTools dashScopeVideoPixVerseText2VideoTools;

    @Autowired
    private DashScopeVideoViduText2VideoTools dashScopeVideoViduText2VideoTools;


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
                    AiTags.HIGH_COST_VALUE,
                    AiTags.SLOW_EXEC_VALUE,
                    AiTags.PUBLIC_NET_VALUE,
                    AiTags.WRITABLE_VALUE
            },
            description = "text to image, output png image file, Note: support chinese text content, don't describe the reference image, just give me reference url if has it."
    )
    public Map<String, Object> text_to_image_kling(
            @ToolParam(value = "content", description = "the content, description what is the image")
            String content,
            @ToolParam(value = "portrait_mode", description = "portrait mode image, default is false")
            boolean portrait_mode,
            @ToolParam(value = "reference_image_url", description = "the reference image url, cloud be null means not reference image, for example \"http://xxx/a.png\" or \"upload://xxx/1.jpg\"")
            String reference_image_url
    ) throws Exception {
        AsyncTaskMessage task = dashScopeImageKlingText2ImageTools.text_to_image_kling(content, portrait_mode, reference_image_url);

        List<String> urls = new ArrayList<>();
        List<AsyncTaskItem> list = task.getList();
        for (AsyncTaskItem item : list) {
            String url = asyncTaskHelper.signTask(item);
            urls.add(url);
        }


        Map<String, Object> ret = new HashMap<>();
        ret.put("taskUrls", urls);
        ret.put("hint", "this is an async task, need use tool `" + OpsMcpServerAdditionalToolProvider.QUERY_ASYNC_TASK_STATUS_BY_URL + "` to query task status");

        return ret;
    }

    @Tool(
            tags = {
                    AiTags.HIGH_COST_VALUE,
                    AiTags.SLOW_EXEC_VALUE,
                    AiTags.PUBLIC_NET_VALUE,
                    AiTags.WRITABLE_VALUE
            },
            description = "text to image, output png image file, Note: support chinese text content, don't describe the reference image, just give me reference url if has it."
    )
    public Map<String, Object> text_to_image_vidu(
            @ToolParam(value = "content", description = "the content, description what is the image")
            String content,
            @ToolParam(value = "portrait_mode", description = "portrait mode image, default is false")
            boolean portrait_mode,
            @ToolParam(value = "reference_image_url", description = "the reference image url, cloud be null means not reference image, for example \"http://xxx/a.png\" or \"upload://xxx/1.jpg\"")
            String reference_image_url
    ) throws Exception {
        AsyncTaskMessage task = dashScopeImageViduText2ImageTools.text_to_image_vidu(content, portrait_mode, reference_image_url);

        List<String> urls = new ArrayList<>();
        List<AsyncTaskItem> list = task.getList();
        for (AsyncTaskItem item : list) {
            String url = asyncTaskHelper.signTask(item);
            urls.add(url);
        }


        Map<String, Object> ret = new HashMap<>();
        ret.put("taskUrls", urls);
        ret.put("hint", "this is an async task, need use tool `" + OpsMcpServerAdditionalToolProvider.QUERY_ASYNC_TASK_STATUS_BY_URL + "` to query task status");

        return ret;
    }

    @Tool(
            tags = {
                    AiTags.HIGH_COST_VALUE,
                    AiTags.SLOW_EXEC_VALUE,
                    AiTags.PUBLIC_NET_VALUE,
                    AiTags.WRITABLE_VALUE
            },
            description = "text to image, output png image file, Note: support chinese text content, don't describe the reference image, just give me reference url if has it."
    )
    public Map<String, Object> text_to_image_wan(
            @ToolParam(value = "content", description = "the content, description what is the image")
            String content,
            @ToolParam(value = "portrait_mode", description = "portrait mode image, default is false")
            boolean portrait_mode,
            @ToolParam(value = "reference_image_url", description = "the reference image url, cloud be null means not reference image, for example \"http://xxx/a.png\" or \"upload://xxx/1.jpg\"")
            String reference_image_url
    ) throws Exception {
        TmpFileTools.FileAttachMessage task = dashScopeImageWanText2ImageTools.text_to_image_wan(content, portrait_mode, reference_image_url);


        Map<String, Object> ret = new HashMap<>();
        ret.put("files", task.getFiles());
        ret.put("hint", "this is an async task, need use tool `" + OpsMcpServerAdditionalToolProvider.QUERY_ASYNC_TASK_STATUS_BY_URL + "` to query task status");

        return ret;
    }

    @Tool(
            tags = {
                    AiTags.HIGH_COST_VALUE,
                    AiTags.SLOW_EXEC_VALUE,
                    AiTags.PUBLIC_NET_VALUE,
                    AiTags.WRITABLE_VALUE
            },
            description = "text to video, output mp4 video file, Note: support chinese text content, don't describe the reference image, just give me reference url if has it."
    )
    public Map<String, Object> text_to_video_happy_horse(
            @ToolParam(value = "content", description = "the content, description what is the image")
            String content,
            @ToolParam(value = "portrait_mode", description = "portrait mode video, default is false")
            boolean portrait_mode,
            @ToolParam(value = "reference_image_url", description = "the reference image url, cloud be null means not reference image, for example \"http://xxx/a.png\" or \"upload://xxx/1.jpg\"")
            String reference_image_url
    ) throws Exception {
        AsyncTaskMessage task = dashScopeVideoHappyHorseText2VideoTools.text_to_video_happy_horse(content, portrait_mode, reference_image_url);

        List<String> urls = new ArrayList<>();
        List<AsyncTaskItem> list = task.getList();
        for (AsyncTaskItem item : list) {
            String url = asyncTaskHelper.signTask(item);
            urls.add(url);
        }


        Map<String, Object> ret = new HashMap<>();
        ret.put("taskUrls", urls);
        ret.put("hint", "this is an async task, need use tool `" + OpsMcpServerAdditionalToolProvider.QUERY_ASYNC_TASK_STATUS_BY_URL + "` to query task status");

        return ret;
    }

    @Tool(
            tags = {
                    AiTags.HIGH_COST_VALUE,
                    AiTags.SLOW_EXEC_VALUE,
                    AiTags.PUBLIC_NET_VALUE,
                    AiTags.WRITABLE_VALUE
            },
            description = "text to video, output mp4 video file, Note: support chinese text content, don't describe the reference image, just give me reference url if has it."
    )
    public Map<String, Object> text_to_video_kling(
            @ToolParam(value = "content", description = "the content, description what is the image")
            String content,
            @ToolParam(value = "portrait_mode", description = "portrait mode video, default is false")
            boolean portrait_mode,
            @ToolParam(value = "reference_image_url", description = "the reference image url, cloud be null means not reference image, for example \"http://xxx/a.png\" or \"upload://xxx/1.jpg\"")
            String reference_image_url
    ) throws Exception {
        AsyncTaskMessage task = dashScopeVideoKlingText2VideoTools.text_to_video_kling(content, portrait_mode, reference_image_url);

        List<String> urls = new ArrayList<>();
        List<AsyncTaskItem> list = task.getList();
        for (AsyncTaskItem item : list) {
            String url = asyncTaskHelper.signTask(item);
            urls.add(url);
        }


        Map<String, Object> ret = new HashMap<>();
        ret.put("taskUrls", urls);
        ret.put("hint", "this is an async task, need use tool `" + OpsMcpServerAdditionalToolProvider.QUERY_ASYNC_TASK_STATUS_BY_URL + "` to query task status");

        return ret;
    }

    @Tool(
            tags = {
                    AiTags.HIGH_COST_VALUE,
                    AiTags.SLOW_EXEC_VALUE,
                    AiTags.PUBLIC_NET_VALUE,
                    AiTags.WRITABLE_VALUE
            },
            description = "text to video, output mp4 video file, Note: support chinese text content, don't describe the reference image, just give me reference url if has it."
    )
    public Map<String, Object> text_to_video_pixverse(
            @ToolParam(value = "content", description = "the content, description what is the image")
            String content,
            @ToolParam(value = "portrait_mode", description = "portrait mode video, default is false")
            boolean portrait_mode,
            @ToolParam(value = "reference_image_url", description = "the reference image url, cloud be null means not reference image, for example \"http://xxx/a.png\" or \"upload://xxx/1.jpg\"")
            String reference_image_url
    ) throws Exception {
        AsyncTaskMessage task = dashScopeVideoPixVerseText2VideoTools.text_to_video_pixverse(content, portrait_mode, reference_image_url);

        List<String> urls = new ArrayList<>();
        List<AsyncTaskItem> list = task.getList();
        for (AsyncTaskItem item : list) {
            String url = asyncTaskHelper.signTask(item);
            urls.add(url);
        }


        Map<String, Object> ret = new HashMap<>();
        ret.put("taskUrls", urls);
        ret.put("hint", "this is an async task, need use tool `" + OpsMcpServerAdditionalToolProvider.QUERY_ASYNC_TASK_STATUS_BY_URL + "` to query task status");

        return ret;
    }

    @Tool(
            tags = {
                    AiTags.HIGH_COST_VALUE,
                    AiTags.SLOW_EXEC_VALUE,
                    AiTags.PUBLIC_NET_VALUE,
                    AiTags.WRITABLE_VALUE
            },
            description = "text to video, output mp4 video file, Note: support chinese text content, don't describe the reference image, just give me reference url if has it."
    )
    public Map<String, Object> text_to_video_vidu(
            @ToolParam(value = "content", description = "the content, description what is the image")
            String content,
            @ToolParam(value = "portrait_mode", description = "portrait mode video, default is false")
            boolean portrait_mode,
            @ToolParam(value = "reference_image_url", description = "the reference image url, cloud be null means not reference image, for example \"http://xxx/a.png\" or \"upload://xxx/1.jpg\"")
            String reference_image_url
    ) throws Exception {
        AsyncTaskMessage task = dashScopeVideoViduText2VideoTools.text_to_video_vidu(content, portrait_mode, reference_image_url);

        List<String> urls = new ArrayList<>();
        List<AsyncTaskItem> list = task.getList();
        for (AsyncTaskItem item : list) {
            String url = asyncTaskHelper.signTask(item);
            urls.add(url);
        }


        Map<String, Object> ret = new HashMap<>();
        ret.put("taskUrls", urls);
        ret.put("hint", "this is an async task, need use tool `" + OpsMcpServerAdditionalToolProvider.QUERY_ASYNC_TASK_STATUS_BY_URL + "` to query task status");

        return ret;
    }
}
