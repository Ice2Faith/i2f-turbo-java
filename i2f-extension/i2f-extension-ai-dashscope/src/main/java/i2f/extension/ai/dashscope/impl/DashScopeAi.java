package i2f.extension.ai.dashscope.impl;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationOutput;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.*;
import com.alibaba.dashscope.tools.ToolCallBase;
import com.alibaba.dashscope.tools.ToolCallFunction;
import com.alibaba.dashscope.utils.JsonUtils;
import com.google.gson.reflect.TypeToken;
import i2f.convert.obj.ObjectConvertor;
import i2f.extension.ai.dashscope.tool.DashScopeToolDefinition;
import i2f.extension.ai.dashscope.tool.DashScopeToolHelper;
import i2f.typeof.TypeOf;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
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
public class DashScopeAi {
    public static final String API_KEY_NAME = "DASHSCOPE_AI_API_KEY";
    public static final String TOOL_CALLS = "tool_calls";

    public static final String DEFAULT_MODEL = "qwen-plus";
    public static final String DEFAULT_RESP_JSON_ADDITIONAL_MESSAGE = "请严格按照JSON格式输出且不需要任何多余的解释或者非JSON格式的信息。";

    protected String apiKey;
    protected String system;
    protected String question;
    protected String model = DEFAULT_MODEL;
    protected GenResultFormat resultFormat = GenResultFormat.TEXT;
    protected GenResponseFormat responseFormat = GenResponseFormat.TEXT;
    protected String respJsonAdditionalUserMessage = DEFAULT_RESP_JSON_ADDITIONAL_MESSAGE;
    protected Map<String, DashScopeToolDefinition> toolDefinitionMap = new LinkedHashMap<>();
    protected Map<String, AtomicInteger> toolCallCounter = new ConcurrentHashMap<>();

    protected LinkedList<Message> historyMessageList = new LinkedList<>();


    public static enum GenResultFormat {
        MESSAGE(GenerationParam.ResultFormat.MESSAGE),
        TEXT(GenerationParam.ResultFormat.TEXT);
        private String text;

        private GenResultFormat(String text) {
            this.text = text;
        }

        public String text() {
            return this.text;
        }
    }

    public static enum GenResponseFormat {
        JSON(ResponseFormat.JSON_OBJECT),
        TEXT(ResponseFormat.TEXT);
        private String text;

        private GenResponseFormat(String text) {
            this.text = text;
        }

        public String text() {
            return this.text;
        }
    }

