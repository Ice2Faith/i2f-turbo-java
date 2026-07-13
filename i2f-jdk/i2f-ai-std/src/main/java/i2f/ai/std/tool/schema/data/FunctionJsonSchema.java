package i2f.ai.std.tool.schema.data;

import i2f.ai.std.tool.schema.JsonSchema;
import i2f.mutator.BaseMutator;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/7/13 14:01
 * @desc
 */
@Data
@NoArgsConstructor
public class FunctionJsonSchema implements IJsonSchema, BaseMutator<FunctionJsonSchema> {
    protected String name;
    protected String description;
    protected boolean strict;
    protected Map<String, Object> parameters;

    public FunctionJsonSchema copyOf() {
        FunctionJsonSchema ret = new FunctionJsonSchema();
        ret.setName(name);
        ret.setDescription(description);
        ret.setStrict(strict);
        ret.setParameters(parameters);
        return ret;
    }

    @Override
    public Map<String, Object> toJsonSchemaMap() {
        Map<String, Object> ret = new LinkedHashMap<>();
        ret.put(JsonSchema.SchemaField.NAME, name);
        ret.put(JsonSchema.SchemaField.DESCRIPTION, description);
        ret.put(JsonSchema.SchemaField.STRICT, strict);
        ret.put(JsonSchema.SchemaField.PARAMETERS, parameters);

        return ret;
    }
}
