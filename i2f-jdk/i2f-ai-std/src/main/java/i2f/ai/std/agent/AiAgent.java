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
import i2f.ai.std.skill.SkillsHelper;
import i2f.ai.std.skill.SkillsTools;
import i2f.ai.std.tool.ToolRawDefinition;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.serialize.std.str.json.IJsonSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

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

        AiAgentResponse ret = new AiAgentResponse();
        ret.setContext(context);
        ret.setMessageList(new ArrayList<>(request.getMessageList()));
        ret.setToolMap(new ConcurrentHashMap<>());
        if (request.getToolMap() != null) {
            ret.getToolMap().putAll(request.getToolMap());
        }

        List<AiMessage> messageList = ret.getMessageList();

        Map<String, ToolRawDefinition> toolMap = ret.getToolMap();
        if (context.isEnableSkills()) {
            String system = SkillsHelper.convertSkillDefinitionsAsSystemPrompt(context.getSkillsMap());
            messageList.add(0, new SystemMessage(system));

            Map<String, ToolRawDefinition> map = ToolRawHelper.parseTools(new SkillsTools());
            for (Map.Entry<String, ToolRawDefinition> entry : map.entrySet()) {
                toolMap.computeIfAbsent(entry.getKey(), k -> entry.getValue());
            }
        }
        if (context.isEnableRag()) {
            if (ragWorker != null) {
                for (int i = messageList.size() - 1; i >= 0; i--) {
                    AiMessage item = messageList.get(i);
                    if (item instanceof UserMessage) {
                        UserMessage msg = (UserMessage) item;
                        String text = msg.getText();
                        List<RagEmbedding> list = ragWorker.similar(text, context.getRagTopCount());
                        if (!list.isEmpty()) {
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
                            if (i + 1 < messageList.size()) {
                                messageList.add(i + 1, new SystemMessage(builder.toString()));
                            } else {
                                messageList.add(new SystemMessage(builder.toString()));
                            }
                        }
                        break;
                    }
                }

            }
        }
        if (context.isEnableRagAct()) {
            if (ragWorker != null) {
                RagTools tool = new RagTools();
                tool.setWorker(ragWorker);
                Map<String, ToolRawDefinition> map = ToolRawHelper.parseTools(tool);
                for (Map.Entry<String, ToolRawDefinition> entry : map.entrySet()) {
                    toolMap.computeIfAbsent(entry.getKey(), k -> entry.getValue());
                }
            }
        }
        int firstUserMessageIndex = 0;
        for (AiMessage item : messageList) {
            if (item instanceof UserMessage) {
                break;
            }
            firstUserMessageIndex++;
        }
        AtomicInteger allToolCallCount = new AtomicInteger(0);
        Map<String, AtomicInteger> singleToolCallCount = new ConcurrentHashMap<>();
        Map<String, AtomicInteger> singleToolSameArgumentFailureCount = new ConcurrentHashMap<>();
        while (true) {
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
            while (messageList.size() > context.getMaxKeepMessageCount().get()) {
                messageList.remove(context.getKeepFirstUserMessage().get() ? firstUserMessageIndex + 1 : firstUserMessageIndex);
            }
            AiRequest modelReq = new AiRequest();
            modelReq.setMessageList(new ArrayList<>(messageList));
            modelReq.setToolMap(new TreeMap<>(ret.getToolMap()));
            AssistantMessage resp = model.generate(modelReq);
            messageList.add(resp);
            if (context.getInterrupt().get()) {
                return ret;
            }
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
                                // 设置ThreadLocal,以便在@Tool方法内部获取执行环境变量等
                                AiAgentContext.CONTEXT.set(context);
                                try {

                                    Map<String, Object> argumentsMap = jsonSerializer.deserializeAsMap(arguments);
                                    rawDefinition = toolMap.get(name);
                                    if (rawDefinition == null) {
                                        throw new IllegalArgumentException("not found this tool [" + name + "], please try others.");
                                    }
                                    Object val = ToolRawHelper.invokeTool(rawDefinition, argumentsMap, context.getToolInterceptor());
                                    if (val instanceof CharSequence) {
                                        callResult = String.valueOf(val);
                                    } else {
                                        String json = jsonSerializer.serialize(val);
                                        callResult = json;
                                    }
                                } catch (Throwable e) {
                                    callEx = e;
                                    failureCounter.incrementAndGet();
                                } finally {
                                    AiAgentContext.CONTEXT.remove();
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
    }
}