    public GenerationResult call() {
        try {
            GenResultFormat result = resultFormat;
            if (result == null) {
                result = GenResultFormat.TEXT;
            }
            GenResponseFormat resp = responseFormat;
            if (resp == null) {
                resp = GenResponseFormat.TEXT;
            }
            if (resp == GenResponseFormat.JSON && (respJsonAdditionalUserMessage != null)) {
                question = question + "\n" + respJsonAdditionalUserMessage;
            }
            Generation gen = new Generation();
            Message systemMsg = null;
            if (system != null && !system.isEmpty()) {
                systemMsg = Message.builder()
                        .role(Role.SYSTEM.getValue())
                        .content(system)
                        .build();
            }
            Message userMsg = Message.builder()
                    .role(Role.USER.getValue())
                    .content(question)
                    .build();
            historyMessageList.clear();
            toolCallCounter.clear();
            if (systemMsg != null) {
                historyMessageList.add(systemMsg);
            }
            historyMessageList.add(userMsg);
            Map<String, DashScopeToolDefinition> toolMap = new HashMap<>();
            if (toolDefinitionMap != null) {
                toolMap.putAll(toolDefinitionMap);
            }
            while (true) {
                while (historyMessageList.size() > 20) {
                    historyMessageList.removeFirst();
                }
                GenerationParam param = GenerationParam.builder()
                        // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-xxx")
                        .apiKey(apiKey)
                        // 此处以qwen-plus为例，可按需更换模型名称。模型列表：https://help.aliyun.com/zh/model-studio/getting-started/models
                        .model(model)
                        .messages(historyMessageList)
                        .resultFormat(result.text())
                        .responseFormat(ResponseFormat.from(responseFormat.text()))
                        .tools(DashScopeToolHelper.convertTools(toolMap))
                        .build();
                GenerationResult call = gen.call(param);
                List<GenerationOutput.Choice> choices = call.getOutput().getChoices();
                if (choices == null || choices.isEmpty()) {
                    return call;
                }
                boolean isToolCall = false;
                for (GenerationOutput.Choice choice : choices) {
                    String reason = choice.getFinishReason();
                    Message message = choice.getMessage();

                    // 处理 function-calling 工具调用
                    if (TOOL_CALLS.equals(reason)) {
                        // 添加工具调用记录到消息列表
                        historyMessageList.add(message);

                        // 处理所有工具调用
                        List<ToolCallBase> toolCalls = message.getToolCalls();
                        for (ToolCallBase toolCall : toolCalls) {
                            if (toolCall instanceof ToolCallFunction) {
                                ToolCallFunction callFunction = (ToolCallFunction) toolCall;
                                ToolCallFunction.CallFunction function = callFunction.getFunction();
                                String name = function.getName();

                                String callResult = null;
                                Throwable callEx = null;
                                try {
                                    DashScopeToolDefinition definition = toolMap.get(name);
                                    Method bindMethod = definition.getBindMethod();
                                    String arguments = function.getArguments();
                                    String callCounterKey = name + "#" + arguments;
                                    AtomicInteger counter = toolCallCounter.computeIfAbsent(callCounterKey, k -> new AtomicInteger());
                                    int count = counter.incrementAndGet();
                                    if (count > 10) {
                                        throw new IllegalStateException("tool [" + name + "] execute count exceed limit, execute reject!");
                                    }
                                    Map<String, Object> map = JsonUtils.fromJson(arguments, new TypeToken<Map<String, Object>>() {
                                    }.getType());
                                    Object[] args = new Object[definition.getFunctionParameterNames().size()];
                                    for (int i = 0; i < args.length; i++) {
                                        Parameter[] parameters = bindMethod.getParameters();
                                        Object value = map.get(definition.getFunctionParameterNames().get(i));
                                        if (i <= parameters.length) {
                                            if (!TypeOf.instanceOf(value, parameters[i].getType())) {
                                                if (ObjectConvertor.isDateType(parameters[i].getType())) {
                                                    Date date = ObjectConvertor.tryParseDate(String.valueOf(value));
                                                    if (date != null) {
                                                        value = ObjectConvertor.tryConvertAsType(value, parameters[i].getType());
                                                    }
                                                }
                                            }
                                            if (!TypeOf.instanceOf(value, parameters[i].getType())) {
                                                try {
                                                    // 尝试转换类型为复杂POJO对象
                                                    Class<?> parameterType = parameters[i].getType();
                                                    if (!TypeOf.isBaseType(parameterType)) {
                                                        String json = JsonUtils.toJson(value);
                                                        value = JsonUtils.fromJson(json, parameterType);
                                                    }
                                                } catch (Throwable e) {

                                                }
                                            }

                                            if (!TypeOf.instanceOf(value, parameters[i].getType())) {
                                                try {
                                                    value = ObjectConvertor.tryConvertAsType(value, parameters[i].getType());
                                                    if (TypeOf.instanceOf(value, parameters[i].getType())) {
                                                    }
                                                } catch (Exception e) {

                                                }
                                            }

                                        }
                                        args[i] = value;
                                    }

                                    Object target = definition.getBindTarget();
                                    if (Modifier.isStatic(bindMethod.getModifiers())) {
                                        target = null;
                                    } else {
                                        if (target == null) {
                                            target = definition.getBindClass().newInstance();
                                        }
                                    }
                                    Object ret = bindMethod.invoke(target, args);
                                    String json = JsonUtils.toJson(ret);
                                    callResult = json;
                                } catch (Throwable e) {
                                    callEx = e;
                                }
                                if (callEx != null) {
                                    if (callEx instanceof InvocationTargetException) {
                                        InvocationTargetException ite = (InvocationTargetException) callEx;
                                        callEx = ite.getTargetException();
                                    }
                                    callResult = "tool [" + name + "] invoke failure! cause by " + callEx.getClass() + ": " + callEx.getMessage();
                                }
                                Message toolMsg = Message.builder()
                                        .role(Role.TOOL.getValue())
                                        .content(callResult)
                                        .build();
                                historyMessageList.add(toolMsg);
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
        GenerationResult call = call();
        return unwrapResultAsString(call);
    }

    public static String unwrapResultAsString(GenerationResult call) {
        List<GenerationOutput.Choice> choices = call.getOutput().getChoices();
        if (choices == null || choices.isEmpty()) {
            return call.getOutput().getText();
        } else {
            StringBuilder builder = new StringBuilder();
            for (GenerationOutput.Choice choice : choices) {
                String reason = choice.getFinishReason();
                Message message = choice.getMessage();

                // 重置返回内容
                builder.setLength(0);

                // 解析响应内容为文本
                List<MessageContentBase> contents = message.getContents();
                if (contents != null && !contents.isEmpty()) {
                    for (MessageContentBase content : contents) {
                        if (content instanceof MessageContentText) {
                            MessageContentText ctt = (MessageContentText) content;
                            String text = ctt.getText();
                            builder.append("---").append("\n")
                                    .append("# text").append("\n")
                                    .append(text);
                        } else if (content instanceof MessageContentImageURL) {
                            MessageContentImageURL ctt = (MessageContentImageURL) content;
                            String url = ctt.getImageURL().getUrl();
                            builder.append("---").append("\n")
                                    .append("# url").append("\n")
                                    .append(url);
                        }
                    }
                } else {
                    String content = message.getContent();
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
}
