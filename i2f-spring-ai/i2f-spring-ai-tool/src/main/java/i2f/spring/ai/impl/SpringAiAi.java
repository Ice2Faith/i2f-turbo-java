package i2f.spring.ai.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.ai.std.tool.ToolRawDefinition;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.spring.ai.tool.SpringAiToolDefinition;
import i2f.spring.ai.tool.SpringAiToolHelper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.metadata.ChatGenerationMetadata;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ice2Faith
 * @date 2026/3/19 8:53
 * @desc
 */
@Data
@NoArgsConstructor
public class SpringAiAi {
    public static final String API_KEY_NAME = "OPENAI_AI_API_KEY";
    public static final String BASE_URL_NAME = "OPENAI_AI_BASE_URL";
    public static final String DASHSCOPE_BASE_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1";

    public static final String DEFAULT_MODEL = "qwen-plus";
    public static final String DEFAULT_RESP_JSON_ADDITIONAL_MESSAGE = "请严格按照JSON格式输出且不需要任何多余的解释或者非JSON格式的信息。";

    private static ObjectMapper objectMapper = new ObjectMapper();

    protected String baseUrl;
    protected String apiKey;
    protected String system;
    protected String question;
    protected String model = DEFAULT_MODEL;
    protected String respJsonAdditionalUserMessage = DEFAULT_RESP_JSON_ADDITIONAL_MESSAGE;
    protected Map<String, SpringAiToolDefinition> toolDefinitionMap = new LinkedHashMap<>();
    protected Map<String, AtomicInteger> toolCallCounter = new ConcurrentHashMap<>();

