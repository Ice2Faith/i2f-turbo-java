package i2f.ai.std.agent;

import i2f.ai.std.model.AiModel;
import i2f.ai.std.model.AiRequest;
import i2f.ai.std.model.message.AiMessage;
import i2f.ai.std.model.message.impl.AssistantMessage;
import i2f.ai.std.model.message.impl.SystemMessage;
import i2f.ai.std.model.message.impl.ToolMessage;
import i2f.ai.std.model.message.impl.UserMessage;
import i2f.ai.std.model.message.tool.ToolCallRequest;
import i2f.ai.std.rag.RagEmbedding;
import i2f.ai.std.rag.RagTools;
import i2f.ai.std.rag.RagWorker;
import i2f.ai.std.skill.SkillDefinition;
import i2f.ai.std.skill.SkillsHelper;
import i2f.ai.std.skill.SkillsTools;
import i2f.ai.std.tool.ToolRawDefinition;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.ai.std.tool.schema.JsonSchema;
import i2f.ai.std.tool.schema.JsonSchemaAnnotationResolver;
import i2f.proxy.std.IProxyInvocationHandler;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.typeof.TypeOf;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2026/3/25 15:33
 * @desc
 */
@Data
@NoArgsConstructor
public class AiAgent {
    public static final ExecutorService DEFAULT_TOOL_POOL = new ForkJoinPool(Runtime.getRuntime().availableProcessors() * 2);
    protected volatile AiModel model;
    protected volatile RagWorker ragWorker;
    protected volatile IJsonSerializer jsonSerializer;
    protected JsonSchemaAnnotationResolver jsonSchemaAnnotationResolver = JsonSchemaAnnotationResolver.INSTANCE;
    protected volatile ExecutorService toolExecPool = DEFAULT_TOOL_POOL;

    public AiAgent model(AiModel model) {
        this.model = model;
        return this;
    }

    public AiAgent ragWorker(RagWorker ragWorker) {
        this.ragWorker = ragWorker;
        return this;
    }

    public AiAgent jsonSerializer(IJsonSerializer jsonSerializer) {
        this.jsonSerializer = jsonSerializer;
        return this;
    }

    public AiAgent jsonSchemaAnnotationResolver(JsonSchemaAnnotationResolver jsonSchemaAnnotationResolver) {
        this.jsonSchemaAnnotationResolver = jsonSchemaAnnotationResolver;
        return this;
    }

    public AiAgent toolExecPool(ExecutorService toolExecPool) {
        this.toolExecPool = toolExecPool;
        return this;
    }

    public AiAgentResponse generate(String user) {
        return generate(null, user, null);
    }

    public AiAgentResponse generate(String system, String user) {
        return generate(system, user, null);
    }

    public AiAgentResponse generate(String user, AiAgentContext context) {
        return generate(null, user, context);
    }

    public AiAgentResponse generate(String system, String user, AiAgentContext context) {
        AiRequest request = new AiRequest();
        List<AiMessage> messageList = new ArrayList<>();
        if (system != null && !system.isEmpty()) {
            messageList.add(new SystemMessage(system));
        }
        messageList.add(new UserMessage(user));
        request.setMessageList(messageList);
        return generate(request, context);
    }

    public AiAgentResponse generate(AiRequest request) {
        return generate(request, null);
    }

