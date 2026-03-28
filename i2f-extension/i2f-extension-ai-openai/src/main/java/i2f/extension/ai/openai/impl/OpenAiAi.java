package i2f.extension.ai.openai.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.*;
import i2f.ai.std.tool.ToolRawDefinition;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.extension.ai.openai.tool.OpenAiToolDefinition;
import i2f.extension.ai.openai.tool.OpenAiToolHelper;
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
public class OpenAiAi {
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
    protected Map<String, OpenAiToolDefinition> toolDefinitionMap = new LinkedHashMap<>();
    protected Map<String, AtomicInteger> toolCallCounter = new ConcurrentHashMap<>();

    protected LinkedList<ChatCompletionMessageParam> historyMessageList = new LinkedList<>();

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

    public ChatCompletion call() {
        try {

            OpenAIOkHttpClient.Builder builder = OpenAIOkHttpClient.builder()
                    .apiKey(apiKey);
            if (baseUrl != null && !baseUrl.isEmpty()) {
                builder.baseUrl(baseUrl);
            }
            OpenAIClient client = builder.build();

            historyMessageList.clear();
            toolCallCounter.clear();

            if (system != null && !system.isEmpty()) {
                historyMessageList.add(ChatCompletionMessageParam.ofSystem(ChatCompletionSystemMessageParam.builder()
                        .content(system)
                        .build()));
            }

            historyMessageList.add(ChatCompletionMessageParam.ofUser(ChatCompletionUserMessageParam.builder()
                    .content(question)
                    .build()));


            Map<String, OpenAiToolDefinition> toolMap = new HashMap<>();
            if (toolDefinitionMap != null) {
                toolMap.putAll(toolDefinitionMap);
            }
            while (true) {
                while (historyMessageList.size() > 20) {
                    historyMessageList.removeFirst();
                }
                // 构建请求参数
                ChatCompletionCreateParams.Builder paramsBuilder = ChatCompletionCreateParams.builder()
                        .model(model) // 推荐使用支持 tool 的模型
                        .messages(historyMessageList)
                        .tools(OpenAiToolHelper.convertTools(toolMap));

                // 发送请求
                ChatCompletion call = client.chat().completions().create(paramsBuilder.build());
                List<ChatCompletion.Choice> choices = call.choices();

                if (choices == null || choices.isEmpty()) {
                    return call;
                }
                boolean isToolCall = false;
                for (ChatCompletion.Choice choice : choices) {
                    ChatCompletion.Choice.FinishReason finishReason = choice.finishReason();
                    ChatCompletionMessage message = choice.message();


                    // 处理 function-calling 工具调用
                    if (ChatCompletion.Choice.FinishReason.TOOL_CALLS.equals(finishReason)) {
                        // 添加工具调用记录到消息列表
                        historyMessageList.add(ChatCompletionMessageParam.ofAssistant(message.toParam()));

                        // 处理所有工具调用
                        List<ChatCompletionMessageToolCall> toolCalls = message.toolCalls().orElse(new ArrayList<>());
                        for (ChatCompletionMessageToolCall toolCall : toolCalls) {
                            if (toolCall.isFunction()) {
                                ChatCompletionMessageFunctionToolCall callFunction = toolCall.asFunction();
                                ChatCompletionMessageFunctionToolCall.Function function = callFunction.function();
                                String name = function.name();
                                String toolCallId = callFunction.id();

                                String callResult = null;
                                Throwable callEx = null;
                                try {
                                    OpenAiToolDefinition definition = toolMap.get(name);
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
                                historyMessageList.add(ChatCompletionMessageParam.ofTool(ChatCompletionToolMessageParam.builder()
                                        .content(callResult)
                                        .toolCallId(toolCallId)
                                        .build()));
                                isToolCall = true;
                            } else {
                                throw new IllegalStateException("not support this type function-calling");
                            }
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
        ChatCompletion call = call();
        return unwrapResultAsString(call);
    }

    public static String unwrapResultAsString(ChatCompletion call) {
        List<ChatCompletion.Choice> choices = call.choices();
        if (choices == null || choices.isEmpty()) {
            return "";
        } else {
            StringBuilder builder = new StringBuilder();
            for (ChatCompletion.Choice choice : choices) {
                ChatCompletion.Choice.FinishReason reason = choice.finishReason();
                ChatCompletionMessage message = choice.message();

                // 重置返回内容
                builder.setLength(0);

                // 解析响应内容为文本
                Optional<ChatCompletionAudio> audio = message.audio();
                if (audio.isPresent()) {
                    ChatCompletionAudio completionAudio = audio.get();
                    if (completionAudio.transcript() != null) {
                        String text = completionAudio.transcript();
                        builder.append("---").append("\n")
                                .append("# text").append("\n")
                                .append(text);
                    }
                    if (completionAudio.data() != null) {
                        String url = completionAudio.data();
                        builder.append("---").append("\n")
                                .append("# url").append("\n")
                                .append(url);
                    }
                } else {
                    String content = message.content().orElse("");
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