    protected LinkedList<Message> historyMessageList = new LinkedList<>();

    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public static <T> T fromJson(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public static <T> T fromJson(String json, TypeReference<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    protected OpenAiChatModel getModel() {
        OpenAiApi.Builder apiBuilder = OpenAiApi.builder();
        if (baseUrl != null && !baseUrl.isEmpty()) {
            apiBuilder.baseUrl(baseUrl);
        }
        if (apiKey != null && !apiKey.isEmpty()) {
            apiBuilder.apiKey(apiKey);
        }
        ;
        OpenAiChatOptions.Builder optionsBuilder = OpenAiChatOptions.builder()
                .N(1)
                .internalToolExecutionEnabled(false);
        if (model != null && !model.isEmpty()) {
            optionsBuilder.model(model);
        }

        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .openAiApi(apiBuilder.build())
                .defaultOptions(optionsBuilder.build())
                .build();
        return chatModel;
    }

    public ChatResponse call() {
        try {

            OpenAiChatModel client = getModel();

            historyMessageList.clear();
            toolCallCounter.clear();

            if (system != null && !system.isEmpty()) {
                historyMessageList.add(SystemMessage.builder()
                        .text(system)
                        .build());
            }

            historyMessageList.add(UserMessage.builder()
                    .text(question)
                    .build());


            Map<String, SpringAiToolDefinition> toolMap = new HashMap<>();
            if (toolDefinitionMap != null) {
                toolMap.putAll(toolDefinitionMap);
            }
            while (true) {
                while (historyMessageList.size() > 20) {
                    historyMessageList.removeFirst();
                }
                // 构建请求参数
                ChatResponse call = client.call(Prompt.builder()
                        .messages(historyMessageList)
                        .chatOptions(ToolCallingChatOptions.builder()
                                .toolCallbacks(SpringAiToolHelper.convertTools(toolMap))
                                .internalToolExecutionEnabled(false)
                                .model(model)
                                .build())
                        .build());

                List<Generation> choices = call.getResults();

                if (choices == null || choices.isEmpty()) {
                    return call;
                }
                boolean isToolCall = false;
                for (Generation choice : choices) {
                    ChatGenerationMetadata metadata = choice.getMetadata();
                    String finishReason = metadata.getFinishReason();
                    AssistantMessage message = choice.getOutput();

                    // 处理 function-calling 工具调用
                    if (message.hasToolCalls()) {
                        // 添加工具调用记录到消息列表
                        historyMessageList.add(message);

                        // 处理所有工具调用
                        List<AssistantMessage.ToolCall> toolCalls = message.getToolCalls();
                        for (AssistantMessage.ToolCall toolCall : toolCalls) {
                            AssistantMessage.ToolCall function = toolCall;
                            String name = function.name();
                            String toolCallId = function.id();

                            String callResult = null;
                            Throwable callEx = null;
                            try {
                                SpringAiToolDefinition definition = toolMap.get(name);
                                if (definition == null) {
                                    throw new IllegalArgumentException("not found this tool [" + name + "], please try others.");
                                }
                                String arguments = function.arguments();
                                String callCounterKey = name + "#" + arguments;
                                AtomicInteger counter = toolCallCounter.computeIfAbsent(callCounterKey, k -> new AtomicInteger());
                                int count = counter.incrementAndGet();
                                if (count > 10) {
                                    throw new IllegalStateException("tool [" + name + "] execute count exceed limit, execute reject!");
                                }
                                ToolRawDefinition rawDefinition = definition.getRawDefinition();
                                Map<String, Object> argumentsMap = fromJson(arguments, new TypeReference<Map<String, Object>>() {
                                });
                                Object ret = ToolRawHelper.invokeTool(rawDefinition, argumentsMap);
                                if (ret instanceof CharSequence) {
                                    callResult = String.valueOf(ret);
                                } else {
                                    String json = toJson(ret);
                                    callResult = json;
                                }
                            } catch (Throwable e) {
                                callEx = e;
                            }
                            if (callEx != null) {
                                if (callEx instanceof InvocationTargetException) {
                                    InvocationTargetException ite = (InvocationTargetException) callEx;
                                    callEx = ite.getTargetException();
                                }
                                callEx.printStackTrace();
                                callResult = "tool [" + name + "] invoke failure! cause by " + callEx.getClass() + ": " + callEx.getMessage();
                            }
                            historyMessageList.add(ToolResponseMessage.builder()
                                    .responses(new ArrayList<>(Collections.singletonList(
                                            new ToolResponseMessage.ToolResponse(function.id(), function.name(), callResult)
                                    )))
                                    .build());
                            isToolCall = true;

                        }

                    }

                    if (isToolCall) {
                        break;
                    }


                }
                // 如果发生了工具调用，则继续对话循环
                if (isToolCall) {
                    continue;
                }
                return call;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public String callAsString() {
        ChatResponse call = call();
        return unwrapResultAsString(call);
    }

    public static String unwrapResultAsString(ChatResponse call) {
        List<Generation> choices = call.getResults();
        if (choices == null || choices.isEmpty()) {
            return "";
        } else {
            StringBuilder builder = new StringBuilder();
            for (Generation choice : choices) {
                ChatGenerationMetadata metadata = choice.getMetadata();
                String finishReason = metadata.getFinishReason();
                AssistantMessage message = choice.getOutput();

                // 重置返回内容
                builder.setLength(0);

                // 解析响应内容为文本
                List<Media> mediaList = message.getMedia();
                if (mediaList != null && !mediaList.isEmpty()) {
                    for (Media media : mediaList) {
                        String id = media.getId();
                        String name = media.getName();
                        Object data = media.getData();
                        if (id != null && !id.isEmpty()) {
                            builder.append("---").append("\n")
                                    .append("# id").append("\n")
                                    .append(id);
                        }
                        if (name != null && !name.isEmpty()) {
                            builder.append("---").append("\n")
                                    .append("# name").append("\n")
                                    .append(name);
                        }
                        if (data != null) {
                            builder.append("---").append("\n")
                                    .append("# data").append("\n")
                                    .append(data);
                        }
                    }
                } else {
                    String content = message.getText();
                    builder.append(content);
                }
            }

            return builder.toString();
        }
    }

    public static String getPossibleApiKey(String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            apiKey = getPossibleApiKey();
        }
        return apiKey;
    }

    public static String getPossibleApiKey() {
        String apiKey = null;
        if (apiKey == null || apiKey.isEmpty()) {
            apiKey = System.getenv(API_KEY_NAME);
        }
        if (apiKey == null || apiKey.isEmpty()) {
            apiKey = System.getProperty(API_KEY_NAME);
        }
        return apiKey;
    }

    public static String getPossibleBaseUrl(String baseUrl) {
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = getPossibleBaseUrl();
        }
        return baseUrl;
    }

    public static String getPossibleBaseUrl() {
        String baseUrl = null;
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = System.getenv(BASE_URL_NAME);
        }
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = System.getProperty(BASE_URL_NAME);
        }
        return baseUrl;
    }
}
