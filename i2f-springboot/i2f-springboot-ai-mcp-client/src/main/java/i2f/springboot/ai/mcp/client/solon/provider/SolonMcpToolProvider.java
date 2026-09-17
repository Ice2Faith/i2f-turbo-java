package i2f.springboot.ai.mcp.client.solon.provider;

import i2f.ai.std.mcp.McpToolProvider;
import i2f.ai.std.tags.AiTagRule;
import i2f.ai.std.tags.AiTagRuleHelper;
import i2f.ai.std.tool.ToolBaseCallRequest;
import i2f.ai.std.tool.definition.ToolDefinition;
import i2f.ai.std.tool.definition.impl.DefaultToolDefinition;
import i2f.ai.std.tool.schema.data.FunctionJsonSchema;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.serialize.str.json.impl.Json2Serializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.noear.solon.ai.chat.content.ContentBlock;
import org.noear.solon.ai.chat.tool.FunctionTool;
import org.noear.solon.ai.chat.tool.ToolResult;
import org.noear.solon.ai.mcp.client.McpClientProvider;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Ice2Faith
 * @date 2026/9/16 13:54
 * @desc
 */
@Data
@NoArgsConstructor
public class SolonMcpToolProvider implements McpToolProvider {
    protected IJsonSerializer jsonSerializer = new Json2Serializer();
    protected McpClientProvider mcpClient;

    protected String name;
    protected String description;
    protected List<AiTagRule> tagRules;

    protected long expireTtl = TimeUnit.MINUTES.toMillis(5);
    protected final CopyOnWriteArrayList<ToolDefinition> cache = new CopyOnWriteArrayList<>();
    protected final AtomicLong expireTs = new AtomicLong(0);
    protected final AtomicBoolean hasCache = new AtomicBoolean(false);
    protected final ReentrantLock lock = new ReentrantLock();

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<ToolDefinition> getTools() {
        if (hasCache.get() && System.currentTimeMillis() < expireTs.get()) {
            return new ArrayList<>(cache);
        }
        lock.lock();
        try {
            List<ToolDefinition> ret = new ArrayList<>();

            Collection<FunctionTool> tools = mcpClient.getTools();
            for (FunctionTool tool : tools) {
                DefaultToolDefinition def = new DefaultToolDefinition();
                def.setName(tool.name());
                def.setDescription(tool.description());

                String json = tool.inputSchema();
                Map<String, Object> parameters = jsonSerializer.deserializeAsMap(json);

                FunctionJsonSchema jsonSchema = new FunctionJsonSchema();
                jsonSchema.setName(def.getName());
                jsonSchema.setDescription(def.getDescription());
                jsonSchema.setParameters(parameters);
                jsonSchema.setStrict(true);
                def.setJsonSchema(jsonSchema);

                if(tagRules!=null){
                    List<String> tags = AiTagRuleHelper.resolveTags(def.getName(), tagRules);
                    def.getTags().addAll(tags);
                }

                ret.add(def);
            }

            cache.clear();
            cache.addAll(ret);
            expireTs.set(System.currentTimeMillis() + expireTtl);
            hasCache.set(true);

            return new ArrayList<>(ret);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean support(ToolBaseCallRequest request) {
        for (ToolDefinition tool : getTools()) {
            if (request.getName().equals(tool.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object callTool(ToolBaseCallRequest request) throws Throwable {
        String json = request.getArguments();
        Map<String, Object> args = jsonSerializer.deserializeAsMap(json);
        ToolResult toolResult = mcpClient.callTool(request.getName(), args);
        boolean error = toolResult.isError();
        if (error) {
            throw new IllegalStateException("call tool error, remote server internal error or parameters wrong!");
        }
        if (toolResult.isMultiModal()) {
            List<Map<String, Object>> ret = new ArrayList<>();
            List<ContentBlock> blocks = toolResult.getBlocks();
            for (ContentBlock block : blocks) {
                String mimeType = block.getMimeType();
                String content = block.getContent();

                Map<String, Object> item = new HashMap<>();
                item.put("mimeType", mimeType);
                item.put("content", content);
                ret.add(item);
            }
            return ret;
        }
        String result = toolResult.getContent();
        return result;
    }
}
