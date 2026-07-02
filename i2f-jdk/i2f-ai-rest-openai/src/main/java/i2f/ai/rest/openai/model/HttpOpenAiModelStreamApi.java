package i2f.ai.rest.openai.model;

import i2f.ai.rest.openai.model.data.*;
import i2f.ai.rest.openai.model.data.chunk.OpenAiCompletionChoiceChunk;
import i2f.ai.rest.openai.model.data.chunk.OpenAiCompletionChunkRespDto;
import i2f.net.http.consts.CharsetConstants;
import i2f.net.http.consts.HttpHeaderConstants;
import i2f.net.http.data.HttpRequest;
import i2f.net.http.impl.HttpUrlConnectProcessor;
import i2f.net.http.interfaces.IHttpProcessor;
import i2f.reference.Reference;
import i2f.reflect.ReflectResolver;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.serialize.str.json.impl.Json2Serializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2026/6/26 21:57
 * @desc
 */
@Data
@NoArgsConstructor
public class HttpOpenAiModelStreamApi {
    protected IHttpProcessor httpProcessor = new HttpUrlConnectProcessor();
    protected IJsonSerializer jsonSerializer = new Json2Serializer();
    protected String baseUrl;
    protected String apiKey;
    protected String model;

    public String getChatCompletionsUrl() {
        String ret = baseUrl;
        if (ret.endsWith("/")) {
            ret = ret.substring(0, ret.length() - 1);
        }
        return ret + "/chat/completions";
    }

    public OpenAiCompletionRespDto completion(OpenAiCompletionReqDto req) {
        OpenAiCompletionRespDto ret = new OpenAiCompletionRespDto();
        completionSse(req, ref -> {
            if (!ref.isValue()) {
                return;
            }
            OpenAiCompletionChunkRespDto chunk = ref.get();
            if (chunk == null) {
                return;
            }
            ret.setId(merge(ret.getId(), chunk.getId()));
            ret.setModel(merge(ret.getModel(), chunk.getModel()));
            ret.setObject(merge(ret.getObject(), chunk.getObject()));
            ret.setSystem_fingerprint(merge(ret.getSystem_fingerprint(), chunk.getSystem_fingerprint()));
            ret.setCreated(nvl(ret.getCreated(), 0L) + nvl(chunk.getCreated(), 0L));
            List<OpenAiCompletionChoiceChunk> chunkChoices = chunk.getChoices();
            if (chunkChoices != null) {
                if (ret.getChoices() == null) {
                    ret.setChoices(new ArrayList<>());
                }
                List<OpenAiCompletionChoice> choices = ret.getChoices();
                for (OpenAiCompletionChoiceChunk chunkChoice : chunkChoices) {
                    if (chunkChoice == null) {
                        continue;
                    }
                    Integer chunkIdx = chunkChoice.getIndex();
                    if (chunkIdx == null) {
                        chunkIdx = 0;
                    }
                    while (choices.size() <= chunkIdx) {
                        choices.add(new OpenAiCompletionChoice());
                    }
                    OpenAiCompletionChoice choice = choices.get(chunkIdx);
                    choice.setFinish_reason(merge(choice.getFinish_reason(), chunkChoice.getFinish_reason()));
                    choice.setLogprobs(nvl(choice.getLogprobs(), chunkChoice.getLogprobs()));
                    OpenAiAssistantMessageRespDto chunkDelta = chunkChoice.getDelta();
                    if (chunkDelta == null) {
                        continue;
                    }
                    if (choice.getMessage() == null) {
                        choice.setMessage(new OpenAiAssistantMessageRespDto());
                    }
                    OpenAiAssistantMessageRespDto message = choice.getMessage();
                    message.setContent(merge(message.getContent(), chunkDelta.getContent()));
                    message.setReasoning_content(merge(message.getReasoning_content(), chunkDelta.getReasoning_content()));
                    List<OpenAiToolCall> chunkCalls = chunkDelta.getTool_calls();
                    if (chunkCalls == null) {
                        continue;
                    }
                    if (message.getTool_calls() == null) {
                        message.setTool_calls(new ArrayList<>());
                    }
                    List<OpenAiToolCall> calls = message.getTool_calls();
                    for (OpenAiToolCall chunkCall : chunkCalls) {
                        if (chunkCall == null) {
                            continue;
                        }
                        Integer callIdx = chunkCall.getIndex();
                        if (callIdx == null) {
                            callIdx = 0;
                        }
                        while (calls.size() <= callIdx) {
                            OpenAiToolCall elem = new OpenAiToolCall();
                            elem.setType(null);
                            calls.add(elem);
                        }
                        OpenAiToolCall call = calls.get(callIdx);
                        call.setIndex(nvl(callIdx, 0));
                        call.setId(merge(call.getId(), chunkCall.getId()));
                        call.setType(merge(call.getType(), chunkCall.getType()));
                        OpenAiToolCallFunction chunkFunc = chunkCall.getFunction();
                        if (chunkFunc == null) {
                            continue;
                        }
                        if (call.getFunction() == null) {
                            call.setFunction(new OpenAiToolCallFunction());
                        }
                        OpenAiToolCallFunction func = call.getFunction();
                        func.setName(merge(func.getName(), chunkFunc.getName()));
                        func.setArguments(merge(func.getArguments(), chunkFunc.getArguments()));
                    }
                }
            }
            OpenAiCompletionUsage chunkUsage = chunk.getUsage();
            if (chunkUsage != null) {
                if (ret.getUsage() == null) {
                    ret.setUsage(new OpenAiCompletionUsage());
                }
                OpenAiCompletionUsage usage = ret.getUsage();
                usage.setCompletion_tokens(nvl(usage.getCompletion_tokens(), 0L) + nvl(chunkUsage.getCompletion_tokens(), 0L));
                usage.setPrompt_tokens(nvl(usage.getPrompt_tokens(), 0L) + nvl(chunkUsage.getPrompt_tokens(), 0L));
                usage.setTotal_tokens(nvl(usage.getTotal_tokens(), 0L) + nvl(chunkUsage.getTotal_tokens(), 0L));
            }
        });
        return ret;
    }