    public AiAgentResponse generate(AiRequest request, AiAgentContext ctx) {
        AiAgentContext context = ctx == null ? new AiAgentContext() : ctx;
        // 设置ThreadLocal,以便在@Tool方法内部获取执行环境变量等
        // 能够在线程中获取用户的角色、权限等绑定的信息，以允许在工具调用等场景中使用
        AiAgentContext.CONTEXT.set(context);
        try {
            AiAgentResponse ret = new AiAgentResponse();
            ret.setContext(context);
            ret.setMessageList(new ArrayList<>(request.getMessageList()));
            ret.setToolMap(new ConcurrentHashMap<>());
            if (request.getToolMap() != null) {
                ret.getToolMap().putAll(request.getToolMap());
            }

            List<AiMessage> messageList = ret.getMessageList();
            Map<String, ToolRawDefinition> toolMap = ret.getToolMap();

            // 移除不包含 tag 的工具
            filterToolsByRule(toolMap, context.getTagsFilterChain());


            // 处理结构化输出需求
            if (context.isEnableStructOutput()) {
                Class<?> outputType = context.getOutputType();
                injectStructOutputPrompt(outputType, messageList);
            }

            // 处理技能
            if (context.isEnableSkills()) {
                if (context.getSkillsMap() == null) {
                    context.setSkillsMap(new HashMap<>());
                }
                Map<String, SkillDefinition> skillsMap = new LinkedHashMap<>(context.getSkillsMap());
                // 移除不包含 tag 的技能
                filterSkillsByRule(skillsMap, context.getTagsFilterChain());


                // 有技能才需要注入相关工具和提示词
                if (!skillsMap.isEmpty()) {
                    injectSkillsPromptAndTools(skillsMap, messageList, toolMap);
                }
            }

            // 处理RAG被动知识检索
            if (context.isEnableRag()) {
                injectUserQuestionRagPrompt(messageList, context.getRagTopCount());
            }

            // 处理工具化的RAG主动检索
            if (context.isEnableRagAct()) {
                injectRagTools(toolMap);
            }

            AtomicInteger allToolCallCount = new AtomicInteger(0);
            Map<String, AtomicInteger> singleToolCallCount = new ConcurrentHashMap<>();
            Map<String, AtomicInteger> singleToolSameArgumentFailureCount = new ConcurrentHashMap<>();
            // Re-Act 循环
            while (true) {
                compressOrDropHistoryMessage(messageList, context);

                AiRequest modelReq = new AiRequest();
                modelReq.setMessageList(new ArrayList<>(messageList));
                modelReq.setToolMap(new TreeMap<>(ret.getToolMap()));
                AssistantMessage resp = model.generate(modelReq);
                messageList.add(resp);

                // 中断只有在执行过一次之后才进行，也就是至少让LLM回答一次
                if (context.getInterrupt().get()) {
                    return ret;
                }
                // 处理 function-calling 调用
                AtomicBoolean isToolCall = new AtomicBoolean(false);
                if (resp.getFinishReason() == AssistantMessage.FinishReason.TOOL_CALL) {
                    List<ToolCallRequest> toolCallList = resp.getToolCallRequestList();
                    if (toolCallList != null && !toolCallList.isEmpty()) {
                        CopyOnWriteArrayList<ToolMessage> toolMsgList = new CopyOnWriteArrayList<>();
                        CountDownLatch latch = new CountDownLatch(toolCallList.size());
                        for (ToolCallRequest toolCall : toolCallList) {
                            Runnable task = () -> {
                                try {
                                    String toolCallId = toolCall.getId();
                                    String name = toolCall.getName();
                                    String arguments = toolCall.getArguments();

                                    if (context.getInterrupt().get()) {
                                        return;
                                    }

                                    isToolCall.set(true);

                                    int allCount = allToolCallCount.incrementAndGet();
                                    if (allCount >= context.getMaxAllToolCallCount().get()) {
                                        toolMsgList.add(new ToolMessage(toolCallId, "error, all tools call count exceed limit count."));
                                        return;
                                    }

                                    AtomicInteger counter = singleToolCallCount.computeIfAbsent(name, k -> new AtomicInteger(0));
                                    int singleCount = counter.incrementAndGet();
                                    if (singleCount >= context.getMaxSingleToolCallCount().get()) {
                                        toolMsgList.add(new ToolMessage(toolCallId, "error, tool [" + name + "] call count exceed limit count."));
                                        return;
                                    }

                                    String failureKey = name + "##" + arguments;
                                    AtomicInteger failureCounter = singleToolSameArgumentFailureCount.computeIfAbsent(failureKey, k -> new AtomicInteger(0));
                                    if (failureCounter.get() >= context.getMaxSingleToolSameArgumentFailureCount().get()) {
                                        toolMsgList.add(new ToolMessage(toolCallId, "error, tool [" + name + "] call failure count exceed limit count, please check arguments."));
                                        return;
                                    }

                                    String callResult = null;
                                    Throwable callEx = null;
                                    ToolRawDefinition rawDefinition = null;

                                    try {
                                        Object val = callTool(toolCall, toolMap, context.getToolInterceptor());
                                        if (val instanceof CharSequence) {
                                            callResult = String.valueOf(val);
                                        } else {
                                            String json = jsonSerializer.serialize(val);
                                            callResult = json;
                                        }
                                    } catch (Throwable e) {
                                        callEx = e;
                                        failureCounter.incrementAndGet();
                                    }
                                    if (callEx != null) {
                                        if (callEx instanceof InvocationTargetException) {
                                            InvocationTargetException ite = (InvocationTargetException) callEx;
                                            callEx = ite.getTargetException();
                                        }
                                        callEx.printStackTrace();
                                        callResult = "tool [" + name + "] invoke failure! cause by " + callEx.getClass() + ": " + callEx.getMessage();
                                    }
                                    ToolMessage toolMsg = new ToolMessage(toolCallId, callResult);
                                    toolMsg.setRequest(toolCall);
                                    toolMsg.setDefinition(rawDefinition);
                                    toolMsgList.add(toolMsg);
                                } finally {
                                    latch.countDown();
                                }
                            };

                            if (context.getEnableParallelToolCall().get()) {
                                if (context.getToolExecPool() != null) {
                                    context.getToolExecPool().submit(task);
                                } else if (toolExecPool != null) {
                                    toolExecPool.submit(task);
                                } else {
                                    DEFAULT_TOOL_POOL.submit(task);
                                }
                            } else {
                                task.run();
                            }
                        }

                        try {
                            latch.await();
                        } catch (InterruptedException e) {

                        }
                        if (!toolMsgList.isEmpty()) {
                            messageList.addAll(toolMsgList);
                        }
                    }
                }
                if (context.getInterrupt().get()) {
                    return ret;
                }
                if (isToolCall.get()) {
                    continue;
                }
                return ret;
            }
        } finally {
            AiAgentContext.CONTEXT.remove();
        }

    }

