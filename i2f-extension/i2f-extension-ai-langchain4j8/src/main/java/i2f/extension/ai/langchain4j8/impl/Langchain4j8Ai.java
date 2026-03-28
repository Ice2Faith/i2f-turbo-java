package i2f.extension.ai.langchain4j8.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.*;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.Response;
import i2f.ai.std.tool.ToolRawDefinition;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.extension.ai.langchain4j8.tool.Langchain4j8ToolDefinition;
import i2f.extension.ai.langchain4j8.tool.Langchain4j8ToolHelper;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class Langchain4j8Ai {
    public static final String API_KEY_NAME = "OPENAI_AI_API_KEY";
    public static final String BASE_URL_NAME = "OPENAI_AI_BASE_URL";
    public static final String DASHSCOPE_BASE_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1";

    public static final String DEFAULT_MODEL = "qwen-plus";

    protected String baseUrl = DASHSCOPE_BASE_URL;
    protected String apiKey;
    protected String system;
    protected String question;
    protected String model = DEFAULT_MODEL;
    protected Map<String, Langchain4j8ToolDefinition> toolDefinitionMap = new LinkedHashMap<>();
    protected Map<String, AtomicInteger> toolCallCounter = new ConcurrentHashMap<>();

    protected LinkedList<ChatMessage> historyMessageList = new LinkedList<>();

    protected static final Gson gson = new Gson();

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> type) {
        return gson.fromJson(json, type);
    }

    public static <T> T fromJson(String json, TypeToken<T> type) {
        return gson.fromJson(json, type.getType());
    }

    public Response<AiMessage> call() {
        try {
            OpenAiChatModel chatModel = OpenAiChatModel.builder()
                    .baseUrl(baseUrl)
                    .apiKey(apiKey)
                    .modelName(model)
                    .build();

            ChatMessage systemMsg = null;
            if (system != null && !system.isEmpty()) {
                systemMsg = new SystemMessage(system);
            }
            ChatMessage userMsg = new UserMessage(question);
            historyMessageList.clear();
            toolCallCounter.clear();
            if (systemMsg != null) {
                historyMessageList.add(systemMsg);
            }
            historyMessageList.add(userMsg);
            Map<String, Langchain4j8ToolDefinition> toolMap = new HashMap<>();
            if (toolDefinitionMap != null) {
                toolMap.putAll(toolDefinitionMap);
            }
            while (true) {
                while (historyMessageList.size() > 20) {
                    historyMessageList.removeFirst();
                }

                Response<AiMessage> resp = chatModel.generate(
                        historyMessageList,
                        Langchain4j8ToolHelper.convertTools(toolMap)
                );

                FinishReason finishReason = resp.finishReason();
                AiMessage message = resp.content();

                if (finishReason == FinishReason.STOP) {
                    return resp;
                }
                boolean isToolCall = false;
                if (finishReason == FinishReason.TOOL_EXECUTION || message.hasToolExecutionRequests()) {

                    // 添加工具调用记录到消息列表
                    historyMessageList.add(message);

                    // 处理所有工具调用

                    List<ToolExecutionRequest> toolCalls = message.toolExecutionRequests();
                    for (ToolExecutionRequest toolCall : toolCalls) {
                        ToolExecutionRequest function = toolCall;
                        String name = function.name();

                        String callResult = null;
                        Throwable callEx = null;
                        try {
                            Langchain4j8ToolDefinition definition = toolMap.get(name);
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
                            Map<String, Object> argumentsMap = fromJson(arguments, new TypeToken<Map<String, Object>>() {
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
                        ChatMessage toolMsg = new ToolExecutionResultMessage(function.id(), function.name(), callResult);
                        historyMessageList.add(toolMsg);
                        isToolCall = true;

                    }

                }

                // 如果发生了工具调用，则继续对话循环
                if (isToolCall) {
                    continue;
                }
                return resp;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public String callAsString() {
        Response<AiMessage> call = call();
        return unwrapResultAsString(call);
    }

    public static String unwrapResultAsString(Response<AiMessage> call) {
        StringBuilder builder = new StringBuilder();
        AiMessage message = call.content();
        FinishReason reason = call.finishReason();

        // 重置返回内容
        builder.setLength(0);

        String content = message.text();
        builder.append(content);

        return builder.toString();
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