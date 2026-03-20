package i2f.ai.std.tool.test;


import i2f.ai.std.tool.ToolRawDefinition;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.ai.std.tool.schema.JsonSchema;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/3/19 15:06
 * @desc
 */
public class TestRawTool {
    public static void main(String[] args) {
        Map<String, Object> schema = JsonSchema.getTypeJsonSchema(TestSchemaPojo.class);
        System.out.println(schema);

        Map<String, ToolRawDefinition> map = ToolRawHelper.parseTools(TestToolComponent.class);
        System.out.println(map);
    }
}