    public Object callTool(ToolCallRequest request, Map<String, ToolRawDefinition> toolMap, IProxyInvocationHandler interceptor) throws Throwable {
        String name = request.getName();
        String arguments = request.getArguments();

        Map<String, Object> argumentsMap = jsonSerializer.deserializeAsMap(arguments);
        ToolRawDefinition rawDefinition = toolMap.get(name);
        if (rawDefinition == null) {
            return callToolNotFoundDefinition(request, toolMap, interceptor);
        }
        Object val = ToolRawHelper.invokeTool(rawDefinition, argumentsMap, interceptor);
        return val;
    }

    public Object callToolNotFoundDefinition(ToolCallRequest request, Map<String, ToolRawDefinition> toolMap, IProxyInvocationHandler interceptor) throws Throwable {
        String name = request.getName();
        throw new IllegalArgumentException("not found this tool [" + name + "], please try others.");
    }

    public void compressOrDropHistoryMessage(List<AiMessage> messageList, AiAgentContext context) {
        // 获取第一条用户消息，用于后续的消息裁剪与压缩
        int firstUserMessageIndex = 0;
        for (AiMessage item : messageList) {
            if (item instanceof UserMessage) {
                break;
            }
            firstUserMessageIndex++;
        }
        // 处理历史消息压缩
        if (context.getCompressHistoryMessage().get()) {
            List<AiMessage> tmpList = new ArrayList<>();
            while (messageList.size() >= context.getCompressHistoryCount().get()) {
                tmpList.add(messageList.remove(firstUserMessageIndex));
            }
            if (!tmpList.isEmpty()) {
                tmpList.add(new UserMessage("总结上述对话内容"));
                AiRequest tmpReq = new AiRequest();
                tmpReq.setMessageList(tmpList);
                AssistantMessage result = model.generate(tmpReq);
                messageList.add(result);
            }
        }
        // 嘴里历史消息截断
        while (messageList.size() > context.getMaxKeepMessageCount().get()) {
            messageList.remove(context.getKeepFirstUserMessage().get() ? firstUserMessageIndex + 1 : firstUserMessageIndex);
        }
    }

    public void injectRagTools(Map<String, ToolRawDefinition> toolMap) {
        if (ragWorker == null) {
            return;
        }
        RagTools tool = new RagTools();
        tool.setWorker(ragWorker);
        Map<String, ToolRawDefinition> map = ToolRawHelper.parseTools(jsonSchemaAnnotationResolver, tool);
        for (Map.Entry<String, ToolRawDefinition> entry : map.entrySet()) {
            toolMap.computeIfAbsent(entry.getKey(), k -> entry.getValue());
        }
    }

