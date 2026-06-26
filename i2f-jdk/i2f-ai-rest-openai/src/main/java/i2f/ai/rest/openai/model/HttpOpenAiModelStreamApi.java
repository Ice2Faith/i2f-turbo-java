package i2f.ai.rest.openai.model;

import i2f.ai.rest.openai.model.data.OpenAiCompletionReqDto;
import i2f.ai.rest.openai.model.data.chunk.OpenAiCompletionChunkRespDto;
import i2f.net.http.consts.CharsetConstants;
import i2f.net.http.consts.HttpHeaderConstants;
import i2f.net.http.data.HttpRequest;
import i2f.net.http.interfaces.IHttpProcessor;
import i2f.reference.Reference;
import i2f.reflect.ReflectResolver;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.serialize.str.json.impl.Json2Serializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
@SuperBuilder
public class HttpOpenAiModelStreamApi {
    protected IHttpProcessor httpProcessor;
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

    public void doPostHttp(OpenAiCompletionReqDto req, Consumer<Reference<OpenAiCompletionChunkRespDto>> chunkConsumer) {
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
                            .json()
                            .addHeader(HttpHeaderConstants.ContentEncoding, CharsetConstants.Utf8)
                            .applyHeader(headers -> {
                                if (apiKey != null && !apiKey.isEmpty()) {
                                    headers.add("Authorization", "Bearer " + apiKey);
                                }
                            })
                            .setData(reqMap)
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