    public static String merge(String s1, String s2) {
        if (s1 == null && s2 == null) {
            return null;
        }
        return nvl(s1, "") + nvl(s2, "");
    }

    public static <T> T nvl(T value, T def) {
        return value == null ? def : value;
    }

    public void completionSse(OpenAiCompletionReqDto req, Consumer<Reference<OpenAiCompletionChunkRespDto>> chunkConsumer) {
        try {
            // 强制开启流输出，进行sse
            req.setStream(true);
            req.setStream_options(new HashMap<>());
            req.getStream_options().put("include_usage", true);

            // openai 标准有强制的字段校验，因此空字段需要移除掉
            Map<String, Object> reqMap = new HashMap<>();
            ReflectResolver.bean2map(req, reqMap);
            Set<String> removeKeys = new HashSet<>();
            for (Map.Entry<String, Object> entry : reqMap.entrySet()) {
                Object value = entry.getValue();
                if (value == null) {
                    removeKeys.add(entry.getKey());
                } else if (value instanceof Collection) {
                    Collection<?> col = (Collection<?>) value;
                    if (col.isEmpty()) {
                        removeKeys.add(entry.getKey());
                    }
                } else if (value instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) value;
                    if (map.isEmpty()) {
                        removeKeys.add(entry.getKey());
                    }
                }
            }
            for (String key : removeKeys) {
                reqMap.remove(key);
            }

            httpProcessor.http(HttpRequest.doPost(getChatCompletionsUrl())
                            .set(u -> u::json)
                            .set2(u -> u::addHeader, HttpHeaderConstants.ContentEncoding, CharsetConstants.Utf8)
                            .set(u -> u::applyHeader, headers -> {
                                if (apiKey != null && !apiKey.isEmpty()) {
                                    headers.add("Authorization", "Bearer " + apiKey);
                                }
                            })
                            .set(u -> u::setData, reqMap)
                            .build()
                    , response -> {
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getInputStream(), StandardCharsets.UTF_8))) {
                            String line = null;
                            while ((line = reader.readLine()) != null) {
                                if (line.startsWith("data:")) {
                                    String data = line.substring(5).trim();
                                    OpenAiCompletionChunkRespDto resp = (OpenAiCompletionChunkRespDto) jsonSerializer.deserialize(data, OpenAiCompletionChunkRespDto.class);
                                    chunkConsumer.accept(Reference.of(resp));
                                } else if ("[DONE]".equals(line.trim())) {
                                    break;
                                }
                            }
                        }
                        chunkConsumer.accept(Reference.finish());
                        return null;
                    });
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