    public void injectUserQuestionRagPrompt(List<AiMessage> messageList, int topN) {
        if (messageList == null || messageList.isEmpty()) {
            return;
        }
        if (ragWorker == null) {
            return;
        }
        AiMessage lastMessage = messageList.get(messageList.size() - 1);
        if (!(lastMessage instanceof UserMessage)) {
            return;
        }
        UserMessage msg = (UserMessage) lastMessage;
        String text = msg.getText();
        List<RagEmbedding> list = ragWorker.similar(text, topN);
        if (list.isEmpty()) {
            return;
        }
        StringBuilder builder = new StringBuilder();

        builder.append("# 相关参考资料").append("\n");
        for (int j = 0; j < list.size(); j++) {
            RagEmbedding embedding = list.get(j);
            String content = embedding.getContent();
            if (j > 0) {
                builder.append("\n");
            }
            builder.append("-------------------------------------").append("\n");
            builder.append("## 参考资料 ").append(j + 1).append("\n");
            builder.append(content).append("\n");
        }

        messageList.add(new SystemMessage(builder.toString()));


    }

    private void injectSkillsPromptAndTools(Map<String, SkillDefinition> skillsMap, List<AiMessage> messageList, Map<String, ToolRawDefinition> toolMap) {
        String system = SkillsHelper.convertSkillDefinitionsAsSystemPrompt(skillsMap);
        messageList.add(0, new SystemMessage(system));

        Map<String, ToolRawDefinition> map = ToolRawHelper.parseTools(jsonSchemaAnnotationResolver, new SkillsTools());
        for (Map.Entry<String, ToolRawDefinition> entry : map.entrySet()) {
            toolMap.computeIfAbsent(entry.getKey(), k -> entry.getValue());
        }
    }

    public void filterSkillsByRule(Map<String, SkillDefinition> skillsMap, List<Predicate<Set<String>>> tagsFilterChain) {
        if (tagsFilterChain == null || tagsFilterChain.isEmpty()) {
            return;
        }
        if (skillsMap == null || skillsMap.isEmpty()) {
            return;
        }
        Set<String> removeKeys = new HashSet<>();
        for (Map.Entry<String, SkillDefinition> entry : skillsMap.entrySet()) {
            SkillDefinition definition = entry.getValue();
            Set<String> tags = definition.getTags();
            if (tags != null && !tags.isEmpty()) {
                boolean include = true;
                for (Predicate<Set<String>> predicate : tagsFilterChain) {
                    if (!predicate.test(tags)) {
                        include = false;
                    }
                }
                if (!include) {
                    removeKeys.add(entry.getKey());
                }
            }
        }
        if (!removeKeys.isEmpty()) {
            for (String key : removeKeys) {
                skillsMap.remove(key);
            }
        }
    }

    public void filterToolsByRule(Map<String, ToolRawDefinition> toolMap, List<Predicate<Set<String>>> tagsFilterChain) {
        if (tagsFilterChain == null || tagsFilterChain.isEmpty()) {
            return;
        }
        if (toolMap == null || toolMap.isEmpty()) {
            return;
        }
        Set<String> removeKeys = new HashSet<>();
        for (Map.Entry<String, ToolRawDefinition> entry : toolMap.entrySet()) {
            ToolRawDefinition definition = entry.getValue();
            Set<String> tags = definition.getTags();
            if (tags != null && !tags.isEmpty()) {
                boolean include = true;
                for (Predicate<Set<String>> predicate : tagsFilterChain) {
                    if (!predicate.test(tags)) {
                        include = false;
                    }
                }
                if (!include) {
                    removeKeys.add(entry.getKey());
                }
            }
        }
        if (!removeKeys.isEmpty()) {
            for (String key : removeKeys) {
                toolMap.remove(key);
            }
        }
    }

    public void injectStructOutputPrompt(Class<?> outputType, List<AiMessage> messageList) {
        if (outputType == null) {
            return;
        }
        if (Object.class.equals(outputType)) {
            return;
        }
        if (TypeOf.typeOfAny(outputType, CharSequence.class, Void.class)) {
            return;
        }

        Map<String, Object> outputSchema = JsonSchema.getTypeJsonSchema(jsonSchemaAnnotationResolver, outputType);
        String outputSchemaJson = jsonSerializer.serialize(outputSchema);
        StringBuilder builder = new StringBuilder();
        builder.append("# 最终输出约束\n" +
                        "- 思考过程中或中间过程可以使用随意格式进行答复\n" +
                        "- 但是确定并检查了输出最终答复的时候，必须严格按照下面的JSON格式进行答复\n" +
                        "- 答复必须是标准JSON的内容，不能包含markdown标记，不能包含除了JSON之外多余的描述性信息\n" +
                        "- 输出的JSON格式约束如下：\n" +
                        "").append(outputSchemaJson)
                .append("\n");
        messageList.add(0, new SystemMessage(builder.toString()));

    }
}
